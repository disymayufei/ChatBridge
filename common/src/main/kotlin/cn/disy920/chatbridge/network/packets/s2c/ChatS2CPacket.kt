package cn.disy920.chatbridge.network.packets.s2c

import cn.disy920.chatbridge.Main.Companion.main
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

data class ChatS2CPacket(
    override val args: ChatPacketArgs
) : S2CPacket<ChatS2CPacket.ChatPacketArgs>(
    type = TYPE,
    reqGroup = null,
    args = args
) {
    companion object {
        @JvmStatic
        val TYPE = "chatBridge"
    }

    @Serializable
    data class ChatPacketArgs(
        @SerialName("identity")
        val serverName: String,
        @SerialName("sender")
        val sender: String,
        @SerialName("text")
        val text: String
    )

    override fun onReceive() {
        main.serverHandler.broadcast("<${args.serverName}§r | ${args.sender}§r> ${args.text}")
    }
}