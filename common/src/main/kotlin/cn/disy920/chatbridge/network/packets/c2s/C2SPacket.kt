package cn.disy920.chatbridge.network.packets.c2s

import cn.disy920.chatbridge.network.packets.Packet
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlin.reflect.KClass

@Serializable
abstract class C2SPacket<T : Any>(
    override val type: String,
    override val reqGroup: LongArray?,
    override val args: T
) : Packet<T> {
    @Suppress("UNCHECKED_CAST")
    open fun encodeToJson(): String {
        val argClass: KClass<T> = args::class as KClass<T>

        val packet: MutableMap<String, Any> = mutableMapOf("type" to type)
        if (argClass != Unit::class) packet["args"] = args
        if (reqGroup != null) packet["reqGroup"] = reqGroup!!

        return Packet.CODEC.encodeToString(packet)
    }
}