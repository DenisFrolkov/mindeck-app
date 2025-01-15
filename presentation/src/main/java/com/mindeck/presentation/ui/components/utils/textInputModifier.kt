package com.mindeck.presentation.ui.components.utils

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.mindeck.presentation.R

@Composable
fun textInputModifier(
    backgroundColor: Color = MaterialTheme.colorScheme.onPrimary,
    topStart: Dp = 0.dp,
    topEnd: Dp = 0.dp,
    bottomStart: Dp = 0.dp,
    bottomEnd: Dp = 0.dp,
    size: Dp = 0.dp
) = Modifier
    .clip(
        shape =
        if (topStart + topEnd + bottomEnd + bottomStart > size) {
            RoundedCornerShape(
                topStart = topStart,
                topEnd = topEnd,
                bottomStart = bottomStart,
                bottomEnd = bottomEnd
            )
        } else {
            RoundedCornerShape(
                size
            )
        },
    )
    .background(
        backgroundColor
    )
    .border(
        dimenDpResource(R.dimen.border_width_dot_two_five),
        MaterialTheme.colorScheme.outline,
        shape =
        if (topStart + topEnd + bottomEnd + bottomStart > size) {
            RoundedCornerShape(
                topStart = topStart,
                topEnd = topEnd,
                bottomStart = bottomStart,
                bottomEnd = bottomEnd
            )
        } else {
            RoundedCornerShape(
                size
            )
        }
    )
    .padding(start = dimenDpResource(R.dimen.padding_small))