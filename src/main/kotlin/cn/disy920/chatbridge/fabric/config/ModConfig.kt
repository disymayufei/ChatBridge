package cn.disy920.chatbridge.fabric.config

import kotlinx.serialization.Serializable

@Serializable
data class ModConfig(
    val serverName: String = "unknown-server",
    val retryInterval: Long = 3000L,
    val host: String = "localhost",
    val port: Int = 16123
)
