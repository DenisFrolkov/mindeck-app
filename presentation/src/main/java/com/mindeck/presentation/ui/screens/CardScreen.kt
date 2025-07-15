package com.mindeck.presentation.ui.screens

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.mindeck.domain.models.Card
import com.mindeck.domain.models.Deck
import com.mindeck.domain.models.ReviewType
import com.mindeck.presentation.R
import com.mindeck.presentation.state.RenderUiState
import com.mindeck.presentation.state.UiState
import com.mindeck.presentation.state.getOrNull
import com.mindeck.presentation.ui.components.buttons.ActionHandlerButton
import com.mindeck.presentation.ui.components.common.QuestionAndAnswerElement
import com.mindeck.presentation.ui.components.dataclasses.CardAttributes
import com.mindeck.presentation.ui.components.dropdown.dropdown_menu.DropdownMenu
import com.mindeck.presentation.ui.components.dropdown.dropdown_menu.DropdownMenuData
import com.mindeck.presentation.ui.components.dropdown.dropdown_menu.DropdownMenuState
import com.mindeck.presentation.ui.components.dropdown.dropdown_menu.animateDropdownMenuHeightIn
import com.mindeck.presentation.ui.components.utils.dimenDpResource
import com.mindeck.presentation.ui.navigation.NavigationRoute
import com.mindeck.presentation.ui.theme.MindeckTheme
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

    val card = cardViewModel.cardByCardIdUIState.collectAsState().value
    val deck = cardViewModel.deckUIState.collectAsState().value

    val dropdownMenuState = remember { DropdownMenuState() }

    val listDropdownMenu = dropdownMenuDataList(
        navController = navController,
        card = card,
        dropdownMenuState = dropdownMenuState,
        cardViewModel = cardViewModel
    )

    CardContent(
        navController,
        deck,
        dropdownMenuState,
        card,
        listDropdownMenu
    )
}

@Composable
private fun CardContent(
    navController: NavController,
    deck: UiState<Deck>,
    dropdownMenuState: DropdownMenuState,
    card: UiState<Card>,
    listDropdownMenu: List<DropdownMenuData>
) {
    val scrollState = rememberScrollState()

    val nameDeck = deck.getOrNull()?.deckName
    val cardAttributes = cardAttributesList(card = card, nameDeck)

    val dropdownVisibleAnimation = animateDropdownMenuHeightIn(
        targetAlpha = dropdownMenuState.dropdownAlpha,
        animationDuration = dropdownMenuState.animationDuration
    )

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
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
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            ActionHandlerButton(
                iconPainter = painterResource(R.drawable.back_icon),
                contentDescription = stringResource(R.string.back_screen_icon_button),
                iconTint = MaterialTheme.colorScheme.onPrimary,
                onClick = { navController.popBackStack() },
            )
            ActionHandlerButton(
                iconPainter = painterResource(R.drawable.menu_icon),
                contentDescription = stringResource(R.string.back_screen_icon_button),
                iconTint = MaterialTheme.colorScheme.onPrimary,
                onClick = { dropdownMenuState.toggle() },
            )
        }
    }
}

@Composable
fun cardAttributesList(
    card: UiState<Card>,
    deckName: String?
): List<CardAttributes> {
    return when (card) {
        is UiState.Success -> listOf(
            CardAttributes(
                title = stringResource(R.string.text_deck_dropdown_selector),
                value = deckName
            ),
            CardAttributes(
                title = stringResource(R.string.text_type_dropdown_selector),
                value = when (card.data.cardType) {
                    "0" -> stringResource(R.string.text_folder_dropdown_selector_simple)
                    else -> stringResource(R.string.text_folder_dropdown_selector_simple_with_answer_input)
                }
            )
        )

        is UiState.Error -> listOf(
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
        for (attribute in cardAttributes) {
            CardAttributesList(attribute = attribute)
        }
        Spacer(modifier = Modifier.height(height = dimenDpResource(R.dimen.spacer_large)))
        CardInfo(cardState = card)
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
private fun CardAttributesList(attribute: CardAttributes) {
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
                if (attribute.load || attribute.value == null) {
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
                } else {
                    Text(
                        text = attribute.value,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}

@Composable
private fun CardInfo(
    cardState: UiState<Card>
) {
    cardState.RenderUiState(
        onSuccess = { card ->
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = card.cardName,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
            Spacer(modifier = Modifier.height(height = dimenDpResource(R.dimen.spacer_medium)))
            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                QuestionAndAnswerElement(
                    question = card.cardQuestion,
                    answer = card.cardAnswer,
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

            if (card.cardTag.isEmpty()) {
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
                                text = card.cardTag,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }
        },
        onLoading = {
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
        },
        onError = {
            Text(
                stringResource(R.string.error_get_info_about_deck),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.error)
            )
        }
    )
}

@Composable
private fun CardDropdownMenu(
    padding: PaddingValues,
    listDropdownMenu: List<DropdownMenuData>,
    dropdownVisibleAnimation: Float,
    dropdownMenuState: DropdownMenuState
) {
    Box(
        modifier = Modifier
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

@Preview(
    showBackground = true,
    backgroundColor = 0xFFE6E6FF
)
@Composable
private fun ScreenPreview() {
    val navController = rememberNavController()
    val deckState: UiState<Deck> = deckDataMock()
    val cardState: UiState<Card> = cardDataMock()
    val dropdownMenuState = DropdownMenuState()

    MindeckTheme {
        CardContent(
            navController,
            deckState,
            dropdownMenuState,
            cardState,
            emptyList<DropdownMenuData>()
        )
    }
}

@Preview(
    device = "spec:parent=pixel_5,orientation=landscape",
    showBackground = true,
    backgroundColor = 0xFFE6E6FF
)
@Composable
private fun ScreenPreviewLandscape() {
    val navController = rememberNavController()
    val deckState: UiState<Deck> = deckDataMock()
    val cardState: UiState<Card> = cardDataMock()
    val dropdownMenuState = DropdownMenuState()

    MindeckTheme {
        CardContent(
            navController,
            deckState,
            dropdownMenuState,
            cardState,
            emptyList<DropdownMenuData>()
        )
    }
}

@Composable
private fun deckDataMock(): UiState<Deck> = UiState.Success(
    Deck(
        deckId = 1,
        deckName = "Kotlin Basics"
    )
)

@Composable
private fun cardDataMock(): UiState<Card> = UiState.Success(
    Card(
        cardId = 1,
        cardName = "Basic Kotlin",
        cardQuestion = "Что такое data class в Kotlin?",
        cardAnswer = "Это класс, предназначенный для хранения данных. Он автоматически генерирует equals, hashCode и toString.",
        cardType = "Теория",
        cardTag = "Kotlin",
        deckId = 101,
        firstReviewDate = 1_725_000_000_000,
        lastReviewDate = 1_725_086_400_000,
        nextReviewDate = 1_725_172_800_000,
        repetitionCount = 2,
        lastReviewType = ReviewType.EASY
    )
)