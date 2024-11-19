package com.mindeck.presentation.ui.components.buttons

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import com.mindeck.presentation.ui.theme.White

@Composable
fun GetAllFindersButton(textStyle: TextStyle, modifier: Modifier, textModifier: Modifier = Modifier) {
    Box(modifier = modifier) {
        Text(text = "Все папки", color = White, style = textStyle, modifier = textModifier)
    }
}