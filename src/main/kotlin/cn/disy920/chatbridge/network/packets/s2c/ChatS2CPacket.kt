package cn.disy920.chatbridge.network.packets.s2c

data class ChatS2CPacket(
    val serverName: String,
    val playerName: String,
    val chatMessage: String
) : S2CPacket