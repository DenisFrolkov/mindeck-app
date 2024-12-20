package com.mindeck.presentation.ui.components.buttons

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
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
import com.mindeck.presentation.R
import com.mindeck.presentation.ui.components.utils.dimenDpResource
import com.mindeck.presentation.ui.theme.outline_variant_blue
import com.mindeck.presentation.ui.theme.on_primary_white

@Composable
fun SaveDataButton(
    text: String,
    textStyle: TextStyle,
    buttonModifier: Modifier = Modifier,
    textModifier: Modifier = Modifier
) {
    Box(
        modifier = buttonModifier,
    ) {
        Text(
            text = text,
            style = textStyle,
            modifier = textModifier.padding(
                vertical = dimenDpResource(R.dimen.padding_small),
                horizontal = dimenDpResource(R.dimen.padding_extra_large)
            )
        )
    }
}