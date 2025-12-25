package io.github.vvmaksim.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.input.TextFieldValue
import io.github.vvmaksim.app.config.PrivateConfig
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
    val tournamentName = mutableStateOf(TextFieldValue("Шахматный турнир N"))
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
        val fileName = "Отчёт.ТК.ФВиС.${data.studentIDNumber}.$formattedDate.xlsx"
        TableManager.createTable("${path.pathString}/$fileName", data)
    }
}
