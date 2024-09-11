package cn.disy920.chatbridge.bukkit.warpper

import cn.disy920.chatbridge.Main.Companion.main
import cn.disy920.chatbridge.intermediate.Logger
import java.util.logging.Level

class BukkitLogger(private val logger: java.util.logging.Logger) : Logger {
    override fun info(msg: String) {
        logger.info(msg)
    }

    override fun debug(msg: String) {
        // 这里调用main被认为是安全的: 在Main类初始化的时候，就已经完成了Main.main的赋值
        if (main.config.debug) logger.warning("Debug - $msg")
    }

    override fun warn(msg: String) {
        logger.warning(msg)
    }

    override fun error(msg: String) {
        logger.severe(msg)
    }

    override fun error(msg: String, error: Throwable) {
        logger.log(Level.SEVERE, msg, error)
    }
}