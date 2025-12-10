package io.github.vvmaksim.app.config

import java.awt.Color

object PrivateConfig {
    const val MIN_WIDTH = 600
    const val MIN_HEIGHT = 800

    const val START_WIDTH = 650
    const val START_HEIGHT = 950

    const val REGULAR_FONT_PATH = "resources/fonts/Rubik-Regular.ttf"
    const val BOLD_FONT_PATH = "resources/fonts/Rubik-Bold.ttf"
    const val MEDIUM_FONT_PATH = "resources/fonts/Rubik-Medium.ttf"
    const val LIGHT_FONT_PATH = "resources/fonts/Rubik-Light.ttf"

    val SECONDARY_TABLE_COLOR = Color(46, 117, 182)

    fun getDefaultUserDirPath(): String = "${System.getProperty("user.home")}/.chess-report"
}
