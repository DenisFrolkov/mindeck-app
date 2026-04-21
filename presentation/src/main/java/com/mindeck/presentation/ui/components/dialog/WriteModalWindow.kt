package com.mindeck.presentation.ui.components.dialog

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.window.Dialog
import com.mindeck.presentation.R
import com.mindeck.presentation.state.UiState
import com.mindeck.presentation.ui.components.buttons.ActionHandlerButton
import com.mindeck.presentation.ui.components.buttons.CustomButton
import com.mindeck.presentation.ui.components.textfields.CardInputField
import com.mindeck.presentation.ui.theme.MindeckTheme

@Composable
fun WriteModalWindow(
    titleText: String,
    buttonText: String,
    placeholder: String,
    actionState: UiState<Unit>,
    onExitClick: () -> Unit,
    onSaveClick: (String) -> Unit,
) {
    val isLoading = actionState is UiState.Loading
    val isError = actionState as? UiState.Error
    val errorMessage = isError?.let { stringResource(it.messageRes, *it.args.toTypedArray()) }

    var text by rememberSaveable { mutableStateOf("") }

    val isTextValid = text.trim().isNotEmpty()

    val animatedButtonColor by animateColorAsState(
        targetValue = when {
            !isTextValid -> MaterialTheme.colorScheme.surfaceVariant
            else -> MaterialTheme.colorScheme.primary
        },
        animationSpec = tween(DURATION_300),
        label = "buttonColor",
    )

    val animatedTextColor by animateColorAsState(
        targetValue = when {
            !isTextValid -> MaterialTheme.colorScheme.onSurfaceVariant
            isLoading -> MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.6f)
            else -> MaterialTheme.colorScheme.onPrimary
        },
        animationSpec = tween(DURATION_300),
        label = "textColor",
    )

    Dialog(onDismissRequest = { if (!isLoading) onExitClick() }) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .background(
                    color = MaterialTheme.colorScheme.background,
                    shape = MaterialTheme.shapes.small,
                )
                .clip(MaterialTheme.shapes.small)
                .padding(MindeckTheme.dimensions.paddingXs),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                ActionHandlerButton(
                    painter = painterResource(R.drawable.img_back),
                    contentDescription = stringResource(R.string.back_screen_icon_button),
                    tint = MaterialTheme.colorScheme.primary,
                    onClick = onExitClick,
                )
                Text(
                    text = titleText,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(1f),
                )
                ActionHandlerButton(
                    painter = painterResource(R.drawable.img_menu),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    size = MindeckTheme.dimensions.iconMd,
                    onClick = { },
                    modifier = Modifier.alpha(0f),
                )
            }

            Spacer(modifier = Modifier.height(MindeckTheme.dimensions.spacerMd))

            CardInputField(
                value = text,
                onValueChange = {
                    text = it
                },
                placeholder = placeholder,
                singleLine = true,
                enabled = !isLoading,
                placeholderTextStyle = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onSurfaceVariant),
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        MaterialTheme.colorScheme.surface,
                        MaterialTheme.shapes.large,
                    )
                    .height(height = MindeckTheme.dimensions.dp46)
                    .border(
                        MindeckTheme.dimensions.dp0_25,
                        MaterialTheme.colorScheme.outline,
                        MaterialTheme.shapes.large,
                    )
                    .padding(MindeckTheme.dimensions.paddingSm),
            )

            Spacer(modifier = Modifier.height(MindeckTheme.dimensions.spacerLg))

            CustomButton(
                text = if (isLoading) stringResource(R.string.loading_text) else buttonText,
                color = animatedButtonColor,
                textColor = animatedTextColor,
                onClick = { if (!isLoading && isTextValid) onSaveClick(text) },
                modifier = Modifier.size(
                    height = MindeckTheme.dimensions.dp42,
                    width = MindeckTheme.dimensions.dp140,
                ),
            )

            errorMessage?.let { message ->
                Text(
                    text = message,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            top = MindeckTheme.dimensions.dp4,
                        ),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.error,
                )
            }
        }
    }
}
