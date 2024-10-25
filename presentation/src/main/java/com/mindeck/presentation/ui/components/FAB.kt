package com.mindeck.presentation.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch


@Composable
fun FAB(
    fabColor: Color,
    fabIconColor: Color,
    fabShape: Float,
    fabIcon: Painter,
    click: Boolean,
    onClick: () -> Unit
) {

    var clickVar by remember { mutableStateOf(click) }


    val coroutineScope = rememberCoroutineScope()

    val widthSizeFAB = remember { Animatable(60F) }
    val heightSizeFAB = remember { Animatable(60F) }
    val shapeSizeFAB = remember { Animatable(10F) }

    LaunchedEffect(click) {
        coroutineScope.launch {
            widthSizeFAB.animateTo(
                targetValue = if (clickVar) 196F else 60F,
                animationSpec = tween(durationMillis = 100)
            )
            heightSizeFAB.animateTo(
                targetValue = if (clickVar) 126F else 60F,
                animationSpec = tween(durationMillis = 100)
            )
            shapeSizeFAB.animateTo(
                targetValue = if (clickVar) 10F else fabShape,
                animationSpec = tween(durationMillis = 100)
            )
            clickVar = !clickVar
        }
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(
                width = widthSizeFAB.value.dp,
                height = heightSizeFAB.value.dp

            )
            .clip(shape = RoundedCornerShape(shapeSizeFAB.value.dp))
            .background(color = fabColor)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                onClick()
            }

    ) {
        if (clickVar) {

        } else {
            Icon(
                painter = fabIcon,
                contentDescription = "FAB",
                tint = fabIconColor,
                modifier = Modifier.size(20.dp),
            )
        }

    }
}