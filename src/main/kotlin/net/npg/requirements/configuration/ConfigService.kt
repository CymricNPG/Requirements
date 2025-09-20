package net.npg.requirements.configuration

import com.charleskorn.kaml.Yaml
import com.intellij.openapi.project.Project
import kotlinx.serialization.decodeFromString
import java.io.File

class ConfigService(private val project: Project) {
    private val yaml = Yaml.default
    private var config: Config? = null

    fun loadConfig(): Config {
        if (config == null) {
            val configFile = File(project.basePath!!, "config.yaml")
            if (configFile.exists()) {
                val content = configFile.readText()
                config = yaml.decodeFromString(content)
            } else {
                // Fallback to default configuration or throw an exception
                config = Config(
                    idFormat = "REQ-{}",
                    types = emptyList(),
                    states = emptyList(),
                    versions = emptyList(),
                    priorities = emptyList(),
                    labels = emptyList(),
                    members = emptyList()
                )
            }
        }
        return config!!
    }

    fun getDomainConfig(domain: String): DomainConfig? {
        val config = loadConfig()
        return config.domainConfigs?.get(domain)
    }
}