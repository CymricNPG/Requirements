package net.npg.requirements.configuration

import com.intellij.openapi.Disposable
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Disposer
import com.intellij.openapi.vfs.AsyncFileListener
import com.intellij.openapi.vfs.VirtualFileManager
import io.mockk.Runs
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.just
import io.mockk.mockkStatic
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNull
import org.junit.jupiter.api.extension.ExtendWith
import java.nio.file.Path
import kotlin.test.assertEquals

@ExtendWith(MockKExtension::class)
class ConfigReadTest {
    @MockK
    private lateinit var mockProject: Project

    @MockK
    private lateinit var mockVirtualFileManager: VirtualFileManager

    private lateinit var configurationService: IntelliJConfigurationService

    @BeforeEach
    fun setUp() {
        // Mock the VirtualFileManager instance
        mockkStatic(VirtualFileManager::class)
        every { VirtualFileManager.getInstance() } returns mockVirtualFileManager
        every { mockProject.projectFilePath } returns "src/test/resources"
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
        configurationService = IntelliJConfigurationService(mockProject)
    }

    @Test
    fun `getConfiguration - read config`() {
        val path = Path.of("src/test/resources/test/")
        val root = configurationService.getConfiguration(path);
        assertEquals("TEST-{}", root!!.idFormat)
        assertEquals(0, root.states.size)
    }

    @Test
    fun `getConfiguration - read none`() {
        val path = Path.of("src/test/")
        val root = configurationService.getConfiguration(path);
        assertNull(root)
    }

    @Test
    fun `getConfiguration - read domain`() {
        val path = Path.of("src/test/resources/test/domain")
        val root = configurationService.getConfiguration(path);
        assertEquals("REQ-QUA-{}", root!!.idFormat)
    }
}