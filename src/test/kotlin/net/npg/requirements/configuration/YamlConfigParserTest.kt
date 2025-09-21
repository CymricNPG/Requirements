package net.npg.requirements.configuration


import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.nio.file.Files
import java.nio.file.Path
import kotlin.test.assertEquals

class YamlConfigParserTest {

    private val yamlConfigParser = YamlConfigParser()

    /**
     * Test parsing a valid YAML configuration file
     */
    @Test
    fun `test parse valid yaml`() {
        val testFile = Path.of("src/test/resources/config.yml").toAbsolutePath()
        val config = yamlConfigParser.parse(testFile)
        assertEquals("STEP-{}", config.idFormat)
    }

    /**
     * Test parsing a non-existent YAML configuration file
     */
    @Test
    fun `test parse non-existent file`(@TempDir tempDir: Path) {
        val nonExistentFile = tempDir.resolve("non-existent.yaml")
        Assertions.assertThrows(IllegalArgumentException::class.java) {
            yamlConfigParser.parse(nonExistentFile)
        }
    }

    /**
     * Test parsing an invalid YAML configuration file
     */
    @Test
    fun `test parse invalid yaml`(@TempDir tempDir: Path) {
        // Write invalid YAML content to the temporary file
        val invalidContent = """
            |name: Test Config
            |version: 1.0.0
            |enabled: true
            ||| This is invalid YAML syntax
        """.trimMargin()

        val tempFile = tempDir.resolve("wrong.yaml")
        Files.writeString(tempFile, invalidContent)

        Assertions.assertThrows(Exception::class.java) {
            yamlConfigParser.parse(tempFile)
        }
    }
}