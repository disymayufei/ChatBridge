package cn.disy920.chatbridge.network.packets.c2s

object PingC2SPacket : C2SPacket<Map<Nothing, Nothing>>(
    type = "ping",
    reqGroup = null,
    args = emptyMap()
)