package com.mindeck.presentation.ui.components.dialog

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
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
import androidx.compose.ui.graphics.Color
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
fun ChooseModalWindow(
    titleText: String,
    chooseObject: List<Pair<String, Int>>?,
    selectedId: Int? = null,
    isLoading: Boolean,
    errorMsg: String?,
    showAddIcon: Boolean = false,
    createTitleText: String = "",
    createButtonText: String = "",
    createPlaceholder: String = "",
    onExitClick: () -> Unit,
    onSaveClick: (Int) -> Unit,
    onCreateClick: (String) -> Unit = {},
) {
    var isCreateMode by rememberSaveable { mutableStateOf(false) }
    var text by rememberSaveable { mutableStateOf("") }

    val isTextValid = text.trim().isNotEmpty()

    val animatedAlpha by animateFloatAsState(
        if (isCreateMode && showAddIcon) 0f else 1f,
        animationSpec = tween(durationMillis = 500),
    )

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
                    text = if (isCreateMode && createTitleText.isNotEmpty()) createTitleText else titleText,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(1f),
                )
                if (!isCreateMode && showAddIcon) {
                    ActionHandlerButton(
                        iconPainter = painterResource(R.drawable.create_deck_img),
                        contentDescription = stringResource(R.string.add_icon_button),
                        iconTint = MaterialTheme.colorScheme.outlineVariant,
                        iconSize = dimensionResource(R.dimen.dimen_24),
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
                                text = if (target) stringResource(R.string.loading_text) else createButtonText,
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
                chooseObject?.let {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(MaterialTheme.shapes.medium)
                            .background(Color.White)
                            .heightIn(max = dimensionResource(R.dimen.dimen_200)),
                        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.dimen_4)),
                    ) {
                        items(chooseObject) { item ->
                            val isSelected = selectedId == item.second
                            val backgroundColor by animateColorAsState(
                                targetValue = if (isSelected) {
                                    MaterialTheme.colorScheme.secondary
                                } else {
                                    MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f)
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
                                        MaterialTheme.colorScheme.onBackground
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
