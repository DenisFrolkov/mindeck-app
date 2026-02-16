package com.mindeck.presentation.ui.components.dialog

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.window.Dialog
import com.mindeck.presentation.R
import com.mindeck.presentation.ui.components.buttons.ActionHandlerButton
import com.mindeck.presentation.ui.components.textfields.CardInputField
import com.mindeck.presentation.ui.theme.text_gray

@Composable
fun CustomModalWindow(
    titleText: String,
    buttonText: String,
    placeholder: String,
    isLoading: Boolean,
    errorMsg: String?,
    onExitClick: () -> Unit,
    onSaveClick: (String) -> Unit,
) {
    var text by rememberSaveable { mutableStateOf("") }

    val isTextValid = text.trim().isNotEmpty()

    val animatedButtonColor by animateColorAsState(
        targetValue = when {
            !isTextValid -> MaterialTheme.colorScheme.secondary
            else -> MaterialTheme.colorScheme.onSecondary
        },
        animationSpec = tween(300),
        label = "buttonColor",
    )

    val animatedTextColor by animateColorAsState(
        targetValue = when {
            !isTextValid -> MaterialTheme.colorScheme.outline
            isLoading -> MaterialTheme.colorScheme.onSecondaryContainer
            else -> MaterialTheme.colorScheme.scrim
        },
        animationSpec = tween(300),
        label = "textColor",
    )

    Dialog(onDismissRequest = onExitClick) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .background(
                    color = MaterialTheme.colorScheme.background,
                    shape = MaterialTheme.shapes.small,
                )
                .clip(MaterialTheme.shapes.small)
                .padding(dimensionResource(R.dimen.dimen_8)),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                ActionHandlerButton(
                    iconPainter = painterResource(R.drawable.img_back),
                    contentDescription = stringResource(R.string.back_screen_icon_button),
                    iconTint = MaterialTheme.colorScheme.outlineVariant,
                    onClick = onExitClick,
                )
                Text(
                    text = titleText,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(1f),
                )
            }

            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacer_large)))

            CardInputField(
                placeholder = placeholder,
                value = text,
                singleLine = true,
                enabled = !isLoading,
                onValueChange = {
                    text = it
                },
                textStyle = MaterialTheme.typography.bodyMedium,
                placeholderTextStyle = MaterialTheme.typography.bodyMedium.copy(text_gray),
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        MaterialTheme.colorScheme.onPrimary,
                        MaterialTheme.shapes.large,
                    )
                    .border(
                        dimensionResource(R.dimen.border_width_dot_two_five),
                        MaterialTheme.colorScheme.outline,
                        MaterialTheme.shapes.large,
                    )
                    .padding(dimensionResource(R.dimen.card_input_field_item_padding)),

            )

            AnimatedVisibility(
                visible = errorMsg != null,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically(),
            ) {
                errorMsg?.let { message ->
                    Text(
                        text = message,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                top = dimensionResource(R.dimen.spacer_extra_small),
                            ),
                        textAlign = TextAlign.Start,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.error,
                    )
                }
            }

            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacer_large)))

            AnimatedContent(
                targetState = isLoading,
            ) { target ->
                Box(
                    modifier = Modifier
                        .background(
                            color = animatedButtonColor,
                            shape = MaterialTheme.shapes.medium,
                        )
                        .then(
                            if (text.isEmpty()) {
                                Modifier
                            } else {
                                Modifier.clickable {
                                    onSaveClick(text)
                                }
                            },
                        ),
                ) {
                    Text(
                        text = if (target) stringResource(R.string.loading_text) else buttonText,
                        style = MaterialTheme.typography.bodyMedium,
                        color = animatedTextColor,
                        modifier = Modifier.padding(
                            vertical = dimensionResource(R.dimen.padding_small),
                            horizontal = dimensionResource(R.dimen.padding_extra_large),
                        ),
                    )
                }
            }
        }
    }
}
