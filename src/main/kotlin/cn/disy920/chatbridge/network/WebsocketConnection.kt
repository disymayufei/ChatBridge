package cn.disy920.chatbridge.network

import cn.disy920.chatbridge.Main
import cn.disy920.chatbridge.network.packets.Packet
import cn.disy920.chatbridge.network.packets.c2s.PingC2SPacket
import com.google.gson.JsonObject
import com.google.gson.JsonSyntaxException
import org.java_websocket.client.WebSocketClient
import org.java_websocket.drafts.Draft_6455
import org.java_websocket.handshake.ServerHandshake
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
        Main.logger.info(String.format("成功连接至跨服聊天服务器：ws://%s:%s", socketAddress.hostString, socketAddress.port))
    }

    override fun onMessage(message: String) {
        val receiveObj: JsonObject

        try {
            receiveObj = Packet.GSON.fromJson(message, JsonObject::class.java)
        }
        catch (e: JsonSyntaxException) {
            Main.logger.error("解析服务端包时发现异常的JSON格式: ", e)
            return
        }

        val header = receiveObj.get("type").asString
        try {
            when (header) {
                "chatBridge" -> {
                    val chat = receiveObj.getAsJsonObject("args")

                    val identity = chat["identity"].asString
                    val sender = chat["sender"].asString
                    val text = chat["text"].asString

                    val chatText = String.format("<%s§r | %s§r> %s", identity, sender, text)

                    Main.logger.info(chatText)
                    Main.serverHandler?.broadcast(chatText)
                }
            }
        }
        catch (e: Exception) {
            Main.logger.error("解析${header}包时发生异常: ", e)
            e.printStackTrace()
        }

    }

    override fun onClose(code: Int, reason: String, remote: Boolean) {
        if (living) autoReconnect()
    }

    override fun onError(ex: Exception) {
        Main.logger.error("与跨服聊天的服务器连接发生错误: ", ex)
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