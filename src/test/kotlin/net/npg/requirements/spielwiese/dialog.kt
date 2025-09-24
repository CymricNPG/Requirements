package net.npg.requirements.spielwiese

import com.intellij.testFramework.fixtures.LightPlatformCodeInsightFixture4TestCase
import net.npg.requirements.ui.RequirementDialog
import net.npg.requirements.ui.RequirementDialogKI


class RequirementDialogTest : LightPlatformCodeInsightFixture4TestCase() {
    @org.junit.jupiter.api.Test
    fun testDialog() {
        val dialog = RequirementDialogKI(
            configService = MockConfigurationService(),
            i18nService = MockI18nService(),
            idService = MockIdService()
        )
        val requirement = dialog.showDialog()
        println(requirement)
    }

    @org.junit.jupiter.api.Test
    fun main() {
        val dialog = RequirementDialog()
        dialog.pack()
        dialog.setVisible(true)
        System.exit(0)
    }
}