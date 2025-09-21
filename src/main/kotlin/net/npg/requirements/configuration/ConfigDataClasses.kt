package net.npg.requirements.configuration

import kotlinx.serialization.Serializable

@Serializable
data class Config(
    val idFormat: String? = null,
    val referenceTypes: Map<String, String> = emptyMap(),
    val states: Map<String, String> = emptyMap(),
    val versions: Map<String, String> = emptyMap(),
    val priorities: Map<String, String> = emptyMap(),
    val labels: Map<String, String> = emptyMap(),
    val members: Map<String, String> = emptyMap(),
    val types: Map<String, String> = emptyMap(),
)