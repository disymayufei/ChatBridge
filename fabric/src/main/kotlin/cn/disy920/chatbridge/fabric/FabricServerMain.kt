package cn.disy920.chatbridge.fabric

import cn.disy920.chatbridge.Main
import cn.disy920.chatbridge.config.ConfigLoader
import cn.disy920.chatbridge.fabric.warpper.FabricLogger
import cn.disy920.chatbridge.listener.Listeners
import com.mojang.logging.LogUtils
import net.fabricmc.api.DedicatedServerModInitializer
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents
import net.fabricmc.fabric.api.message.v1.ServerMessageEvents
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.server.MinecraftServer
import java.net.URI

class FabricServerMain : DedicatedServerModInitializer {
    companion object {
        lateinit var server: MinecraftServer
        lateinit var modInstance: Main

        const val MOD_ID = "cn.disy920.chatbridge"
    }
    override fun onInitializeServer() {
        val configLoader = ConfigLoader(FabricLoader.getInstance().configDir.toFile())
        val config = configLoader.loadConfig()

        ServerLifecycleEvents.SERVER_STARTING.register { server ->
            FabricServerMain.server = server

            modInstance = Main(
                configLoader,
                FabricLogger(LogUtils.getLogger()),
                FabricServerHandler(
                    server,
                    config.serverName,
                    URI("ws://${config.host}:${config.port}"),
                    config.retryInterval
                )
            )

            modInstance.onLoad()
        }

        ServerLifecycleEvents.SERVER_STARTED.register { _ ->
            modInstance.onEnable()
        }

        registerEvents()
    }

    private fun registerEvents() {
        ServerLifecycleEvents.SERVER_STOPPING.register { _ ->
            modInstance.onDisable()
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