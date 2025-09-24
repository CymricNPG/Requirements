package net.npg.requirements.spielwiese

import net.npg.requirements.configuration.Configuration
import net.npg.requirements.configuration.ConfigurationService
import net.npg.requirements.i18n.I18nService
import net.npg.requirements.ui.IdService
import java.nio.file.Path

class MockConfigurationService : ConfigurationService {
    override fun getConfiguration(path: Path) = Configuration(
        idFormat = "REQ-{0}",
        types = mapOf("feature" to "Feature", "bug" to "Bug"),
        states = mapOf("draft" to "Draft", "review" to "Review"),
        priorities = mapOf("high" to "High", "medium" to "Medium", "low" to "Low"),
        labels = mapOf("ui" to "UI", "backend" to "Backend"),
        members = mapOf("roland" to "Roland Spatzenegger"),
        referenceTypes = emptyMap()
    )
}

class MockI18nService : I18nService {
    override fun getString(key: String) = when (key) {
        "ok" -> "OK"
        "cancel" -> "Cancel"
        "id" -> "ID:"
        "summary" -> "Summary:"
        "type" -> "Type:"
        "status" -> "Status:"
        "priority" -> "Priority:"
        "responsible" -> "Responsible:"
        "labels" -> "Labels:"
        "created" -> "Created:"
        "requirement.dialog.title" -> "New Requirement"
        else -> key
    }

    override fun getString(key: String, vararg params: Any): String {
        TODO("Not yet implemented")
    }
}

class MockIdService : IdService {
    override fun generateId(path: String) = "REQ-123"
}