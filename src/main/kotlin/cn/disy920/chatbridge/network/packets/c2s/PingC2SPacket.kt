package cn.disy920.chatbridge.network.packets.c2s

object PingC2SPacket : C2SPacket {
    override fun encodeToJson(): String {
        return "{\"type\": \"ping\", \"args\":{}}"
    }
}