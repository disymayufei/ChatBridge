package cn.disy920.chatbridge.config

import kotlinx.serialization.Serializable

@Serializable
data class Config(
    val serverName: String = "unknown-server",
    val retryInterval: Long = 3000L,
    val host: String = "localhost",
    val port: Int = 16123,
    val messageConfig: MessageConfig = MessageConfig()
)

@Serializable
data class MessageConfig(
    val transferChat: Boolean = true,
    val transferMCDRCommand: Boolean = false,
    val transferPlayerChange: Boolean = true,
    val transferServerLifecycle: Boolean = true
)