package com.mindeck.presentation.ui.components.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.mindeck.presentation.R
import com.mindeck.presentation.ui.components.buttons.ActionHandlerButton

@Composable
fun ActionBar(
    onBackClick: () -> Unit,
    onMenuClick: () -> Unit,
    containerModifier: Modifier,
    iconModifier: Modifier
) {
    Row(
        modifier = containerModifier,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        ActionHandlerButton(
            iconPainter = painterResource(R.drawable.back_icon),
            contentDescription = stringResource(R.string.back_screen_icon_button),
            iconModifier = iconModifier
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) {
                    onBackClick()
                },
        )
        ActionHandlerButton(
            iconPainter = painterResource(R.drawable.menu_icon),
            contentDescription = stringResource(R.string.back_screen_icon_button),
            iconModifier = iconModifier
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) {
                    onMenuClick()
                }
        )
    }
}