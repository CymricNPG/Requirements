package net.npg.requirements.i18n

/**
 * I18nService - Dienst zur Verwaltung von Internationalisierung (I18N)
 *
 * Diese Klasse implementiert das Singleton-Pattern und verwaltet die
 * Internationalisierung der Benutzeroberfläche basierend auf den Konfigurationen.
 *
 * Nutzt moderne Java Locale APIs und IntelliJ Platform Internationalisierung.
 */
interface I18nService {
    fun getString(key: String): String
    fun getString(key: String, vararg params: Any): String
}