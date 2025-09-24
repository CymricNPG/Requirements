package net.npg.requirements.i18n

import com.intellij.DynamicBundle
import java.util.*

class ClassPathI18nService private constructor() : I18nService {

    /**
     * IntelliJ Platform Dynamic Bundle für erweiterte Funktionen
     */
    private val dynamicBundle = object : DynamicBundle("messages") {}

    companion object {
        @Volatile
        private var INSTANCE: ClassPathI18nService? = null

        /**
         * Singleton-Instanz erhalten
         *
         * @return I18nService-Instanz
         */
        fun getInstance(): ClassPathI18nService {
            return INSTANCE ?: synchronized(this) {
                val instance = INSTANCE ?: ClassPathI18nService()
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
    override fun getString(key: String): String {
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
    override fun getString(key: String, vararg params: Any): String {
        return try {
            // Versuche zunächst IntelliJ Platform Bundle mit Parametern
            dynamicBundle.getMessage(key, *params)
        } catch (e: MissingResourceException) {
            key
        }
    }

}