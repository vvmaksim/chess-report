package io.github.vvmaksim.view

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Checkbox
import androidx.compose.material.CheckboxColors
import androidx.compose.material.CheckboxDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle

@Suppress("ktlint:standard:function-naming")
@Composable
fun CustomCheckboxRow(
    text: String,
    checked: Boolean,
    onCheckedChange: ((Boolean) -> Unit)?,
    colors: CheckboxColors =
        CheckboxDefaults.colors(
            checkedColor = MaterialTheme.colors.primary,
            uncheckedColor = MaterialTheme.colors.onBackground,
        ),
    modifier: Modifier = Modifier,
    textStyle: TextStyle = MaterialTheme.typography.body1,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = colors,
        )
        Text(
            text = text,
            style = textStyle,
        )
    }
}

@Suppress("ktlint:standard:function-naming")
@Preview
@Composable
private fun PreviewCustomCheckboxRow() {
    PreviewSurface(
        content = {
            CustomCheckboxRow(
                text = "Some text",
                checked = false,
                onCheckedChange = {},
            )
        },
    )
}
