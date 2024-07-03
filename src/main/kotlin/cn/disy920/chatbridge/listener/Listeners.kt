package cn.disy920.chatbridge.listener

import cn.disy920.chatbridge.Main
import cn.disy920.chatbridge.network.packets.c2s.ChatC2SPacket
import cn.disy920.chatbridge.network.packets.c2s.PlayerJoinC2SPacket
import cn.disy920.chatbridge.network.packets.c2s.PlayerQuitC2SPacket

class Listeners {
    companion object {
        fun onChat(playerName: String, message: String) {
            val serverName = Main.serverHandler?.serverName ?: return
            Main.serverHandler?.websocketHandler?.sendPacket(ChatC2SPacket(serverName, playerName, message))
        }

        fun onPlayerJoin(playerName: String) {
            val serverName = Main.serverHandler?.serverName ?: return
            Main.serverHandler?.websocketHandler?.sendPacket(PlayerJoinC2SPacket(serverName, playerName))
        }

        fun onPlayerQuit(playerName: String) {
            val serverName = Main.serverHandler?.serverName ?: return
            Main.serverHandler?.websocketHandler?.sendPacket(PlayerQuitC2SPacket(serverName, playerName))
        }
    }
}