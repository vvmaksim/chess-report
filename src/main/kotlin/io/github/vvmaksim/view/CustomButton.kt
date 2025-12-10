package io.github.vvmaksim.view

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import io.github.vvmaksim.app.theme.AppShapes
import io.github.vvmaksim.app.theme.AppTheme

@Suppress("ktlint:standard:function-naming")
@Composable
fun CustomButton(
    buttonText: String,
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier,
    shape: Shape = AppShapes.medium,
) {
    Button(
        onClick = onClick,
        modifier = modifier.height(46.dp),
        content = {
            Text(
                text = buttonText,
                style = MaterialTheme.typography.button,
            )
        },
        shape = shape,
    )
}

@Suppress("ktlint:standard:function-naming")
@Preview
@Composable
private fun PreviewCustomButton() {
    AppTheme {
        PreviewSurface(
            content = {
                Column(modifier = Modifier.padding(8.dp)) {
                    CustomButton("Button")
                }
            },
            surfaceModifier = Modifier.padding(16.dp),
        )
    }
}
