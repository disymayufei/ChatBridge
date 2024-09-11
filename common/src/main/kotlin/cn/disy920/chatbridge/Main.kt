package cn.disy920.chatbridge

import cn.disy920.chatbridge.config.Config
import cn.disy920.chatbridge.config.ConfigLoader
import cn.disy920.chatbridge.intermediate.Logger
import cn.disy920.chatbridge.network.packets.c2s.ChatC2SPacket

class Main(
    private val configLoader: ConfigLoader,
    val logger: Logger,
    val serverHandler: ServerHandler
) {

    companion object {
        lateinit var main: Main
    }

    init {
        main = this
    }

    val config: Config
        get() = configLoader.loadConfig()

    fun onLoad() {
        serverHandler.websocketHandler.connect()
    }

    fun onEnable() {
        val messageConfig = config.messageConfig
        if (messageConfig.transferServerLifecycle) {
            serverHandler.websocketHandler.sendPacket(ChatC2SPacket(config.serverName, "server", "§e服务器正在启动..."))
        }
        logger.info("跨服聊天组件已启动，作者: Disy920!")
    }

    fun onDisable() {
        val messageConfig = config.messageConfig
        if (messageConfig.transferServerLifecycle) {
            serverHandler.websocketHandler.sendPacket(ChatC2SPacket(config.serverName, "server", "§6服务器正在关闭..."))
        }

        serverHandler.websocketHandler.disconnect()
        logger.info("跨服聊天组件已关闭，有缘再见!")
    }

    fun reloadConfig() {
        configLoader.reloadConfig()
    }
}