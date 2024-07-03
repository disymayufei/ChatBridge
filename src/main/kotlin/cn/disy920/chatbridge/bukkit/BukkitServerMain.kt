package cn.disy920.chatbridge.bukkit

import cn.disy920.chatbridge.Main
import cn.disy920.chatbridge.bukkit.listener.PlayerListener
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import java.net.URI

class BukkitServerMain : JavaPlugin() {
    override fun onEnable() {
        saveDefaultConfig()

        val serverName = config.getString("server_name", "unknown-server")!!
        val host = config.getString("host", "localhost")!!
        val port = config.getInt("port", 16123)
        val connectionRetryInterval = config.getLong("retry_interval", 3000L)
        Main.serverHandler = BukkitServerHandler(serverName, URI("ws://$host:$port"), connectionRetryInterval)

        Bukkit.getPluginManager().registerEvents(PlayerListener(), this)

        Main.onEnable()
    }

    override fun onDisable() {
        Main.onDisable()
    }
}