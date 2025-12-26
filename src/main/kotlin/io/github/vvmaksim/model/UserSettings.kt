package io.github.vvmaksim.model

import kotlinx.serialization.Serializable

@Serializable
data class UserSettings(
    val firstPlayerName: String,
    val secondPlayerName: String,
    val tournamentName: String,
    val studentGroupNumber: String,
    val studentIDNumber: String,
    val teacherName: String,
)
