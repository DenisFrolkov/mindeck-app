package com.mindeck.presentation.ui.components.buttons

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mindeck.presentation.ui.theme.outline_variant_blue
import com.mindeck.presentation.ui.theme.on_primary_white

@Composable
fun SaveDataButton(
    text: String,
    onClick: () -> Unit,
    fontSize: TextUnit = 14.sp,
    fontFamily: FontFamily,
    color: Color = on_primary_white,
    buttonModifier: Modifier = Modifier,
    textModifier: Modifier = Modifier
) {
    Box(
        modifier = buttonModifier
            .background(color = outline_variant_blue, shape = RoundedCornerShape(6.dp))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                onClick()
            },
    ) {
        Text(
            text = text,
            style = TextStyle(fontSize = fontSize, fontFamily = fontFamily, color = color),
            modifier = textModifier.padding(vertical = 12.dp, horizontal = 38.dp)
        )
    }
}