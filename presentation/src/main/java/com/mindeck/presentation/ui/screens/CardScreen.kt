package com.mindeck.presentation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController
import com.mindeck.presentation.R
import com.mindeck.presentation.ui.components.common.ActionBar
import com.mindeck.presentation.ui.components.common.QuestionAndAnswerElement
import com.mindeck.presentation.ui.components.dataclasses.CardAttributes
import com.mindeck.presentation.ui.components.dropdown.dropdown_menu.DropdownMenu
import com.mindeck.presentation.ui.components.dropdown.dropdown_menu.DropdownMenuData
import com.mindeck.presentation.ui.components.dropdown.dropdown_menu.DropdownMenuState
import com.mindeck.presentation.ui.components.dropdown.dropdown_menu.animateDropdownMenuHeightIn
import com.mindeck.presentation.ui.components.utils.dimenDpResource
import com.mindeck.presentation.uiState.UiState
import com.mindeck.presentation.viewmodel.CardViewModel

@Composable
fun CardScreen(
    navController: NavController,
    cardViewModel: CardViewModel
) {
    val scrollState = rememberScrollState()

    var dropdownMenuState = remember { DropdownMenuState() }

    val dropdownVisibleAnimation = animateDropdownMenuHeightIn(
        targetAlpha = dropdownMenuState.dropdownAlpha,
        animationDuration = dropdownMenuState.animationDuration
    )

    val card = cardViewModel.cardUIState.collectAsState().value
    val folder = cardViewModel.folderUIState.collectAsState().value

    var listDropdownMenu = listOf(
        DropdownMenuData(
            title = stringResource(R.string.dropdown_menu_data_edit_card),
            action = {

            }
        ),
        DropdownMenuData(
            title = stringResource(R.string.dropdown_menu_data_remove_card),
            action = {

            }
        )
    )

    var cardAttributes = listOf<CardAttributes>()

    when (card) {
        is UiState.Success -> {
            when (folder) {
                is UiState.Success -> {
                    cardAttributes = listOf(
                        CardAttributes(
                            title = stringResource(R.string.text_folder_dropdown_selector),
                            value = folder.data?.folderName ?: stringResource(R.string.text_no_folder)
                        ),
                        CardAttributes(
                            title = stringResource(R.string.text_deck_dropdown_selector),
                            value = card.data.deckId.toString()
                        ),
                        CardAttributes(
                            title = stringResource(R.string.text_type_dropdown_selector),
                            value = card.data.cardType
                        ),
                    )
                }
            }
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(horizontal = dimenDpResource(R.dimen.padding_medium))
            .padding(top = dimenDpResource(R.dimen.padding_medium)),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            ActionBar(
                onBackClick = { navController.popBackStack() },
                onMenuClick = { dropdownMenuState.toggle() },
                containerModifier = Modifier
                    .fillMaxWidth(),
                iconModifier = Modifier
                    .clip(shape = MaterialTheme.shapes.extraLarge)
                    .background(
                        color = MaterialTheme.colorScheme.outlineVariant,
                        shape = MaterialTheme.shapes.extraLarge
                    )
                    .padding(dimenDpResource(R.dimen.padding_small))
                    .size(dimenDpResource(R.dimen.padding_medium)),
            )
        },
        content = { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .verticalScroll(state = scrollState)
            ) {
                cardAttributes.forEach { attribute ->
                    Spacer(modifier = Modifier.height(dimenDpResource(R.dimen.spacer_medium)))
                    Row() {
                        Text(
                            text = attribute.title,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier
                                .padding(dimenDpResource(R.dimen.padding_extra_small))
                                .wrapContentSize(Alignment.CenterStart)
                                .width(dimenDpResource(R.dimen.dropdown_min_weight))
                        )

                        Spacer(modifier = Modifier.width(dimenDpResource(R.dimen.spacer_small)))

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(
                                        color = MaterialTheme.colorScheme.onPrimary,
                                        shape = MaterialTheme.shapes.extraSmall
                                    )
                                    .height(height = dimenDpResource(R.dimen.dropdown_menu_item_height))
                                    .border(
                                        dimenDpResource(R.dimen.border_width_dot_two_five),
                                        MaterialTheme.colorScheme.outline,
                                        shape = MaterialTheme.shapes.extraSmall
                                    )
                                    .wrapContentSize(Alignment.Center)

                            ) {
                                Text(
                                    text = attribute.value,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(height = dimenDpResource(R.dimen.spacer_large)))

                when (card) {
                    is UiState.Success -> {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = card.data.cardName,
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                        Spacer(modifier = Modifier.height(height = dimenDpResource(R.dimen.spacer_medium)))
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                        ) {
                            QuestionAndAnswerElement(
                                question = card.data.cardQuestion,
                                answer = card.data.cardAnswer,
                                questionStyle = MaterialTheme.typography.bodyMedium.copy(
                                    textAlign = TextAlign.Start
                                ),
                                answerStyle = MaterialTheme.typography.bodyMedium.copy(
                                    textAlign = TextAlign.Start
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(MaterialTheme.colorScheme.onPrimary)
                                    .border(
                                        dimenDpResource(R.dimen.border_width_dot_five),
                                        MaterialTheme.colorScheme.outline,
                                        MaterialTheme.shapes.extraSmall
                                    )
                            )
                        }

                        if (card.data.cardTag.isEmpty()) {
                            Spacer(modifier = Modifier.height(height = dimenDpResource(R.dimen.spacer_medium)))
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Text(
                                    text = stringResource(R.string.text_tag_input_field),
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Spacer(modifier = Modifier.width(dimenDpResource(R.dimen.spacer_large)))
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .background(
                                                color = MaterialTheme.colorScheme.onPrimary,
                                                shape = MaterialTheme.shapes.extraSmall
                                            )
                                            .height(height = dimenDpResource(R.dimen.dropdown_menu_item_height))
                                            .border(
                                                dimenDpResource(R.dimen.border_width_dot_two_five),
                                                MaterialTheme.colorScheme.outline,
                                                shape = MaterialTheme.shapes.extraSmall
                                            )
                                            .padding(dimenDpResource(R.dimen.padding_extra_small))
                                            .wrapContentSize(Alignment.CenterStart)

                                    ) {
                                        Text(
                                            text = card.data.cardTag,
                                            style = MaterialTheme.typography.bodyMedium
                                        )
                                    }
                                }
                            }

                        }
                    }
                }
            }
            if (dropdownMenuState.isExpanded) {
                Box(modifier = Modifier
                    .fillMaxSize()
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) { dropdownMenuState.toggle() })

                DropdownMenu(
                    listDropdownMenuItem = listDropdownMenu,
                    dropdownModifier = Modifier
                        .padding(padding)
                        .alpha(dropdownVisibleAnimation)
                        .fillMaxWidth()
                        .padding(top = dimenDpResource(R.dimen.spacer_extra_small))
                        .wrapContentSize(Alignment.TopEnd)
                )
            }
        }
    )
}

