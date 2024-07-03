package cn.disy920.chatbridge.network.packets.c2s

import cn.disy920.chatbridge.network.packets.Packet

interface C2SPacket : Packet {
    fun encodeToJson(): String
}