package cn.disy920.chatbridge

import cn.disy920.chatbridge.network.WebsocketHandler
import java.net.URI

abstract class ServerHandler(
    open val serverName: String,
    serverAddress: URI,
    websocketRetryInterval: Long
) {
    val websocketHandler = WebsocketHandler(serverAddress, websocketRetryInterval)
    abstract fun broadcast(text: String)
}