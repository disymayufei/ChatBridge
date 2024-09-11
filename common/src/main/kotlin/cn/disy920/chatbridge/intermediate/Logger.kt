package cn.disy920.chatbridge.intermediate

interface Logger {
    fun info(msg: String)
    fun debug(msg: String)
    fun warn(msg: String)
    fun error(msg: String)
    fun error(msg: String, error: Throwable)
}