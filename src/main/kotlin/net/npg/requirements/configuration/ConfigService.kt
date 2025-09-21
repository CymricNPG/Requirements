package net.npg.requirements.configuration

import com.intellij.openapi.Disposable
import com.intellij.openapi.components.Service
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Disposer
import com.intellij.openapi.vfs.AsyncFileListener
import com.intellij.openapi.vfs.VirtualFileManager
import java.nio.file.Files
import java.nio.file.Path

/**
 * Service zum Verwalten der Konfiguration im IntelliJ-Plugin.
 * Reagiert auf Dateiänderungen und lädt die Konfiguration hierarchisch.
 */
@Service(Service.Level.PROJECT)
class ConfigService(private val project: Project) : Disposable {
    private val parser: ConfigParser<Config> = YamlConfigParser()
    val configCache: MutableMap<Path, Config> = mutableMapOf()

    val fileListener: AsyncFileListener = AsyncFileListener { events ->
        object : AsyncFileListener.ChangeApplier {
            override fun afterVfsChange() {
                val workedDirectory = mutableSetOf<Path>()
                // Reload the configuration for the directory of the changed file
                events
                    .filter { it.isFromSave && it.file?.name == "config.yaml" }
                    .mapNotNull { it.file?.toNioPath()?.parent }
                    .filter { workedDirectory.add(it) }
                    .forEach { reloadConfigForDirectory(it) }
            }
        }
    }

    init {
        VirtualFileManager.getInstance().addAsyncFileListener(fileListener, this)
    }

    override fun dispose() {
        // Unregister the listener when the service is disposed
        Disposer.dispose(this)
    }

    // TODO
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
        val absolutePath = directoryPath.toAbsolutePath()
        configCache.remove(absolutePath)

        val filePath = absolutePath.resolve("config.yaml")
        if (Files.exists(filePath)) {
            try {
                val parsedConfig = parser.parse(filePath)
                configCache[absolutePath] = parsedConfig;
            } catch (e: Exception) {
                LOG.warn("Cannot parse file: " + absolutePath + " " + e.message)
            }
        }
    }

    companion object {
        private val LOG = Logger.getInstance(ConfigService::class.java)
    }
}