package io.github.vvmaksim.model

data class MatchData(
    val firstPlayerName: String,
    val secondPlayerName: String,
    val reporterName: String,
    val date: String,
    val tournamentName: String,
    val studentGroupNumber: Int,
    val studentIDNumber: Int,
    val teacherName: String,
    val firstMatchMoves: String,
    val firstMatchResult: GameResult,
    val secondMatchMoves: String,
    val secondMatchResult: GameResult,
)
