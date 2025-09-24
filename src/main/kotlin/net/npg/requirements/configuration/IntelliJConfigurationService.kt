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
interface ConfigurationService {
    fun getConfiguration(path: Path): Configuration?
}

@Service(Service.Level.PROJECT)
class IntelliJConfigurationService(private val project: Project) : Disposable, ConfigurationService {
    private val parser: ConfigParser<Configuration> = YamlConfigParser()
    val configCache: MutableMap<Path, Configuration> = mutableMapOf()

    val fileListener: AsyncFileListener = AsyncFileListener { events ->
        object : AsyncFileListener.ChangeApplier {
            override fun afterVfsChange() {
                val workedDirectory = mutableSetOf<Path>()
                // Reload the configuration for the directory of the changed file
                events
                    .filter { it.isFromSave && it.file?.name == CONFIG_FILE }
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

    /**
     * Erzwingt das Neuladen der Konfiguration für ein Verzeichnis.
     * @param directoryPath Pfad zum Verzeichnis.
     */
    fun reloadConfigForDirectory(directoryPath: Path): Configuration? {
        val absolutePath = directoryPath.toAbsolutePath()
        configCache.remove(absolutePath)

        val filePath = absolutePath.resolve(CONFIG_FILE)
        if (Files.exists(filePath)) {
            try {
                val parsedConfig = parser.parse(filePath)
                configCache[absolutePath] = parsedConfig;
                return parsedConfig
            } catch (e: Exception) {
                LOG.warn("Cannot parse file: " + absolutePath + " " + e.message)
            }
        }
        return null
    }

    override fun getConfiguration(path: Path): Configuration? {
        val configs = collectConfigurations(path)
        if (configs.isEmpty()) {
            return null
        }

        val result = configs
            .stream()
            .reduce { s, t -> s.replace(t) }
            .orElse(null)

        return result
    }

    private fun collectConfigurations(path: Path): List<Configuration> {
        val configs = mutableListOf<Configuration>()
        var workPath: Path? = path

        while (workPath != null) {
            val projectPath = Path.of(project.projectFilePath!!).toAbsolutePath()

            val searchPath = workPath.toAbsolutePath()
            if (projectPath.startsWith(searchPath)) {
                break
            }
            if (configCache.containsKey(searchPath)) {
                configs.add(configCache[searchPath]!!)
            } else {
                val possibleConfig = reloadConfigForDirectory(searchPath)
                if (possibleConfig != null) {
                    configs.add(possibleConfig)
                }
            }
            workPath = workPath.parent
        }
        return configs
    }

    companion object {
        private val LOG = Logger.getInstance(IntelliJConfigurationService::class.java)
        const val CONFIG_FILE = "config.yml"
    }
}

private fun Configuration.replace(t: Configuration): Configuration {
    return Configuration(
        idFormat = if (t.idFormat != null) idFormat else this.idFormat,
        referenceTypes = t.referenceTypes.ifEmpty { this.referenceTypes },
        states = t.states.ifEmpty { this.states },
        versions = t.versions.ifEmpty { this.versions },
        priorities = t.priorities.ifEmpty { this.priorities },
        labels = t.labels.ifEmpty { this.labels },
        members = t.members.ifEmpty { this.members },
        types = t.types.ifEmpty { this.types },
    )
}