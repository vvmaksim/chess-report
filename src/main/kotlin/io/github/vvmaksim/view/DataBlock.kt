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
import io.github.vvmaksim.app.config.TextManager
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

            DataBlockTitleText(text = TextManager.UI.MATCH_INFO_TITLE)
            Spacer(Modifier.padding(8.dp))
            CustomTextField(
                value = viewModel.firstPlayerName.value,
                onValueChange = { newValue: TextFieldValue ->
                    viewModel.firstPlayerName.value = newValue
                    viewModel.reporterName.value = newValue
                },
                label = { Text(TextManager.UI.FIRST_PLAYER_LABEL) },
                placeholder = { Text(TextManager.UI.FULL_NAME_PLACEHOLDER) },
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(Modifier.padding(8.dp))
            CustomTextField(
                value = viewModel.secondPlayerName.value,
                onValueChange = { newValue: TextFieldValue ->
                    viewModel.secondPlayerName.value = newValue
                },
                label = { Text(TextManager.UI.SECOND_PLAYER_LABEL) },
                placeholder = { Text(TextManager.UI.FULL_NAME_PLACEHOLDER) },
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(Modifier.padding(8.dp))
            CustomTextField(
                value = viewModel.reporterName.value,
                onValueChange = { newValue: TextFieldValue ->
                    viewModel.reporterName.value = newValue
                },
                label = { Text(TextManager.UI.REPORTER_LABEL) },
                placeholder = { Text(TextManager.UI.FULL_NAME_PLACEHOLDER) },
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(Modifier.padding(8.dp))
            CustomTextField(
                value = viewModel.teacherName.value,
                onValueChange = { newValue: TextFieldValue ->
                    viewModel.teacherName.value = newValue
                },
                label = { Text(TextManager.UI.TEACHER_LABEL) },
                placeholder = { Text(TextManager.UI.FULL_NAME_PLACEHOLDER) },
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(Modifier.padding(8.dp))
            CustomTextField(
                value = viewModel.tournamentName.value,
                onValueChange = { newValue: TextFieldValue ->
                    viewModel.tournamentName.value = newValue
                },
                label = { Text(TextManager.UI.TOURNAMENT_NAME_LABEL) },
                placeholder = { Text(TextManager.UI.TOURNAMENT_NAME_PLACEHOLDER) },
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(Modifier.padding(8.dp))
            CustomTextField(
                value = viewModel.date.value,
                onValueChange = { newValue: TextFieldValue ->
                    viewModel.date.value = newValue
                },
                label = { Text(TextManager.UI.MATCH_DATE_LABEL) },
                placeholder = { Text(TextManager.UI.MATCH_DATE_PLACEHOLDER) },
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(Modifier.padding(8.dp))
            CustomTextField(
                value = viewModel.studentIDNumber.value,
                onValueChange = { newValue: TextFieldValue ->
                    viewModel.studentIDNumber.value = newValue
                },
                label = { Text(TextManager.UI.STUDENT_ID_NUMBER_LABEL) },
                placeholder = { Text(TextManager.UI.STUDENT_ID_NUMBER_PLACEHOLDER) },
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(Modifier.padding(8.dp))
            CustomTextField(
                value = viewModel.studentGroupNumber.value,
                onValueChange = { newValue: TextFieldValue ->
                    viewModel.studentGroupNumber.value = newValue
                },
                label = { Text(TextManager.UI.STUDENT_GROUP_NUMBER_LABEL) },
                placeholder = { Text(TextManager.UI.STUDENT_GROUP_NUMBER_PLACEHOLDER) },
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(Modifier.padding(8.dp))
            DataBlockTitleText(TextManager.UI.FIRST_GAME_TITLE)

            CustomCheckboxRow(
                text = TextManager.UI.WRITE_MOVES_FROM_LINK,
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
                                TextManager.UI.MATCH_MOVES_LABEL_WITH_LINK
                            } else {
                                TextManager.UI.MATCH_MOVES_LABEL_WITH_PGN
                            },
                    )
                },
                placeholder = {
                    Text(
                        text =
                            if (viewModel.firstMatchMovesAsLink.value) {
                                TextManager.UI.MATCH_MOVES_PLACEHOLDER_WITH_LINK
                            } else {
                                TextManager.UI.MATCH_MOVES_PLACEHOLDER_WITH_PGN
                            },
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
                DataBlockTitleText(TextManager.Words.WINNER)
                Spacer(Modifier.padding(8.dp))
                DropdownSelectButton(
                    items = listOf(viewModel.firstPlayerName.value.text, viewModel.secondPlayerName.value.text, TextManager.Words.DRAW),
                    selectedItem =
                        when (viewModel.firstMatchResult.value) {
                            GameResult.FIRST_PLAYER_IS_WINNER -> viewModel.firstPlayerName.value.text
                            GameResult.SECOND_PLAYER_IS_WINNER -> viewModel.secondPlayerName.value.text
                            GameResult.DRAW -> TextManager.Words.DRAW
                            else -> TextManager.Errors.UNKNOWN_ERROR_MESSAGE
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
            DataBlockTitleText(TextManager.UI.SECOND_GAME_TITLE)

            CustomCheckboxRow(
                text = TextManager.UI.WRITE_MOVES_FROM_LINK,
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
                                TextManager.UI.MATCH_MOVES_LABEL_WITH_LINK
                            } else {
                                TextManager.UI.MATCH_MOVES_LABEL_WITH_PGN
                            },
                    )
                },
                placeholder = {
                    Text(
                        text =
                            if (viewModel.secondMatchMovesAsLink.value) {
                                TextManager.UI.MATCH_MOVES_PLACEHOLDER_WITH_LINK
                            } else {
                                TextManager.UI.MATCH_MOVES_PLACEHOLDER_WITH_PGN
                            },
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
                DataBlockTitleText(TextManager.Words.WINNER)
                Spacer(Modifier.padding(8.dp))
                DropdownSelectButton(
                    items = listOf(viewModel.firstPlayerName.value.text, viewModel.secondPlayerName.value.text, TextManager.Words.DRAW),
                    selectedItem =
                        when (viewModel.secondMatchResult.value) {
                            GameResult.FIRST_PLAYER_IS_WINNER -> viewModel.firstPlayerName.value.text
                            GameResult.SECOND_PLAYER_IS_WINNER -> viewModel.secondPlayerName.value.text
                            GameResult.DRAW -> TextManager.Words.DRAW
                            else -> TextManager.Errors.UNKNOWN_ERROR_MESSAGE
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
            DataBlockTitleText(TextManager.UI.SELECT_DIRECTORY_PATH_FOR_SAVE_REPORT)
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
                buttonText = TextManager.UI.GENERATE_REPORT_BUTTON_TEXT,
            )
            if (viewModel.isGenerated.value) {
                Spacer(Modifier.padding(8.dp))
                CustomButton(
                    onClick = {
                        try {
                            Desktop.getDesktop().open(File(viewModel.path.value.pathString))
                        } catch (ex: Exception) {
                            openDirectoryError.value = TextManager.Errors.getOpenDirectoryErrorMessage(viewModel.path.value, ex.toString())
                            Logger.error(openDirectoryError.value)
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    buttonText = TextManager.UI.OPEN_DIRECTORY_BUTTON_TEXT,
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
