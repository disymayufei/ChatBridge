package cn.disy920.chatbridge.network.packets.c2s

import cn.disy920.chatbridge.network.packets.Packet
import kotlinx.serialization.Serializable

data class ChatC2SPacket(
    val serverName: String,
    val playerName: String,
    val chatMessage: String
) : C2SPacket<ChatC2SPacket.ChatPacketArgs>(
    type = "chatBridge",
    reqGroup = LongArray(0),
    args = ChatPacketArgs(
        serverName,
        playerName,
        chatMessage
    )
) {
    @Serializable
    data class ChatPacketArgs(
        val identity: String,
        val sender: String,
        val text: String
    )
}