package io.github.vvmaksim.viewmodel

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.input.TextFieldValue
import io.github.vvmaksim.app.config.PrivateConfig
import io.github.vvmaksim.app.config.TextManager
import io.github.vvmaksim.model.DateManager
import io.github.vvmaksim.model.GameResult
import io.github.vvmaksim.model.MatchData
import io.github.vvmaksim.model.TableManager
import java.nio.file.Path
import kotlin.io.path.Path
import kotlin.io.path.pathString

class MainScreenViewModel {
    val isGenerated = mutableStateOf(false)

    val firstPlayerName = mutableStateOf(TextFieldValue("Игрок 1"))
    val secondPlayerName = mutableStateOf(TextFieldValue("Игрок 2"))
    val reporterName = mutableStateOf(firstPlayerName.value)
    val date = mutableStateOf(TextFieldValue(DateManager.getCurrentDateAsString()))
    val tournamentName = mutableStateOf(TextFieldValue("Шахматный турнир №"))
    val studentGroupNumber = mutableStateOf(TextFieldValue("1000"))
    val studentIDNumber = mutableStateOf(TextFieldValue("100000"))
    val teacherName = mutableStateOf(TextFieldValue("Преподаватель"))
    val firstMatchMoves = mutableStateOf(TextFieldValue(""))
    val firstMatchMovesAsLink = mutableStateOf(true)
    val firstMatchResult = mutableStateOf(GameResult.FIRST_PLAYER_IS_WINNER)
    val secondMatchMoves = mutableStateOf(TextFieldValue(""))
    val secondMatchMovesAsLink = mutableStateOf(true)
    val secondMatchResult = mutableStateOf(GameResult.FIRST_PLAYER_IS_WINNER)
    val path = mutableStateOf(Path(PrivateConfig.getDefaultUserDirPath()))

    private fun getMatchMovesLabel(movesAsLink: Boolean): String {
        return if (movesAsLink) {
            TextManager.UI.MATCH_MOVES_LABEL_WITH_LINK
        } else {
            TextManager.UI.MATCH_MOVES_LABEL_WITH_PGN
        }
    }

    private fun getMatchMovesPlaceholder(movesAsLink: Boolean): String {
        return if (movesAsLink) {
            TextManager.UI.MATCH_MOVES_PLACEHOLDER_WITH_LINK
        } else {
            TextManager.UI.MATCH_MOVES_PLACEHOLDER_WITH_PGN
        }
    }

    private fun getSelectedWinnerText(result: GameResult): String {
        return when (result) {
            GameResult.FIRST_PLAYER_IS_WINNER -> firstPlayerName.value.text
            GameResult.SECOND_PLAYER_IS_WINNER -> secondPlayerName.value.text
            GameResult.DRAW -> TextManager.Words.DRAW
            else -> TextManager.Errors.UNKNOWN_ERROR_MESSAGE
        }
    }

    private fun getGameResultFromString(selectedWinner: String): GameResult {
        return when (selectedWinner) {
            firstPlayerName.value.text -> GameResult.FIRST_PLAYER_IS_WINNER
            secondPlayerName.value.text -> GameResult.SECOND_PLAYER_IS_WINNER
            else -> GameResult.DRAW
        }
    }

    val firstMatchMovesLabel by derivedStateOf {
        getMatchMovesLabel(firstMatchMovesAsLink.value)
    }

    val firstMatchMovesPlaceholder by derivedStateOf {
        getMatchMovesPlaceholder(firstMatchMovesAsLink.value)
    }

    val secondMatchMovesLabel by derivedStateOf {
        getMatchMovesLabel(secondMatchMovesAsLink.value)
    }

    val secondMatchMovesPlaceholder by derivedStateOf {
        getMatchMovesPlaceholder(secondMatchMovesAsLink.value)
    }

    val winnerOptions by derivedStateOf {
        listOf(firstPlayerName.value.text, secondPlayerName.value.text, TextManager.Words.DRAW)
    }

    val firstMatchSelectedWinner by derivedStateOf {
        getSelectedWinnerText(firstMatchResult.value)
    }

    val secondMatchSelectedWinner by derivedStateOf {
        getSelectedWinnerText(secondMatchResult.value)
    }

    fun onFirstMatchWinnerSelected(selectedWinner: String) {
        firstMatchResult.value = getGameResultFromString(selectedWinner)
    }

    fun onSecondMatchWinnerSelected(selectedWinner: String) {
        secondMatchResult.value = getGameResultFromString(selectedWinner)
    }

    fun generateReport(path: Path) {
        val data =
            MatchData(
                firstPlayerName = firstPlayerName.value.text,
                secondPlayerName = secondPlayerName.value.text,
                reporterName = reporterName.value.text,
                date = date.value.text,
                tournamentName = tournamentName.value.text,
                studentGroupNumber = studentGroupNumber.value.text.toIntOrNull() ?: -1,
                studentIDNumber = studentIDNumber.value.text.toIntOrNull() ?: -1,
                teacherName = teacherName.value.text,
                firstMatchMoves = firstMatchMoves.value.text,
                firstMatchMovesAsLink = firstMatchMovesAsLink.value,
                firstMatchResult = firstMatchResult.value,
                secondMatchMoves = secondMatchMoves.value.text,
                secondMatchMovesAsLink = secondMatchMovesAsLink.value,
                secondMatchResult = secondMatchResult.value,
            )
        isGenerated.value = true
        println(data.toString())
        val formattedDate = DateManager.getFormattedDateAsString(data.date)
        val fileName = TextManager.Table.getTableName(data.studentIDNumber, formattedDate)
        TableManager.createTable("${path.pathString}/$fileName", data)
    }
}
