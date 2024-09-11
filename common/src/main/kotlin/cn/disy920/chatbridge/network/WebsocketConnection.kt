package cn.disy920.chatbridge.network

import cn.disy920.chatbridge.Main.Companion.main
import cn.disy920.chatbridge.intermediate.Logger
import cn.disy920.chatbridge.network.packets.Packet
import cn.disy920.chatbridge.network.packets.c2s.PingC2SPacket
import cn.disy920.chatbridge.network.packets.s2c.S2CPacket
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.serializer
import org.java_websocket.client.WebSocketClient
import org.java_websocket.drafts.Draft_6455
import org.java_websocket.handshake.ServerHandshake
import java.net.ConnectException
import java.net.URI

class WebsocketConnection(
    address: URI,
    private val retryInterval: Long
) : WebSocketClient(address, Draft_6455(), null, 2000) {

    /**
     * 表示连接是否被主动中止，值为false则表示已经被主动中止
     * 被主动中止的连接不应该再被使用或服用，如果仍有需要，必须重建连接
     * 该值不代表连接已成功建立，若要判断连接是否成功建立，请使用isOpen方法
     */
    var living = false

    private val logger: Logger
        get() = main.logger

    private var reconnecting = false
    private var lastConnectTime = 0L
    private var heartbeatThread: Thread? = null

    private val pingJson = PingC2SPacket.encodeToJson()

    private val heartbeatTask = Runnable {
        while (isOpen && !Thread.currentThread().isInterrupted) {
            send(pingJson)
            sendPing()
            try {
                Thread.sleep(500)
            } catch (e: InterruptedException) {
                return@Runnable
            }
        }
    }

    init {
        this.isReuseAddr = true
    }

    override fun onOpen(handshakedata: ServerHandshake?) {
        resetHeartbeatThread()

        reconnecting = false

        val socketAddress = this.remoteSocketAddress
        logger.info(String.format("成功连接至跨服聊天服务器：ws://%s:%s", socketAddress.hostString, socketAddress.port))
    }

    @OptIn(InternalSerializationApi::class)
    override fun onMessage(message: String) {
        val packetJsonObj = try {
            Packet.CODEC.parseToJsonElement(message).jsonObject
        }
        catch (e: Exception) {
            logger.error("接收到非法数据包: $message")
            logger.error("以下是解析时的错误堆栈信息: ", e)
            return
        }

        val packetType = packetJsonObj["type"]?.jsonPrimitive?.content ?: return
        val packetClass = S2CPacket.getPacketClass(packetType) ?: let {
            logger.warn("接收到无法处理的包类型: $packetType, 跳过处理...")
            return
        }

        val packet = Packet.CODEC.decodeFromJsonElement(packetClass.serializer(), packetJsonObj)
        packet.onReceive()
    }

    override fun onClose(code: Int, reason: String, remote: Boolean) {
        if (living) autoReconnect()
    }

    override fun onError(ex: Exception) {
        if (ex is ConnectException) {
            // 这就是单纯的与服务器断开连接，实际上应该忽略掉，等待自动重连就行，避免log刷屏
            logger.info("与跨服聊天的服务器连接断开，正在尝试自动重连...")
            return
        }

        logger.error("与跨服聊天的服务器连接发生错误: ", ex)
    }

    override fun connect() {
        living = true
        super.connect()
    }

    fun disconnect() {
        living = false
        close()
    }

    private fun resetHeartbeatThread() {
        heartbeatThread?.interrupt()
        heartbeatThread = Thread(heartbeatTask)
        heartbeatThread!!.start()
    }

    private fun autoReconnect() {
        if (!living || reconnecting) return
        reconnecting = true

        heartbeatThread?.interrupt()

        val currentThread = Thread.currentThread()

        Thread {
            while (!currentThread.isAlive && living && !isOpen) {
                try {
                    val currentTime = System.currentTimeMillis()
                    if (currentTime - lastConnectTime < retryInterval) {
                        Thread.sleep(1000)
                        continue
                    }
                    else {
                        lastConnectTime = currentTime
                    }

                    reconnect()
                }
                catch (_: InterruptedException) {
                    return@Thread
                }
            }
        }.start()
    }
}