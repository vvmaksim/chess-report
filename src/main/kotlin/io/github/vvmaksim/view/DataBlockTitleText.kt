package io.github.vvmaksim.view

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import io.github.vvmaksim.app.theme.AppTheme

@Suppress("ktlint:standard:function-naming")
@Composable
fun DataBlockTitleText(
    text: String,
    modifier: Modifier = Modifier,
    textAlign: TextAlign = TextAlign.Center,
    style: TextStyle = MaterialTheme.typography.body1,
) {
    Text(
        modifier = modifier,
        text = text,
        textAlign = textAlign,
        style = style,
    )
}

@Suppress("ktlint:standard:function-naming")
@Preview
@Composable
private fun PreviewDataBlockTitleText() {
    AppTheme {
        PreviewSurface(
            content = {
                Column(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                ) {
                    DataBlockTitleText(text = "Some looooooooooooooooooooooooooooooooooooong text")
                }
            },
        )
    }
}
