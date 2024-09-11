package cn.disy920.chatbridge.fabric.warpper

import cn.disy920.chatbridge.Main
import cn.disy920.chatbridge.intermediate.Logger

class FabricLogger(
    private val logger: org.slf4j.Logger
) : Logger {
    override fun info(msg: String) {
        logger.info(msg)
    }

    override fun debug(msg: String) {
        if (Main.main.config.debug) logger.debug("Debug - $msg")
    }

    override fun warn(msg: String) {
        logger.warn(msg)
    }

    override fun error(msg: String) {
        logger.error(msg)
    }

    override fun error(msg: String, error: Throwable) {
        logger.error(msg, error)
    }

}