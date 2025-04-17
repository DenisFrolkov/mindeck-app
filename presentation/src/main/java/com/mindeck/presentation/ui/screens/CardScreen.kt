package com.mindeck.presentation.ui.screens

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.mindeck.domain.models.Card
import com.mindeck.presentation.R
import com.mindeck.presentation.state.UiState
import com.mindeck.presentation.ui.components.common.ActionBar
import com.mindeck.presentation.ui.components.common.QuestionAndAnswerElement
import com.mindeck.presentation.ui.components.dataclasses.CardAttributes
import com.mindeck.presentation.ui.components.dropdown.dropdown_menu.DropdownMenu
import com.mindeck.presentation.ui.components.dropdown.dropdown_menu.DropdownMenuData
import com.mindeck.presentation.ui.components.dropdown.dropdown_menu.DropdownMenuState
import com.mindeck.presentation.ui.components.dropdown.dropdown_menu.animateDropdownMenuHeightIn
import com.mindeck.presentation.ui.components.utils.dimenDpResource
import com.mindeck.presentation.ui.navigation.NavigationRoute
import com.mindeck.presentation.viewmodel.CardViewModel

@Composable
fun CardScreen(
    navController: NavController,
    cardId: Int
) {
    val cardViewModel: CardViewModel = hiltViewModel(navController.currentBackStackEntry!!)

    LaunchedEffect(cardId) {
        cardViewModel.loadCardById(cardId = cardId)
    }

    val scrollState = rememberScrollState()

    val card = cardViewModel.cardByCardIdUIState.collectAsState().value

    val dropdownMenuState = remember { DropdownMenuState() }

    val dropdownVisibleAnimation = animateDropdownMenuHeightIn(
        targetAlpha = dropdownMenuState.dropdownAlpha,
        animationDuration = dropdownMenuState.animationDuration
    )

    val listDropdownMenu = dropdownMenuDataList(
        navController = navController,
        card = card,
        dropdownMenuState = dropdownMenuState,
        cardViewModel = cardViewModel
    )

    val cardAttributes = cardAttributesList(card = card)

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Scaffold(
            topBar = {
                CardTopBar(navController = navController, dropdownMenuState = dropdownMenuState)
            },
            content = { padding ->
                Content(
                    padding = padding,
                    scrollState = scrollState,
                    dropdownVisibleAnimation = dropdownVisibleAnimation,
                    card = card,
                    cardAttributes = cardAttributes,
                    listDropdownMenu = listDropdownMenu,
                    dropdownMenuState = dropdownMenuState
                )
            }
        )
    }
}

@Composable
private fun dropdownMenuDataList(
    navController: NavController,
    card: UiState<Card>,
    dropdownMenuState: DropdownMenuState,
    cardViewModel: CardViewModel
): List<DropdownMenuData> {
    return when (card) {
        is UiState.Success -> {
            listOf(
                DropdownMenuData(
                    title = stringResource(R.string.dropdown_menu_data_study_card),
                    titleStyle = MaterialTheme.typography.bodyMedium,
                    action = {
                        dropdownMenuState.reset()
                        navController.navigate(
                            NavigationRoute.CardStudyScreen.createRoute(
                                card.data.cardId
                            )
                        )
                    }
                ),
                DropdownMenuData(
                    title = stringResource(R.string.dropdown_menu_data_edit_card),
                    titleStyle = MaterialTheme.typography.bodyMedium,
                    action = {
                        dropdownMenuState.reset()
                    }
                ),
                DropdownMenuData(
                    title = stringResource(R.string.dropdown_menu_data_remove_card),
                    titleStyle = MaterialTheme.typography.bodyMedium,
                    action = {
                        dropdownMenuState.reset()
                        cardViewModel.deleteDeck(card = card.data)
                        navController.popBackStack()
                    }
                )
            )
        }

        is UiState.Loading -> {
            listOf(
                DropdownMenuData(
                    title = stringResource(R.string.text_loading),
                    titleStyle = MaterialTheme.typography.bodyMedium,
                    action = {}
                )
            )
        }

        else -> {
            listOf(
                DropdownMenuData(
                    title = stringResource(R.string.text_error_loading),
                    titleStyle = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.error),
                    action = {
                        dropdownMenuState.reset()
                    }
                )
            )
        }
    }
}

@Composable
private fun CardTopBar(
    navController: NavController,
    dropdownMenuState: DropdownMenuState
) {
    Box(
        modifier = Modifier
            .padding(horizontal = dimenDpResource(R.dimen.padding_medium))
            .padding(top = dimenDpResource(R.dimen.padding_medium))
            .statusBarsPadding()
    ) {
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
    }
}

@Composable

fun cardAttributesList(
    card: UiState<Card>,
): List<CardAttributes> {
    return when (card) {
        is UiState.Success -> listOf(
            CardAttributes(
                title = stringResource(R.string.text_deck_dropdown_selector),
                value = card.data.deckId.toString()
            ),
            CardAttributes(
                title = stringResource(R.string.text_type_dropdown_selector),
                value = card.data.cardType
            )
        )

        is UiState.Error -> listOf(
            CardAttributes(
                title = stringResource(R.string.text_folder_dropdown_selector),
                value = stringResource(R.string.text_error_loading)
            ),
            CardAttributes(
                title = stringResource(R.string.text_deck_dropdown_selector),
                value = stringResource(R.string.text_error_loading)
            ),
            CardAttributes(
                title = stringResource(R.string.text_type_dropdown_selector),
                value = stringResource(R.string.text_error_loading)
            )
        )

        else -> listOf(
            CardAttributes(
                title = stringResource(R.string.text_folder_dropdown_selector),
                load = true
            ),
            CardAttributes(
                title = stringResource(R.string.text_deck_dropdown_selector),
                load = true
            ),
            CardAttributes(
                title = stringResource(R.string.text_type_dropdown_selector),
                load = true
            )
        )
    }
}

@Composable

private fun Content(
    padding: PaddingValues,
    scrollState: ScrollState,
    dropdownVisibleAnimation: Float,
    card: UiState<Card>,
    cardAttributes: List<CardAttributes>,
    listDropdownMenu: List<DropdownMenuData>,
    dropdownMenuState: DropdownMenuState

) {
    Column(
        modifier = Modifier
            .padding(padding)
            .padding(horizontal = dimenDpResource(R.dimen.padding_medium))
            .statusBarsPadding()
            .verticalScroll(state = scrollState)
    ) {
        CardAttributesList(cardAttributes = cardAttributes)
        Spacer(modifier = Modifier.height(height = dimenDpResource(R.dimen.spacer_large)))
        CardInfo(card = card)
    }
    if (dropdownMenuState.isExpanded) {
        CardDropdownMenu(
            padding = padding,
            listDropdownMenu = listDropdownMenu,
            dropdownVisibleAnimation = dropdownVisibleAnimation,
            dropdownMenuState = dropdownMenuState
        )
    }
}

@Composable
private fun CardAttributesList(cardAttributes: List<CardAttributes>) {
    for (attribute in cardAttributes) {
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
                    if (attribute.load) {
                        Box(
                            modifier = Modifier
                                .wrapContentSize(Alignment.Center)
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(dimenDpResource(R.dimen.circular_progress_indicator_size_mini)),
                                color = MaterialTheme.colorScheme.primary,
                                strokeWidth = dimenDpResource(R.dimen.circular_progress_indicator_weight_two)
                            )
                        }
                    } else if (attribute.value != null) {
                        Text(
                            text = attribute.value,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
    }
}

@Composable

private fun CardInfo(
    card: UiState<Card>
) {
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

        is UiState.Loading -> {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = dimenDpResource(R.dimen.padding_large))
                    .wrapContentSize(Alignment.Center)
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(dimenDpResource(R.dimen.circular_progress_indicator_size)),
                    color = MaterialTheme.colorScheme.primary,
                    strokeWidth = dimenDpResource(R.dimen.circular_progress_indicator_weight_two)
                )
            }
        }

        is UiState.Error -> {
            Text(
                stringResource(R.string.error_get_info_about_folder),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.error)
            )
        }
    }
}

@Composable
private fun CardDropdownMenu(
    padding: PaddingValues,
    listDropdownMenu: List<DropdownMenuData>,
    dropdownVisibleAnimation: Float,
    dropdownMenuState: DropdownMenuState
) {
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
            .padding(horizontal = dimenDpResource(R.dimen.padding_medium))
            .alpha(dropdownVisibleAnimation)
            .fillMaxWidth()
            .padding(top = dimenDpResource(R.dimen.spacer_extra_small))
            .wrapContentSize(Alignment.TopEnd)
    )
}