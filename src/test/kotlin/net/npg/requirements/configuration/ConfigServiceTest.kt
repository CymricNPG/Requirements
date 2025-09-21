package net.npg.requirements.configuration

import com.intellij.openapi.Disposable
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Disposer
import com.intellij.openapi.vfs.AsyncFileListener
import com.intellij.openapi.vfs.VirtualFileManager
import com.intellij.openapi.vfs.newvfs.events.VFileEvent
import io.mockk.*
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.io.TempDir
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

@ExtendWith(MockKExtension::class)
class ConfigServiceTest {

    @MockK
    private lateinit var mockProject: Project

    @MockK
    private lateinit var mockDisposable: Disposable

    @MockK
    private lateinit var mockConfigParser: ConfigParser<Config>

    @MockK
    private lateinit var mockVirtualFileManager: VirtualFileManager

    private lateinit var configService: ConfigService

    @BeforeEach
    fun setUp() {
        // Mock the VirtualFileManager instance
        mockkStatic(VirtualFileManager::class)
        every { VirtualFileManager.getInstance() } returns mockVirtualFileManager

        every {
            mockVirtualFileManager.addAsyncFileListener(
                ofType(AsyncFileListener::class),
                ofType(Disposable::class)
            )
        }.returns(Unit)
        // Mock Disposer.dispose
        mockkStatic(Disposer::class)
        every { Disposer.dispose(any()) } just Runs

        // Instantiate ConfigService with mocks
        configService = ConfigService(mockProject).apply {
            // Replace the parser with a mock
            val field = this::class.java.getDeclaredField("parser")
            field.isAccessible = true
            field.set(this, mockConfigParser)
        }
    }

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `reloadConfigForDirectory - Datei existiert - lädt und cached die Konfiguration`(@TempDir testPath: Path) {
        // Vorbereitung
        val configFile = testPath.resolve("config.yaml")
        Files.writeString(configFile, "bla");
        val mockConfig = mockk<Config>()

        every { mockConfigParser.parse(configFile) } returns mockConfig

        // Ausführung
        configService.reloadConfigForDirectory(testPath)

        // Überprüfung
        verify { mockConfigParser.parse(configFile) }
        Assertions.assertTrue(configService.configCache.containsKey(testPath.toAbsolutePath()))
    }

    @Test
    fun `reloadConfigForDirectory - Datei existiert nicht - entfernt Cache-Eintrag`() {
        // Vorbereitung
        val testPath = Paths.get("/test/dir")
        val configFile = testPath.resolve("config.yaml")

        // Ausführung
        configService.reloadConfigForDirectory(testPath)

        // Überprüfung
        Assertions.assertFalse(configService.configCache.containsKey(testPath.toAbsolutePath()))
    }

    @Test
    fun `dispose - Disposable vorhanden - wird disposed`() {
        // Ausführung
        configService.dispose()

        // Überprüfung
        verify { Disposer.dispose(any()) }
    }

    @Test
    fun `fileListener - config yaml geändert - lädt Konfiguration neu`(@TempDir testPath: Path) {
        // Vorbereitung
        val configFile = testPath.resolve("config.yaml")
        Files.writeString(configFile, "bla");
        val mockEvent = mockk<VFileEvent>()
        every { mockEvent.isFromSave } returns true
        every { mockEvent.file?.name } returns "config.yaml"
        every { mockEvent.file?.toNioPath() } returns testPath.resolve("config.yaml")

        // Ausführung
        configService.fileListener.prepareChange(listOf(mockEvent))?.afterVfsChange()

        // Überprüfung
        verify { mockConfigParser.parse(any()) }
    }

    @Test
    fun `fileListener - andere Datei geändert - ignoriert`() {
        // Vorbereitung
        val mockEvent = mockk<VFileEvent>()
        every { mockEvent.isFromSave } returns true
        every { mockEvent.file?.name } returns "other.txt"

        // Ausführung
        configService.fileListener.prepareChange(listOf(mockEvent))?.afterVfsChange()

        // Überprüfung: Kein Aufruf von parse
        verify(exactly = 0) { mockConfigParser.parse(any()) }
    }
}