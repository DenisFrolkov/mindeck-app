package com.mindeck.presentation.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mindeck.presentation.ui.components.dailyProgressTracker.DailyProgressTracker

@Composable
fun MainScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize(),
    ) {
        Box(modifier = Modifier.padding(top = 30.dp).padding(horizontal = 16.dp)) { DailyProgressTracker() }
    }
}