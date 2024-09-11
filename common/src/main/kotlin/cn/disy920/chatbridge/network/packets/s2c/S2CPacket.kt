package cn.disy920.chatbridge.network.packets.s2c

import cn.disy920.chatbridge.network.packets.Packet
import cn.disy920.chatbridge.utils.ReflectionUtil
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlin.reflect.KClass
import kotlin.reflect.full.companionObjectInstance
import kotlin.reflect.full.memberProperties

@Serializable
abstract class S2CPacket<T : Any>(
    override val type: String,
    @Transient override val reqGroup: LongArray? = null,
    @Transient override val args: T? = null
) : Packet<T> {
    companion object {
        private val packetCache = HashMap<String, KClass<out S2CPacket<*>>>(32)

        init {
            for (subClass in ReflectionUtil.getSubClasses<S2CPacket<*>>()) {
                val companion = (subClass.companionObjectInstance) ?: continue
                val type = companion::class.memberProperties
                    .find { it.name == "TYPE" }
                    ?.getter
                    ?.call(companion) as? String
                    ?: throw IllegalStateException("Cannot find the TYPE property in the class ${subClass.simpleName}")

                packetCache[type] = subClass
            }

            println(packetCache)
        }

        @JvmStatic
        fun getPacketClass(type: String) = packetCache[type]
    }

    abstract fun onReceive()
}