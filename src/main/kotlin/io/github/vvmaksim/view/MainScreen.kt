package io.github.vvmaksim.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.vvmaksim.viewmodel.MainScreenViewModel

@Suppress("ktlint:standard:function-naming")
@Composable
fun MainScreen(viewModel: MainScreenViewModel) {
    Box(
        modifier =
            Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background)
                .padding(vertical = 16.dp),
        contentAlignment = Alignment.TopCenter,
    ) {
        DataBlock(viewModel)
    }
}
