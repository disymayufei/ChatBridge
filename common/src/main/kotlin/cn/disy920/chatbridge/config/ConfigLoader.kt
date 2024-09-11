package cn.disy920.chatbridge.config

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File

class ConfigLoader(private val rootDir: File) {
    private val configFile = File(rootDir, "config.json")
    private lateinit var config: Config

    companion object {
        private val Json: Json = Json {
            ignoreUnknownKeys = true
            isLenient = true
            encodeDefaults = true
            prettyPrint = true
        }
    }

    fun loadConfig(): Config {
        if (!rootDir.isDirectory) rootDir.mkdirs()
        if (!configFile.isFile) {
            configFile.createNewFile()
            config = Config()
            configFile.writeText(Json.encodeToString(config))
            return config
        }

        if (!::config.isInitialized) {
            config = Json.decodeFromString(configFile.readText())
        }

        return config
    }

    fun reloadConfig() {
        if (!rootDir.isDirectory || !configFile.isFile) {
            loadConfig()
            return
        }

        config = Json.decodeFromString(configFile.readText())
    }
}