package cn.disy920.chatbridge.network.packets.c2s

import cn.disy920.chatbridge.network.packets.Packet

data class PlayerQuitC2SPacket(
    val serverName: String,
    val playerName: String
) : C2SPacket<ChatC2SPacket.ChatPacketArgs>(
    type = "chatBridge",
    reqGroup = LongArray(0),
    args = ChatC2SPacket.ChatPacketArgs(
        serverName,
        "server",
        "§e玩家${playerName}离开了该服务器§r"
    )
)
