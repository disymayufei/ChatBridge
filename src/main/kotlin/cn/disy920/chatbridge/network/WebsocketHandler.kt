package cn.disy920.chatbridge.network

import cn.disy920.chatbridge.network.packets.c2s.C2SPacket
import java.net.URI

class WebsocketHandler(
    serverAddress: URI,
    retryInterval: Long
) {
    private val websocketConnection: WebsocketConnection = WebsocketConnection(serverAddress, retryInterval)

    init {
        if (!websocketConnection.living) websocketConnection.connect()
    }

    fun sendPacket(packet: C2SPacket) {
        if (!websocketConnection.living) throw ConnectionLostException("与服务器的连接已断开")
        websocketConnection.send(packet.encodeToJson())
    }

    fun disconnect() {
        if (websocketConnection.living) websocketConnection.disconnect()
    }

    internal class ConnectionLostException : RuntimeException {
        constructor(reason: String) : super(reason)
        constructor(reason: String, cause: Throwable) : super(reason, cause)
    }
}