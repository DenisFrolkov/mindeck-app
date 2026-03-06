package com.mindeck.presentation.ui.components.topBar

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import com.mindeck.presentation.R
import com.mindeck.presentation.ui.components.buttons.ActionHandlerButton

@Composable
fun AppTopBar(
    showMenuButton: Boolean = false,
    onBackClick: () -> Unit,
    onMenuClick: () -> Unit = {},
    modifier: Modifier = Modifier,
) {
    val animatedAlpha by animateFloatAsState(
        targetValue = if (!showMenuButton) 0f else 1f,
        animationSpec = tween(durationMillis = DURATION_100),
    )

    Row(
        modifier = modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        ActionHandlerButton(
            painter = painterResource(R.drawable.img_back),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            onClick = onBackClick,
        )
        ActionHandlerButton(
            painter = painterResource(R.drawable.img_menu),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            onClick = {
                if (showMenuButton) {
                    onMenuClick()
                }
            },
            modifier = Modifier.alpha(animatedAlpha),
        )
    }
}

const val DURATION_100 = 100
