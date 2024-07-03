package cn.disy920.chatbridge.network.packets.c2s

import cn.disy920.chatbridge.network.packets.Packet

data class PlayerQuitC2SPacket(
    val serverName: String,
    val playerName: String
) : C2SPacket {
    override fun encodeToJson(): String {
        return Packet.GSON.toJson(
            mapOf(
                "type" to "chatBridge",
                "reqGroup" to LongArray(1),
                "args" to mapOf(
                    "identity" to serverName,
                    "sender" to "server",
                    "text" to "§e玩家${playerName}离开了该服务器§r"
                )
            )
        )
    }
}
