package com.mindeck.presentation.ui.components.dialog

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.window.Dialog
import com.mindeck.presentation.R
import com.mindeck.presentation.state.UiState
import com.mindeck.presentation.ui.components.buttons.ActionHandlerButton
import com.mindeck.presentation.ui.components.textfields.CardInputField

@Composable
fun ChooseModalWindow(
    titleText: String,
    items: List<Pair<String, Int>>?,
    selectedId: Int? = null,
    actionState: UiState<Unit>,
    showAddIcon: Boolean = false,
    createTitle: String = "",
    createButtonLabel: String = "",
    createPlaceholder: String = "",
    onExitClick: () -> Unit,
    onSaveClick: (Int) -> Unit,
    onCreateClick: (String) -> Unit = {},
) {
    val isLoading = actionState is UiState.Loading
    val isError = actionState as? UiState.Error
    val errorMessage = isError?.let { stringResource(it.messageRes, *it.args.toTypedArray()) }

    var isCreateMode by rememberSaveable { mutableStateOf(items?.isEmpty() == true) }
    var text by rememberSaveable { mutableStateOf("") }

    val isTextValid = text.trim().isNotEmpty()

    val animatedAlpha by animateFloatAsState(
        if (isCreateMode && showAddIcon) 0f else 1f,
        animationSpec = tween(durationMillis = 500),
    )

    val animatedButtonColor by animateColorAsState(
        targetValue = when {
            !isTextValid -> MaterialTheme.colorScheme.surfaceVariant
            else -> MaterialTheme.colorScheme.primary
        },
        animationSpec = tween(300),
        label = "buttonColor",
    )

    val animatedTextColor by animateColorAsState(
        targetValue = when {
            !isTextValid -> MaterialTheme.colorScheme.onSurfaceVariant
            isLoading -> MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.6f)
            else -> MaterialTheme.colorScheme.onPrimary
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
                    painter = painterResource(R.drawable.img_back),
                    contentDescription = stringResource(R.string.back_screen_icon_button),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    onClick = {
                        if (isCreateMode) {
                            isCreateMode = false
                            text = ""
                        } else {
                            onExitClick()
                        }
                    },
                )
                Text(
                    text = if (isCreateMode && createTitle.isNotEmpty()) createTitle else titleText,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(1f),
                )
                if (!isCreateMode && showAddIcon) {
                    ActionHandlerButton(
                        painter = painterResource(R.drawable.create_deck_img),
                        contentDescription = stringResource(R.string.add_icon_button),
                        tint = MaterialTheme.colorScheme.primary,
                        size = dimensionResource(R.dimen.dimen_24),
                        onClick = {
                            isCreateMode = true
                        },
                        modifier = Modifier.alpha(animatedAlpha),
                    )
                }
            }

            if (isCreateMode) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    CardInputField(
                        placeholder = createPlaceholder,
                        value = text,
                        singleLine = true,
                        enabled = !isLoading,
                        onValueChange = {
                            text = it
                        },
                        placeholderTextStyle = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onSurfaceVariant),
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                MaterialTheme.colorScheme.surface,
                                MaterialTheme.shapes.large,
                            )
                            .border(
                                dimensionResource(R.dimen.border_width_dot_two_five),
                                MaterialTheme.colorScheme.outline,
                                MaterialTheme.shapes.large,
                            )
                            .padding(dimensionResource(R.dimen.card_input_field_item_padding)),
                    )

                    errorMessage?.let { message ->
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

                    Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacer_large)))

                    AnimatedContent(
                        targetState = isLoading,
                        transitionSpec = {
                            fadeIn(animationSpec = tween(300)) togetherWith
                                fadeOut(animationSpec = tween(300))
                        },
                        label = "loading_button",
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
                                            onCreateClick(text)
                                        }
                                    },
                                ),
                        ) {
                            Text(
                                text = if (target) stringResource(R.string.loading_text) else createButtonLabel,
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
            } else {
                items?.let {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(MaterialTheme.shapes.medium)
                            .background(MaterialTheme.colorScheme.surface)
                            .heightIn(max = dimensionResource(R.dimen.dimen_200)),
                        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.dimen_4)),
                    ) {
                        items(it) { item ->
                            val isSelected = selectedId == item.second
                            val backgroundColor by animateColorAsState(
                                targetValue = if (isSelected) {
                                    MaterialTheme.colorScheme.primaryContainer
                                } else {
                                    MaterialTheme.colorScheme.surfaceVariant
                                },
                                animationSpec = tween(300),
                                label = "background_color",
                            )

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(
                                        color = backgroundColor,
                                        shape = MaterialTheme.shapes.small,
                                    )
                                    .clip(MaterialTheme.shapes.small)
                                    .clickable {
                                        onSaveClick(item.second)
                                    }
                                    .padding(dimensionResource(R.dimen.dimen_12)),
                            ) {
                                Text(
                                    text = item.first,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = if (isSelected) {
                                        MaterialTheme.colorScheme.onPrimaryContainer
                                    } else {
                                        MaterialTheme.colorScheme.onSurfaceVariant
                                    },
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
