package com.mindeck.presentation.ui.components.topBar

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.mindeck.presentation.R
import com.mindeck.presentation.ui.components.buttons.ActionHandlerButton
import com.mindeck.presentation.ui.components.utils.dimenDpResource

@Composable
fun TopAppBar(
    visibleMenuButton: Boolean,
    onBackClick: () -> Unit,
    onMenuClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val animatedAlpha by animateFloatAsState(if (!visibleMenuButton) 0f else 1f, animationSpec = tween(durationMillis = 100))

    Row(
        modifier = modifier
            .padding(top = dimenDpResource(R.dimen.padding_medium))
            .fillMaxWidth()
            .statusBarsPadding(),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        ActionHandlerButton(
            iconPainter = painterResource(R.drawable.img_back),
            contentDescription = stringResource(R.string.back_screen_icon_button),
            iconTint = MaterialTheme.colorScheme.outlineVariant,
            onClick = onBackClick,
        )
        ActionHandlerButton(
            iconPainter = painterResource(R.drawable.img_menu),
            contentDescription = stringResource(R.string.back_screen_icon_button),
            iconTint = MaterialTheme.colorScheme.outlineVariant,
            onClick = {
                if (visibleMenuButton) {
                    onMenuClick()
                }
            },
            modifier = Modifier.alpha(animatedAlpha),
        )
    }
}
