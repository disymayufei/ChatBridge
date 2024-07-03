package cn.disy920.chatbridge.fabric.config

import cn.disy920.chatbridge.fabric.FabricServerMain.Companion.MOD_ID
import kotlinx.serialization.json.Json
import net.fabricmc.loader.api.FabricLoader
import java.io.File
import java.io.FileNotFoundException
import java.io.FileReader
import java.nio.file.Files
import java.nio.file.Path

class ModConfigLoader {
    companion object {
        private val Json: Json = Json {
            ignoreUnknownKeys = true
            isLenient = true
        }

        private val configDir = File(FabricLoader.getInstance().configDir.toFile(), "Chat Bridge")
        private val configFile: File = File(configDir, "config.json")

        fun loadConfig(): ModConfig {
            if (!configFile.isFile) {
                throw FileNotFoundException("Couldn't not found the config file!")
            }
            FileReader(configFile).use { reader ->
                return Json.decodeFromString<ModConfig>(reader.readText())
            }
        }

        fun saveDefaultConfig() {
            if (!configDir.isDirectory) {
                configDir.mkdirs()
            }

            if (!configFile.isFile) {
                object {}.javaClass.getResourceAsStream("/${MOD_ID.replace(".", "/")}/config/config.json")
                    .use { resource ->
                        if (resource != null) {
                            configFile.createNewFile()
                            Files.writeString(configFile.toPath(), String(resource.readAllBytes()))
                        }
                    }
            }
        }
    }
}