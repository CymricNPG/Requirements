package net.npg.requirements.ui

interface IdService {
    fun generateId(path: String): String
}