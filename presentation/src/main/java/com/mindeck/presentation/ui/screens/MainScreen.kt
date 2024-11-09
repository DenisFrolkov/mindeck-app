package com.mindeck.presentation.ui.screens

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mindeck.presentation.R
import com.mindeck.presentation.ui.components.folder.DisplayCardFolder

@Composable
fun MainScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 50.dp)
            .padding(horizontal = 16.dp),
    ) {
        DisplayCardFolder(
            numberOfCards = 1,
            folderName = "Повторите, чтобы не забыть!",
            folderIcon = R.drawable.repeat_card_item,
            modifier = Modifier
        )
    }
}