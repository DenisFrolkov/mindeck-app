package com.mindeck.presentation.ui.components.repeat_options

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import com.mindeck.presentation.R
import com.mindeck.presentation.ui.components.utils.dimenDpResource

@Composable
fun RepeatOptionsButton(
    buttonColor: Color,
    textDifficultyOfRepetition: String,
    titleTextStyle: TextStyle,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .background(color = buttonColor, shape = MaterialTheme.shapes.extraSmall)
            .border(
                width = dimenDpResource(R.dimen.border_width_dot_two_five),
                color = MaterialTheme.colorScheme.outline,
                shape = MaterialTheme.shapes.extraSmall
            )
            .width(dimenDpResource(R.dimen.repeat_options_button_weight))
            .padding(vertical = dimenDpResource(R.dimen.repeat_options_button_vertical_padding))
            .wrapContentSize(Alignment.Center)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                onClick()
            }
    ) {
        Text(
            text = textDifficultyOfRepetition,
            style = titleTextStyle,
            modifier = Modifier.fillMaxWidth().padding(horizontal = dimenDpResource(R.dimen.padding_extra_small))
        )
    }
}