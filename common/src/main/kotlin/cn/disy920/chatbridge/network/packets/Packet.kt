package cn.disy920.chatbridge.network.packets

import kotlinx.serialization.json.Json

interface Packet<T : Any> {
    val type: String
    val reqGroup: LongArray?
    val args: T?
    companion object {
        @JvmStatic
        val CODEC: Json = Json {
            ignoreUnknownKeys = true
            isLenient = true
            encodeDefaults = true
            explicitNulls = false
        }
    }
}