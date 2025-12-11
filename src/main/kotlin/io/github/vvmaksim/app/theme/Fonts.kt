package io.github.vvmaksim.app.theme

import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.platform.Font
import io.github.vvmaksim.app.config.PrivateConfig
import java.io.File

object Fonts {
    val fontFamily =
        FontFamily(
            Font(File(PrivateConfig.REGULAR_FONT_PATH), FontWeight.Normal),
            Font(File(PrivateConfig.BOLD_FONT_PATH), FontWeight.Bold),
            Font(File(PrivateConfig.MEDIUM_FONT_PATH), FontWeight.Medium),
            Font(File(PrivateConfig.LIGHT_FONT_PATH), FontWeight.Light),
        )
}
