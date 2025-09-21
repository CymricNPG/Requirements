package net.npg.requirements.configuration

import com.charleskorn.kaml.Yaml
import kotlinx.serialization.decodeFromString
import java.nio.file.Files
import java.nio.file.Path

/**
 * Implementierung des [ConfigParser] für YAML-Dateien mit Kaml.
 */
class YamlConfigParser : ConfigParser<Config> {
    private val yaml = Yaml.default

    override fun parse(file: Path): Config {
        require(Files.exists(file)) { "Datei existiert nicht: $file" }
        val content = Files.readString(file)
        return yaml.decodeFromString<Config>(content)
    }
}