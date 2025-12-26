package io.github.vvmaksim.viewmodel

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.input.TextFieldValue
import io.github.vvmaksim.app.config.PrivateConfig
import io.github.vvmaksim.app.config.TextManager
import io.github.vvmaksim.model.ChessManager
import io.github.vvmaksim.model.DateManager
import io.github.vvmaksim.model.GameData
import io.github.vvmaksim.model.GameResult
import io.github.vvmaksim.model.MatchData
import io.github.vvmaksim.model.PgnManager
import io.github.vvmaksim.model.TableManager
import io.github.vvmaksim.model.UserSettings
import io.github.vvmaksim.model.UserSettingsManager
import java.nio.file.Path
import kotlin.io.path.Path
import kotlin.io.path.pathString

class MainScreenViewModel {
    val isGenerated = mutableStateOf(false)
    val errorState = mutableStateOf<String?>(null)

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

    init {
        loadInitialSettings()
    }

    private fun loadInitialSettings() {
        UserSettingsManager.loadSettings()?.let { settings ->
            firstPlayerName.value = TextFieldValue(settings.firstPlayerName)
            secondPlayerName.value = TextFieldValue(settings.secondPlayerName)
            tournamentName.value = TextFieldValue(settings.tournamentName)
            studentGroupNumber.value = TextFieldValue(settings.studentGroupNumber)
            studentIDNumber.value = TextFieldValue(settings.studentIDNumber)
            teacherName.value = TextFieldValue(settings.teacherName)
            reporterName.value = firstPlayerName.value
        }
    }

    private fun saveCurrentSettings() {
        val settings =
            UserSettings(
                firstPlayerName = firstPlayerName.value.text,
                secondPlayerName = secondPlayerName.value.text,
                tournamentName = tournamentName.value.text,
                studentGroupNumber = studentGroupNumber.value.text,
                studentIDNumber = studentIDNumber.value.text,
                teacherName = teacherName.value.text,
            )
        UserSettingsManager.saveSettings(settings)
    }

    fun onFirstMatchMovesChange(newValue: TextFieldValue) {
        firstMatchMoves.value = newValue
        errorState.value = null
    }

    fun onSecondMatchMovesChange(newValue: TextFieldValue) {
        secondMatchMoves.value = newValue
        errorState.value = null
    }

    private fun getMatchMovesLabel(movesAsLink: Boolean): String =
        if (movesAsLink) {
            TextManager.UI.MATCH_MOVES_LABEL_WITH_LINK
        } else {
            TextManager.UI.MATCH_MOVES_LABEL_WITH_PGN
        }

    private fun getMatchMovesPlaceholder(movesAsLink: Boolean): String =
        if (movesAsLink) {
            TextManager.UI.MATCH_MOVES_PLACEHOLDER_WITH_LINK
        } else {
            TextManager.UI.MATCH_MOVES_PLACEHOLDER_WITH_PGN
        }

    private fun getSelectedWinnerText(result: GameResult): String =
        when (result) {
            GameResult.FIRST_PLAYER_IS_WINNER -> firstPlayerName.value.text
            GameResult.SECOND_PLAYER_IS_WINNER -> secondPlayerName.value.text
            GameResult.DRAW -> TextManager.Words.DRAW
            else -> TextManager.Errors.UNKNOWN_ERROR_MESSAGE
        }

    private fun getGameResultFromString(selectedWinner: String): GameResult =
        when (selectedWinner) {
            firstPlayerName.value.text -> GameResult.FIRST_PLAYER_IS_WINNER
            secondPlayerName.value.text -> GameResult.SECOND_PLAYER_IS_WINNER
            else -> GameResult.DRAW
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

    private suspend fun validateAndParseGame(
        moves: String,
        isLink: Boolean,
        gameNumber: Int,
    ): Result<GameData> {
        val pgnManager = PgnManager()
        try {
            val gameDataResult =
                if (isLink) {
                    pgnManager.getPgnFromUrl(moves)
                } else {
                    Result.success(GameData(moves, firstMatchResult.value))
                }

            return gameDataResult.fold(
                onSuccess = { gameData ->
                    try {
                        ChessManager.getMoves(gameData.moves)
                        Result.success(gameData)
                    } catch (e: Exception) {
                        Result.failure(
                            IllegalArgumentException(TextManager.Errors.getIncorrectNotationInGameErrorMessage(gameNumber, e.message)),
                        )
                    }
                },
                onFailure = {
                    Result.failure(IllegalArgumentException(TextManager.Errors.getErrorInGameErrorMessage(gameNumber, it.message)))
                },
            )
        } finally {
            pgnManager.close()
        }
    }

    suspend fun generateReport(path: Path) {
        val firstGameData =
            validateAndParseGame(firstMatchMoves.value.text, firstMatchMovesAsLink.value, 1)
                .getOrElse {
                    errorState.value = it.message
                    return
                }

        val secondGameData =
            validateAndParseGame(secondMatchMoves.value.text, secondMatchMovesAsLink.value, 2)
                .getOrElse {
                    errorState.value = it.message
                    return
                }

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
                firstMatchMoves = firstGameData.moves,
                firstMatchMovesAsLink = false, // Data is now processed
                firstMatchResult = if (firstMatchMovesAsLink.value) firstGameData.result else firstMatchResult.value,
                secondMatchMoves = secondGameData.moves,
                secondMatchMovesAsLink = false, // Data is now processed
                secondMatchResult = if (secondMatchMovesAsLink.value) secondGameData.result else secondMatchResult.value,
            )

        saveCurrentSettings()

        isGenerated.value = true
        val formattedDate = DateManager.getFormattedDateAsString(data.date)
        val fileName = TextManager.Table.getTableName(data.studentIDNumber, formattedDate)
        TableManager.createTable("${path.pathString}/$fileName", data)
    }
}
