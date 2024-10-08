package cn.disy920.chatbridge.bukkit

import cn.disy920.chatbridge.Main
import cn.disy920.chatbridge.bukkit.listener.PlayerListener
import cn.disy920.chatbridge.bukkit.warpper.BukkitLogger
import cn.disy920.chatbridge.config.ConfigLoader
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import java.net.URI

class BukkitServerMain : JavaPlugin() {
    private lateinit var pluginInstance: Main

    override fun onLoad() {
        val configLoader = ConfigLoader(this.dataFolder)
        val config = configLoader.loadConfig()

        pluginInstance = Main(
            configLoader,
            BukkitLogger(this.logger),
            BukkitServerHandler(
                config.serverName,
                URI("ws://${config.host}:${config.port}"),
                config.retryInterval
            )
        )

        pluginInstance.onLoad()
    }

    override fun onEnable() {
        pluginInstance.onEnable()
        Bukkit.getPluginManager().registerEvents(PlayerListener(), this)
    }

    override fun onDisable() {
        pluginInstance.onDisable()
    }
}