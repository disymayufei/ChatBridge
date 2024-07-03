package cn.disy920.chatbridge

import org.slf4j.Logger
import org.slf4j.LoggerFactory

class Main {
    companion object {
        val logger: Logger = LoggerFactory.getLogger(Main::class.java)
        var serverHandler: ServerHandler? = null

        fun onEnable() {
            logger.info("跨服聊天组件已启动，作者: Disy920!")
        }

        fun onDisable() {
            serverHandler?.websocketHandler?.disconnect()
            logger.info("跨服聊天组件已关闭，有缘再见!")
        }
    }
}