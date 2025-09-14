package net.npg.requirements.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages

class ShowRequirementsInfoAction : AnAction("Info…") {
    override fun actionPerformed(e: AnActionEvent) {
        val project: Project? = e.project
        Messages.showInfoMessage(
            project,
            "Das Plugin ist installiert und der Menüeintrag ist funktionsfähig.",
            "Anforderungsmanagement"
        )
    }
}