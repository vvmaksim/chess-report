package io.github.vvmaksim.model

import io.github.vvmaksim.app.Logger
import kotlinx.serialization.json.Json
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.Path
import kotlin.io.path.exists
import kotlin.io.path.readText
import kotlin.io.path.writeText

object UserSettingsManager {
    private val configDir: Path = Path(System.getProperty("user.home"), ".chess-report", "config")
    private val configFile: Path = configDir.resolve("settings.json")
    private val json = Json { prettyPrint = true }

    init {
        try {
            if (!Files.exists(configDir)) {
                Files.createDirectories(configDir)
            }
        } catch (e: Exception) {
            Logger.error("Failed to create config directory: $configDir", e)
        }
    }

    fun saveSettings(settings: UserSettings) {
        try {
            val jsonString = json.encodeToString(settings)
            configFile.writeText(jsonString)
        } catch (e: Exception) {
            Logger.error("Failed to save settings to file: $configFile", e)
        }
    }

    fun loadSettings(): UserSettings? =
        try {
            if (configFile.exists()) {
                val jsonString = configFile.readText()
                json.decodeFromString<UserSettings>(jsonString)
            } else {
                null
            }
        } catch (e: Exception) {
            Logger.error("Failed to load settings from file: $configFile", e)
            null
        }
}
