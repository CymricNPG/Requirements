package net.npg.requirements

import com.intellij.DynamicBundle
import java.util.*

/**
 * I18nService - Dienst zur Verwaltung von Internationalisierung (I18N)
 *
 * Diese Klasse implementiert das Singleton-Pattern und verwaltet die
 * Internationalisierung der Benutzeroberfläche basierend auf den Konfigurationen.
 *
 * Nutzt moderne Java Locale APIs und IntelliJ Platform Internationalisierung.
 */
class I18nService private constructor() {

    /**
     * IntelliJ Platform Dynamic Bundle für erweiterte Funktionen
     */
    private val dynamicBundle = object : DynamicBundle("messages") {}

    companion object {
        @Volatile
        private var INSTANCE: I18nService? = null

        /**
         * Singleton-Instanz erhalten
         *
         * @return I18nService-Instanz
         */
        fun getInstance(): I18nService {
            return INSTANCE ?: synchronized(this) {
                val instance = INSTANCE ?: I18nService()
                INSTANCE = instance
                instance
            }
        }

        fun reset() {
            INSTANCE = null
        }
    }

    /**
     * Text für einen Schlüssel aus dem Resource-Bundle abrufen
     *
     * @param key Schlüssel für den Text
     * @return Übersetzter Text oder der Schlüssel, wenn kein Eintrag gefunden wurde
     */
    fun getMessage(key: String): String {
        return try {
            // Versuche zunächst IntelliJ Platform Bundle
            dynamicBundle.getMessage(key)
        } catch (e: MissingResourceException) {
            key // Rückgabe des Schlüssels, falls keine Übersetzung vorhanden ist
        }
    }

    /**
     * Text mit Parametern für einen Schlüssel aus dem Resource-Bundle abrufen
     *
     * @param key Schlüssel für den Text
     * @param params Parameter für die Formatierung
     * @return Übersetzter Text oder der Schlüssel, wenn kein Eintrag gefunden wurde
     */
    fun getMessage(key: String, vararg params: Any): String {
        return try {
            // Versuche zunächst IntelliJ Platform Bundle mit Parametern
            dynamicBundle.getMessage(key, *params)
        } catch (e: MissingResourceException) {
            key
        }
    }

}