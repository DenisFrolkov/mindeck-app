package com.mindeck.presentation.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.mindeck.presentation.R
import com.mindeck.presentation.ui.components.dailyProgressTracker.DailyProgressTracker

@Composable
fun MainScreen() {
    val insets = WindowInsets.systemBars.asPaddingValues().calculateTopPadding()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = insets)
            .padding(horizontal = 16.dp)
            .padding(top = 40.dp),
    ) {
        DailyProgressTracker(
            dptIcon = painterResource(R.drawable.dpt_icon)
        )
    }
}