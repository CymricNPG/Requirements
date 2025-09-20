package net.npg.requirements.configuration

import kotlinx.serialization.Serializable

@Serializable
data class DomainConfig(
    val idPrefix: String,
    val labels: List<String>
)


@Serializable
data class Config(
    val idFormat: String,
    val types: List<ReferenceType>,
    val states: List<State>,
    val versions: List<Version>,
    val priorities: List<Priority>,
    val labels: List<Label>,
    val members: List<Member>,
    val domainConfigs: Map<String, DomainConfig>? = null

)

@Serializable
data class ReferenceType(
    val id: String,
    val shortText: String,
    val description: String
)

@Serializable
data class State(
    val id: String,
    val name: String
)

@Serializable
data class Version(
    val id: String,
    val name: String
)

@Serializable
data class Priority(
    val id: String,
    val name: String
)

@Serializable
data class Label(
    val id: String,
    val name: String
)

@Serializable
data class Member(
    val id: String,
    val name: String
)