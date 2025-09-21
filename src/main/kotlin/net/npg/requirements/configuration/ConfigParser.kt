package net.npg.requirements.configuration

import java.nio.file.Path

/**
 * Interface für das Parsen von Konfigurationsdateien.
 */
interface ConfigParser<T> {
    /**
     * Parsed die Konfiguration aus der gegebenen Datei.
     * @param file Pfad zur Konfigurationsdatei.
     * @return  geparsten Konfiguration.
     */
    fun parse(file: Path): T
}