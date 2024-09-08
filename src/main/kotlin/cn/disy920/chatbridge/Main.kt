package cn.disy920.chatbridge

import cn.disy920.chatbridge.config.Config
import cn.disy920.chatbridge.config.ConfigLoader
import cn.disy920.chatbridge.network.packets.c2s.ChatC2SPacket
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class Main(
    private val configLoader: ConfigLoader,
    val serverHandler: ServerHandler
) {

    companion object {
        lateinit var main: Main
    }

    init {
        main = this
    }

    val logger: Logger = LoggerFactory.getLogger(Main::class.java)
    val config: Config
        get() = configLoader.loadConfig()


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