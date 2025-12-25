package io.github.vvmaksim.view

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import io.github.vvmaksim.app.Logger
import io.github.vvmaksim.app.theme.AppTheme
import io.github.vvmaksim.model.GameResult
import io.github.vvmaksim.viewmodel.MainScreenViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.awt.Desktop
import java.io.File
import kotlin.io.path.Path
import kotlin.io.path.pathString

@Suppress("ktlint:standard:function-naming")
@Composable
fun DataBlock(
    viewModel: MainScreenViewModel,
    modifier: Modifier = Modifier.padding(16.dp),
) {
    val scrollState = rememberScrollState()
    Box(modifier = modifier.width(480.dp)) {
        Column(
            modifier =
                modifier
                    .verticalScroll(scrollState)
                    .padding(end = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            val openDirectoryError = remember { mutableStateOf("") }
            val isFocusedFirstMatchMovesField = remember { mutableStateOf(false) }
            val isFocusedSecondMatchMovesField = remember { mutableStateOf(false) }
            val scope = rememberCoroutineScope()

            DataBlockTitleText(text = "Заполните данные для отчёта:")
            Spacer(Modifier.padding(8.dp))
            CustomTextField(
                value = viewModel.firstPlayerName.value,
                onValueChange = { newValue: TextFieldValue ->
                    viewModel.firstPlayerName.value = newValue
                    viewModel.reporterName.value = newValue
                },
                label = { Text("Игрок 1") },
                placeholder = { Text("ФИО") },
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(Modifier.padding(8.dp))
            CustomTextField(
                value = viewModel.secondPlayerName.value,
                onValueChange = { newValue: TextFieldValue ->
                    viewModel.secondPlayerName.value = newValue
                },
                label = { Text("Игрок 2") },
                placeholder = { Text("ФИО") },
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(Modifier.padding(8.dp))
            CustomTextField(
                value = viewModel.reporterName.value,
                onValueChange = { newValue: TextFieldValue ->
                    viewModel.reporterName.value = newValue
                },
                label = { Text("Репортёр") },
                placeholder = { Text("ФИО") },
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(Modifier.padding(8.dp))
            CustomTextField(
                value = viewModel.teacherName.value,
                onValueChange = { newValue: TextFieldValue ->
                    viewModel.teacherName.value = newValue
                },
                label = { Text("Преподаватель") },
                placeholder = { Text("ФИО") },
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(Modifier.padding(8.dp))
            CustomTextField(
                value = viewModel.tournamentName.value,
                onValueChange = { newValue: TextFieldValue ->
                    viewModel.tournamentName.value = newValue
                },
                label = { Text("Название турнира") },
                placeholder = { Text("Шахматный турнир №1") },
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(Modifier.padding(8.dp))
            CustomTextField(
                value = viewModel.date.value,
                onValueChange = { newValue: TextFieldValue ->
                    viewModel.date.value = newValue
                },
                label = { Text("Дата партии") },
                placeholder = { Text("ДД.ММ.ГГГГ") },
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(Modifier.padding(8.dp))
            CustomTextField(
                value = viewModel.studentIDNumber.value,
                onValueChange = { newValue: TextFieldValue ->
                    viewModel.studentIDNumber.value = newValue
                },
                label = { Text("Номер студенческого") },
                placeholder = { Text("000000") },
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(Modifier.padding(8.dp))
            CustomTextField(
                value = viewModel.studentGroupNumber.value,
                onValueChange = { newValue: TextFieldValue ->
                    viewModel.studentGroupNumber.value = newValue
                },
                label = { Text("Номер группы") },
                placeholder = { Text("0000") },
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(Modifier.padding(8.dp))
            DataBlockTitleText("Партия 1:")

            CustomCheckboxRow(
                text = "Записать ходы партии по ссылки",
                checked = viewModel.firstMatchMovesAsLink.value,
                onCheckedChange = { viewModel.firstMatchMovesAsLink.value = it },
            )

            Spacer(Modifier.padding(8.dp))
            CustomTextField(
                value = viewModel.firstMatchMoves.value,
                onValueChange = { newValue: TextFieldValue ->
                    viewModel.firstMatchMoves.value = newValue
                },
                label = {
                    Text(
                        text =
                            if (viewModel.firstMatchMovesAsLink.value) {
                                "Вставьте ссылку на анализ партии"
                            } else {
                                "Ходы в формате PGN"
                            },
                    )
                },
                placeholder = {
                    Text(
                        text = if (viewModel.firstMatchMovesAsLink.value) "https://lichess.org/" else "1.e4 e5 2.Nf3 Nc6 ...",
                    )
                },
                singleLine = false,
                maxLines = if (isFocusedFirstMatchMovesField.value) 10 else 2,
                minLines = if (isFocusedFirstMatchMovesField.value) 4 else 1,
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .onFocusChanged { isFocusedFirstMatchMovesField.value = it.isFocused },
            )
            Spacer(Modifier.padding(8.dp))
            if (!viewModel.firstMatchMovesAsLink.value) {
                DataBlockTitleText("Победитель")
                Spacer(Modifier.padding(8.dp))
                DropdownSelectButton(
                    items = listOf(viewModel.firstPlayerName.value.text, viewModel.secondPlayerName.value.text, "Ничья"),
                    selectedItem =
                        when (viewModel.firstMatchResult.value) {
                            GameResult.FIRST_PLAYER_IS_WINNER -> viewModel.firstPlayerName.value.text
                            GameResult.SECOND_PLAYER_IS_WINNER -> viewModel.secondPlayerName.value.text
                            GameResult.DRAW -> "Ничья"
                            else -> "Что-то пошло не так=("
                        },
                    onItemSelected = { selectedResult: String ->
                        when (selectedResult) {
                            viewModel.firstPlayerName.value.text -> viewModel.firstMatchResult.value = GameResult.FIRST_PLAYER_IS_WINNER
                            viewModel.secondPlayerName.value.text -> viewModel.firstMatchResult.value = GameResult.SECOND_PLAYER_IS_WINNER
                            else -> viewModel.firstMatchResult.value = GameResult.DRAW
                        }
                    },
                )
                Spacer(Modifier.padding(8.dp))
            }
            DataBlockTitleText("Партия 2:")

            CustomCheckboxRow(
                text = "Записать ходы партии по ссылки",
                checked = viewModel.secondMatchMovesAsLink.value,
                onCheckedChange = { viewModel.secondMatchMovesAsLink.value = it },
            )

            Spacer(Modifier.padding(8.dp))
            CustomTextField(
                value = viewModel.secondMatchMoves.value,
                onValueChange = { newValue: TextFieldValue ->
                    viewModel.secondMatchMoves.value = newValue
                },
                label = {
                    Text(
                        text =
                            if (viewModel.secondMatchMovesAsLink.value) {
                                "Вставьте ссылку на анализ партии"
                            } else {
                                "Ходы в формате PGN"
                            },
                    )
                },
                placeholder = {
                    Text(
                        text = if (viewModel.secondMatchMovesAsLink.value) "https://lichess.org/" else "1.e4 e5 2.Nf3 Nc6 ...",
                    )
                },
                singleLine = false,
                maxLines = if (isFocusedSecondMatchMovesField.value) 10 else 2,
                minLines = if (isFocusedSecondMatchMovesField.value) 4 else 1,
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .onFocusChanged { isFocusedSecondMatchMovesField.value = it.isFocused },
            )
            Spacer(Modifier.padding(8.dp))
            if (!viewModel.secondMatchMovesAsLink.value) {
                DataBlockTitleText("Победитель")
                Spacer(Modifier.padding(8.dp))
                DropdownSelectButton(
                    items = listOf(viewModel.firstPlayerName.value.text, viewModel.secondPlayerName.value.text, "Ничья"),
                    selectedItem =
                        when (viewModel.secondMatchResult.value) {
                            GameResult.FIRST_PLAYER_IS_WINNER -> viewModel.firstPlayerName.value.text
                            GameResult.SECOND_PLAYER_IS_WINNER -> viewModel.secondPlayerName.value.text
                            GameResult.DRAW -> "Ничья"
                            else -> "Что-то пошло не так=("
                        },
                    onItemSelected = { selectedResult: String ->
                        when (selectedResult) {
                            viewModel.firstPlayerName.value.text -> viewModel.secondMatchResult.value = GameResult.FIRST_PLAYER_IS_WINNER
                            viewModel.secondPlayerName.value.text -> viewModel.secondMatchResult.value = GameResult.SECOND_PLAYER_IS_WINNER
                            else -> viewModel.secondMatchResult.value = GameResult.DRAW
                        }
                    },
                )
                Spacer(Modifier.padding(8.dp))
            }
            DataBlockTitleText("Выберите директорию, в которую будет сохранён отчёт:")
            Spacer(Modifier.padding(8.dp))
            SelectPathButton(
                selectedPath = viewModel.path.value.pathString,
                onPathSelected = { newValue: String ->
                    viewModel.path.value = Path(newValue)
                },
            )
            Spacer(Modifier.padding(8.dp))
            CustomButton(
                onClick = {
                    viewModel.generateReport(viewModel.path.value)
                    scope.launch {
                        // Задержка для того, чтобы Compose успевал прорисовать новую кнопку и можно было проскролить вниз
                        delay(10)
                        scrollState.animateScrollTo(scrollState.maxValue)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                buttonText = "Сгенерировать отчёт",
            )
            if (viewModel.isGenerated.value) {
                Spacer(Modifier.padding(8.dp))
                CustomButton(
                    onClick = {
                        try {
                            Desktop.getDesktop().open(File(viewModel.path.value.pathString))
                        } catch (ex: Exception) {
                            openDirectoryError.value = "Не удалось открыть директорию:${viewModel.path.value}. Ex:$ex"
                            Logger.error(openDirectoryError.value)
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    buttonText = "Открыть папку с отчётом",
                )
                if (openDirectoryError.value.isNotEmpty()) {
                    Spacer(Modifier.padding(8.dp))
                    Text(
                        text = openDirectoryError.value,
                        color = MaterialTheme.colors.error,
                        style = MaterialTheme.typography.caption,
                    )
                }
            }
        }
        VerticalScrollbar(
            modifier =
                Modifier
                    .align(Alignment.CenterEnd)
                    .fillMaxHeight(),
            adapter = rememberScrollbarAdapter(scrollState),
        )
    }
}

@Suppress("ktlint:standard:function-naming")
@Preview
@Composable
private fun PreviewDataBlock() {
    AppTheme {
        PreviewSurface(
            content = {
                DataBlock(MainScreenViewModel())
            },
        )
    }
}
