package net.npg.requirements.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages
import net.npg.requirements.i18n.ClassPathI18nService

class ShowRequirementsInfoAction : AnAction("Info…") {
    override fun actionPerformed(e: AnActionEvent) {
        val project: Project? = e.project

        val i18n = ClassPathI18nService.getInstance()
        val title = i18n.getString("requirement.create.title")


        Messages.showInfoMessage(
            project,
            "Das Plugin ist installiert und der Menüeintrag ist funktionsfähig.",
            title
        )
    }
}