package cn.disy920.chatbridge.listener

import cn.disy920.chatbridge.Main.Companion.main
import cn.disy920.chatbridge.network.packets.c2s.ChatC2SPacket
import cn.disy920.chatbridge.network.packets.c2s.PlayerJoinC2SPacket
import cn.disy920.chatbridge.network.packets.c2s.PlayerQuitC2SPacket

object Listeners {
    fun onChat(playerName: String, message: String) {
        val messageConfig = main.config.messageConfig
        if (!messageConfig.transferChat) return
        if (message.startsWith("!!") && !messageConfig.transferMCDRCommand) return

        val serverName = main.serverHandler.serverName
        main.serverHandler.websocketHandler.sendPacket(ChatC2SPacket(serverName, playerName, message))
    }

    fun onPlayerJoin(playerName: String) {
        val messageConfig = main.config.messageConfig
        if (!messageConfig.transferPlayerChange) return

        val serverName = main.serverHandler.serverName
        main.serverHandler.websocketHandler.sendPacket(PlayerJoinC2SPacket(serverName, playerName))
    }

    fun onPlayerQuit(playerName: String) {
        val messageConfig = main.config.messageConfig
        if (!messageConfig.transferPlayerChange) return

        val serverName = main.serverHandler.serverName
        main.serverHandler.websocketHandler.sendPacket(PlayerQuitC2SPacket(serverName, playerName))
    }
}