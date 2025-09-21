package net.npg.requirements.configuration

import com.intellij.openapi.components.Service
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.newvfs.BulkFileListener
import com.intellij.openapi.vfs.newvfs.events.VFileEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.nio.file.Files
import java.nio.file.Path

/**
 * Service zum Verwalten der Konfiguration im IntelliJ-Plugin.
 * Reagiert auf Dateiänderungen und lädt die Konfiguration hierarchisch.
 */
@Service(Service.Level.PROJECT)
class ConfigService(private val project: Project) {
    private val parser: ConfigParser<Config> = YamlConfigParser()
    private val configCache: MutableMap<Path, Map<String, Any>> = mutableMapOf()
    private val fileListener = object : BulkFileListener {
        override fun after(events: List<VFileEvent>) {
            events.forEach { event ->
                if (event.isFromSave && event.file?.name == "config.yaml") {
                    CoroutineScope(Dispatchers.IO).launch {
                        //         loadConfigForDirectory(event.file?.toNioPath()?.parent ?: return@launch)
                    }
                }
            }
        }
    }

    init {
        //    VirtualFileManager.getInstance().addAsyncFileListener(fileListener)
    }

    /**
     * Lädt die Konfiguration für das gegebene Verzeichnis und alle Elternverzeichnisse.
     * @param directoryPath Pfad zum Verzeichnis.
     * @return Vollständige Konfiguration als Map.
     */
    fun getConfigForDirectory(directoryPath: Path): Map<String, Any> {
        return configCache.getOrPut(directoryPath) {
            buildHierarchicalConfig(directoryPath)
        }
    }

    private fun buildHierarchicalConfig(directoryPath: Path): Map<String, Any> {
        val config = mutableMapOf<String, Any>()
        var currentPath: Path? = directoryPath
        while (currentPath != null) {
            val configFile = currentPath.resolve("config.yaml")
            if (Files.exists(configFile)) {
                val parsedConfig = parser.parse(configFile)
                //       config.putAll(parsedConfig)
            }
            currentPath = currentPath.parent
        }
        return config
    }

    /**
     * Erzwingt das Neuladen der Konfiguration für ein Verzeichnis.
     * @param directoryPath Pfad zum Verzeichnis.
     */
    fun reloadConfigForDirectory(directoryPath: Path) {
        configCache.remove(directoryPath)
        getConfigForDirectory(directoryPath)
    }
}