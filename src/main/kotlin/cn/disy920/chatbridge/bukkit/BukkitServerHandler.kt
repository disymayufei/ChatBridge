package cn.disy920.chatbridge.bukkit

import cn.disy920.chatbridge.ServerHandler
import org.bukkit.Bukkit
import java.net.URI

class BukkitServerHandler(
    override val serverName: String,
    serverAddress: URI,
    websocketRetryInterval: Long
) : ServerHandler(serverName, serverAddress, websocketRetryInterval) {
    override fun broadcast(text: String) {
        Bukkit.broadcastMessage(text)
    }
}