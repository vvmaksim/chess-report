package io.github.vvmaksim.app

import androidx.compose.runtime.Composable
import io.github.vvmaksim.app.theme.AppTheme
import io.github.vvmaksim.view.MainScreen
import io.github.vvmaksim.viewmodel.MainScreenViewModel

@Suppress("ktlint:standard:function-naming")
@Composable
fun App(viewmodel: MainScreenViewModel) {
    Logger.info("Launching the app rendering")
    AppTheme {
        MainScreen(viewmodel)
    }
}
