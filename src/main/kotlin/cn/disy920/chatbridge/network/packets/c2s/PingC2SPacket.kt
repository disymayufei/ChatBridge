package cn.disy920.chatbridge.network.packets.c2s

class PingC2SPacket : C2SPacket {
    companion object {
        val INSTANCE = PingC2SPacket()
    }

    override fun encodeToJson(): String {
        return "{\"type\": \"ping\", \"args\":{}}"
    }
}