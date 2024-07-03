package cn.disy920.chatbridge.fabric

import cn.disy920.chatbridge.ServerHandler
import net.minecraft.server.MinecraftServer
import net.minecraft.text.Text
import java.net.URI

class FabricServerHandler(
    private val server: MinecraftServer,
    override val serverName: String,
    serverAddress: URI,
    websocketRetryInterval: Long
) : ServerHandler(serverName, serverAddress, websocketRetryInterval) {
    override fun broadcast(text: String) {
        server.playerManager.broadcast(
            Text.literal(text),
            false
        )
    }

}