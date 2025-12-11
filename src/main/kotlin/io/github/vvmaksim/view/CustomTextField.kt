package io.github.vvmaksim.view

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import io.github.vvmaksim.app.theme.AppShapes
import io.github.vvmaksim.app.theme.AppTheme

@Suppress("ktlint:standard:function-naming")
@Composable
fun CustomTextField(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    modifier: Modifier = Modifier,
    label: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    singleLine: Boolean = true,
    shape: Shape = AppShapes.medium,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    minLines: Int = 1,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        label = label,
        placeholder = placeholder,
        singleLine = singleLine,
        shape = shape,
        maxLines = maxLines,
        minLines = minLines,
    )
}

@Suppress("ktlint:standard:function-naming")
@Preview
@Composable
private fun PreviewCustomTextField() {
    AppTheme {
        PreviewSurface(
            content = {
                Column(modifier = Modifier.padding(16.dp)) {
                    CustomTextField(
                        value = TextFieldValue("Start Value 1"),
                        onValueChange = {},
                    )
                    Spacer(Modifier.padding(8.dp))
                    CustomTextField(
                        value = TextFieldValue("Start Value 2"),
                        onValueChange = {},
                        placeholder = { Text("This is placeholder") },
                        label = { Text("label") },
                    )
                }
            },
        )
    }
}
