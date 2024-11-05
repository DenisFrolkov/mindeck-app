package com.mindeck.presentation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mindeck.presentation.ui.components.buttons.BackScreenButton
import com.mindeck.presentation.ui.components.dropdown_selector.DropdownSelector
import com.mindeck.presentation.ui.theme.BackgroundScreen

@Composable
fun CreationCardScreen() {
    val insets = WindowInsets.systemBars.asPaddingValues().calculateTopPadding()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = BackgroundScreen)
            .padding(top = insets)
    ) {
        BackScreenButton()
        Spacer(modifier = Modifier.height(20.dp))
        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            DropdownSelector(titleSelector = "Папка:", "Общая папка", modifier = Modifier)
        }
    }
}