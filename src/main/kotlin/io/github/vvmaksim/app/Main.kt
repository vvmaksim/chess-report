package io.github.vvmaksim.app

import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import io.github.vvmaksim.app.config.PrivateConfig
import io.github.vvmaksim.app.config.PrivateConfig.MIN_HEIGHT
import io.github.vvmaksim.app.config.PrivateConfig.MIN_WIDTH
import io.github.vvmaksim.viewmodel.MainScreenViewModel
import java.awt.Dimension

fun main() =
    application {
        Logger.startMessage()
        val windowState =
            rememberWindowState(
                position = WindowPosition(Alignment.Center),
                size = DpSize(PrivateConfig.START_WIDTH.dp, PrivateConfig.START_HEIGHT.dp),
            )
        Window(
            onCloseRequest = {
                Logger.finishMessage()
                exitApplication()
            },
            title = "Chess Report",
            state = windowState,
            icon = painterResource("icons/app_icon.png"),
        ) {
            val viewmodel = remember { MainScreenViewModel() }
            window.minimumSize = Dimension(MIN_WIDTH, MIN_HEIGHT)
            App(viewmodel)
        }
    }
