package net.npg.requirements.configuration

import com.charleskorn.kaml.Yaml
import kotlinx.serialization.decodeFromString
import java.nio.file.Files
import java.nio.file.Path

/**
 * Implementierung des [ConfigParser] für YAML-Dateien mit Kaml.
 */
class YamlConfigParser : ConfigParser<Configuration> {
    private val yaml = Yaml.default

    override fun parse(file: Path): Configuration {
        require(Files.exists(file)) { "Datei existiert nicht: $file" }
        val content = Files.readString(file)
        return yaml.decodeFromString<Configuration>(content)
    }
}