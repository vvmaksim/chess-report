package io.github.vvmaksim.app.theme

import androidx.compose.material.Colors
import androidx.compose.ui.graphics.Color

object Theme {
    val colors =
        Colors(
            primary = Color("FF6200EE".toLong(16)),
            primaryVariant = Color("FF757575".toLong(16)),
            secondary = Color("FFFFC107".toLong(16)),
            secondaryVariant = Color("FFFFA000".toLong(16)),
            background = Color("FFE6E6FA".toLong(16)),
            surface = Color("FFF5F5F5".toLong(16)),
            error = Color("FFF44336".toLong(16)),
            onPrimary = Color("FFFFFFFF".toLong(16)),
            onSecondary = Color("FF000000".toLong(16)),
            onBackground = Color("FF8E8E9B".toLong(16)),
            onSurface = Color("FF000000".toLong(16)),
            onError = Color("FFFFFFFF".toLong(16)),
            isLight = true,
        )
}
