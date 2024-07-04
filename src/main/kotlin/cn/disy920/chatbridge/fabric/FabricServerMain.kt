package cn.disy920.chatbridge.fabric

import cn.disy920.chatbridge.Main
import cn.disy920.chatbridge.fabric.config.ModConfigLoader
import cn.disy920.chatbridge.listener.Listeners
import net.fabricmc.api.DedicatedServerModInitializer
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents
import net.fabricmc.fabric.api.message.v1.ServerMessageEvents
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents
import net.minecraft.server.MinecraftServer
import java.net.URI

class FabricServerMain : DedicatedServerModInitializer {
    companion object {
        lateinit var server: MinecraftServer
        const val MOD_ID = "cn.disy920.chatbridge"
    }
    override fun onInitializeServer() {
        ModConfigLoader.saveDefaultConfig()

        ServerLifecycleEvents.SERVER_STARTING.register { server ->
            val config = ModConfigLoader.loadConfig()


            FabricServerMain.server = server
            Main.serverHandler = FabricServerHandler(
                server,
                config.serverName,
                URI("ws://${config.host}:${config.port}"),
                config.retryInterval)
            Main.onEnable()
        }

        registerEvents()
    }

    private fun registerEvents() {
        ServerLifecycleEvents.SERVER_STOPPING.register { _ ->
            Main.onDisable()
        }

        ServerPlayConnectionEvents.JOIN.register { handler, _, _ ->
            Listeners.onPlayerJoin(handler.player.name.string)
        }

        ServerPlayConnectionEvents.DISCONNECT.register { handler,  _ ->
            Listeners.onPlayerQuit(handler.player.name.string)
        }

        ServerMessageEvents.CHAT_MESSAGE.register { message, player, _ ->
            Listeners.onChat(player.name.string, message.signedBody.content)
        }
    }
}