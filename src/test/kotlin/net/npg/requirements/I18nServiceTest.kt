package net.npg.requirements

import com.intellij.DynamicBundle
import org.junit.jupiter.api.BeforeEach
import java.util.*
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertSame

/**
 * Tests für die I18nService-Klasse
 *
 * Diese Tests prüfen die Funktionalität des I18N-Services.
 */
class I18nServiceTest {

    @BeforeEach
    fun setUp() {
        // Zurücksetzen der Singleton-Instanz für Testzwecke
        I18nService.reset()
        // Clear ResourceBundle caches to ensure clean state
        DynamicBundle.clearCache()

    }

    /**
     * Testet das Singleton-Pattern der I18nService-Klasse
     */
    @Test
    fun `test singleton pattern`() {
        val service1 = I18nService.getInstance()
        val service2 = I18nService.getInstance()

        assertSame(service1, service2)
    }
    
    /**
     * Testet das Abrufen von Nachrichten für englische Sprache
     */
    @Test
    fun `test get message english`() {
        Locale.setDefault(Locale.ENGLISH)
        val i18nService = I18nService.getInstance()

        // Test mit existierendem Schlüssel
        val message = i18nService.getMessage("requirement.create.title")
        assertEquals("Create New Requirement", message)

        // Test mit nicht existierendem Schlüssel
        val nonExistent = i18nService.getMessage("non.existent.key")
        assertContains(nonExistent, "non.existent.key")
    }

    /**
     * Testet das Abrufen von Nachrichten für deutsche Sprache
     */
    @Test
    fun `test get message german`() {
        Locale.setDefault(Locale.GERMAN)
        val i18nService = I18nService.getInstance()

        // Test mit existierendem Schlüssel
        val message = i18nService.getMessage("requirement.create.title")
        assertEquals("Neue Anforderung erstellen", message)

        // Test mit nicht existierendem Schlüssel
        val nonExistent = i18nService.getMessage("non.existent.key")
        assertContains(nonExistent, "non.existent.key")
    }

    /**
     * Testet das Abrufen von Nachrichten mit Parametern
     */
    @Test
    fun `test get message with parameters`() {
        Locale.setDefault(Locale.ENGLISH)
        val i18nService = I18nService.getInstance()

        // Test mit Parametern
        val message = i18nService.getMessage("requirement.create.title", "Test")
        assertEquals("Create New Requirement", message)

    }

    /**
     * Testet das Abrufen von Nachrichten mit Parametern
     */
    @Test
    fun `test get message with parameters in german`() {
        Locale.setDefault(Locale.GERMAN)
        val i18nService = I18nService.getInstance()

        // Test mit Parametern
        val germanMessage = i18nService.getMessage("requirement.create.title", "Test")
        assertEquals("Neue Anforderung erstellen", germanMessage)
    }

}