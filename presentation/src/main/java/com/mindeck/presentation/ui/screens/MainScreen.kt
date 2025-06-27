package com.mindeck.presentation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.mindeck.domain.models.Card
import com.mindeck.domain.models.Deck
import com.mindeck.domain.models.ReviewType
import com.mindeck.presentation.R
import com.mindeck.presentation.state.RenderUiState
import com.mindeck.presentation.state.UiState
import com.mindeck.presentation.state.onSuccess
import com.mindeck.presentation.ui.components.daily_progress_tracker.DailyProgressTracker
import com.mindeck.presentation.ui.components.daily_progress_tracker.DailyProgressTrackerState
import com.mindeck.presentation.ui.components.dataclasses.DisplayItemData
import com.mindeck.presentation.ui.components.dataclasses.DisplayItemStyle
import com.mindeck.presentation.ui.components.dialog.CustomModalWindow
import com.mindeck.presentation.ui.components.fab.FAB
import com.mindeck.presentation.ui.components.fab.FabMenuData
import com.mindeck.presentation.ui.components.fab.FabState
import com.mindeck.presentation.ui.components.fab.FabState.Companion.ITEM_HEIGHT
import com.mindeck.presentation.ui.components.folder.DisplayItem
import com.mindeck.presentation.ui.components.utils.dimenDpResource
import com.mindeck.presentation.ui.components.utils.dimenFloatResource
import com.mindeck.presentation.ui.navigation.NavigationRoute
import com.mindeck.presentation.ui.theme.MindeckTheme
import com.mindeck.presentation.viewmodel.MainViewModel

@Composable
fun MainScreen(
    navController: NavController,
) {
    val mainViewModel: MainViewModel = hiltViewModel(navController.currentBackStackEntry!!)

    LaunchedEffect(Unit) {
        mainViewModel.initRepetition()
    }

    val decksState = mainViewModel.decksState.collectAsState().value
    val cardsForRepetitionState = mainViewModel.cardsForRepetitionState.collectAsState().value
    val createDeckState = mainViewModel.createDeckState.collectAsState().value
    val modalWindowValue by mainViewModel.modalWindowValue.collectAsState()

    MainContent(
        navController,
        decksState,
        cardsForRepetitionState,
        createDeckState,
        modalWindowValue,
        onSaveDeck = { deckName ->
            mainViewModel.createDeck(deckName)
        },
        toggleModalWindow = {
            mainViewModel.toggleModalWindow(it)
        }
    )
}

@Composable
private fun MainContent(
    navController: NavController,
    decksState: UiState<List<Deck>>,
    cardsForRepetitionState: UiState<List<Card>>,
    createDeckState: UiState<Unit>,
    initialDialog: Boolean = false,
    onSaveDeck: (String) -> Unit,
    toggleModalWindow: (Boolean) -> Unit,
) {
    val dailyProgressTrackerState =
        remember { DailyProgressTrackerState() }

    val fabMenuItems = getFabMenuItems({ toggleModalWindow(true) }, navController)
    val fabState = remember { FabState(expandedHeight = ITEM_HEIGHT.dp * fabMenuItems.size) }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        floatingActionButton = {
            FAB(
                fabIcon = painterResource(R.drawable.fab_menu_icon),
                fabMenuItems = fabMenuItems,
                fabColor = MaterialTheme.colorScheme.outlineVariant,
                fabIconColor = MaterialTheme.colorScheme.onPrimary,
                fabState = fabState,
                textStyle = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        content = { paddingValues ->
            PageContent(
                paddingValues,
                dailyProgressTrackerState,
                decksState,
                cardsForRepetitionState,
                navController
            )

            if (initialDialog) {
                Dialog(
                    onDismissRequest = {
                        toggleModalWindow(false)
                    }
                ) {
                    CustomModalWindow(
                        stringResource(R.string.create_item_dialog_text_creating_deck),
                        stringResource(R.string.create_item_dialog_text_create_deck),
                        stringResource(R.string.create_item_dialog_text_input_name_deck),
                        createDeckState,
                        exitButton = {
                            toggleModalWindow(false)
                        },
                        saveButton = {
                            onSaveDeck(it)
                        }
                    )
                }
            }
        }
    )
}

@Composable
private fun PageContent(
    paddingValues: PaddingValues,
    dailyProgressTrackerState: DailyProgressTrackerState,
    decksState: UiState<List<Deck>>,
    cardsRepetition: UiState<List<Card>>,
    navController: NavController
) {
    Column(
        modifier = Modifier
            .padding(paddingValues)
            .padding(dimenDpResource(R.dimen.padding_medium))
    ) {
        DailyProgressTracker(
            cardsRepetitionState = cardsRepetition,
            dptIcon = painterResource(R.drawable.dpt_icon),
            dailyProgressTrackerState = dailyProgressTrackerState
        )
        Spacer(modifier = Modifier.height(dimenDpResource(R.dimen.spacer_large)))
        RepeatCardItem(navController = navController, cardsRepetitionState = cardsRepetition)
        Spacer(modifier = Modifier.height(dimenDpResource(R.dimen.spacer_large)))
        DecksSection(navController = navController, decksState = decksState)
    }
}

@Composable
private fun DecksSection(
    navController: NavController,
    decksState: UiState<List<Deck>>
) {
    decksState.RenderUiState(
        onSuccess = { decks ->
            decks.take(5).forEach { folder ->
                DeckItem(navController, folder)
                Spacer(modifier = Modifier.height(dimenDpResource(R.dimen.spacer_small)))
            }
            if (decks.size > 5) {
                Spacer(modifier = Modifier.height(dimenDpResource(R.dimen.spacer_small)))
                ButtonAllFolders(navController)
            }
        },
        onLoading = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentSize(Alignment.Center)
            ) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.primary,
                    strokeWidth = dimenDpResource(R.dimen.circular_progress_indicator_weight_one)
                )
            }
        },
        onError = {
            Text(
                stringResource(R.string.error_get_all_decks),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.error)
            )
        }
    )
}

@Composable
private fun RepeatCardItem(
    navController: NavController,
    cardsRepetitionState: UiState<List<Card>>
) {
    cardsRepetitionState.onSuccess { cardsRepetition ->
        if (cardsRepetition.isNotEmpty()) {
            DisplayItem(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        dimenDpResource(R.dimen.border_width_dot_two_five),
                        MaterialTheme.colorScheme.outline,
                        MaterialTheme.shapes.small
                    )
                    .clip(shape = MaterialTheme.shapes.small)
                    .height(dimenDpResource(R.dimen.display_card_item_size))
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                        navController.navigate(NavigationRoute.RepeatCardsScreen.route)
                    },
                showCount = true,
                displayItemData = DisplayItemData(
                    itemIcon = R.drawable.folder_icon,
                    numberOfCards = cardsRepetition.size,
                    itemName = stringResource(R.string.text_repeat_cards)
                ),
                displayItemStyle = DisplayItemStyle(
                    backgroundColor = MaterialTheme.colorScheme.tertiary.copy(
                        dimenFloatResource(R.dimen.float_zero_dot_five_significance)
                    ),
                    iconColor = MaterialTheme.colorScheme.onTertiary,
                    textStyle = MaterialTheme.typography.bodyMedium,
                )
            )
        }

    }
}

@Composable
private fun DeckItem(
    navController: NavController,
    deck: Deck
) {
    DisplayItem(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                dimenDpResource(R.dimen.border_width_dot_two_five),
                MaterialTheme.colorScheme.outline,
                MaterialTheme.shapes.small
            )
            .clip(shape = MaterialTheme.shapes.small)
            .height(dimenDpResource(R.dimen.display_card_item_size))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                navController.navigate(
                    NavigationRoute.DeckScreen.createRoute(
                        deck.deckId
                    )
                )
            },
        showCount = false,
        displayItemData = DisplayItemData(
            itemIcon = R.drawable.folder_icon,
            numberOfCards = deck.deckId,
            itemName = deck.deckName
        ),
        displayItemStyle = DisplayItemStyle(
            backgroundColor = MaterialTheme.colorScheme.secondary.copy(
                dimenFloatResource(R.dimen.float_zero_dot_five_significance)
            ),
            iconColor = MaterialTheme.colorScheme.outlineVariant,
            textStyle = MaterialTheme.typography.bodyMedium,
        )
    )
}

@Composable
private fun ButtonAllFolders(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentSize(Alignment.Center)
    ) {
        Box(
            modifier = Modifier
                .background(
                    color = MaterialTheme.colorScheme.outlineVariant,
                    shape = MaterialTheme.shapes.medium
                )
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) {
                    navController.navigate(NavigationRoute.DecksScreen.route)
                }) {
            Text(
                text = stringResource(R.string.title_text_all_decks),
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onPrimary
                ),
                modifier = Modifier.padding(
                    vertical = dimenDpResource(R.dimen.padding_medium),
                    horizontal = dimenDpResource(R.dimen.padding_extra_large)
                )
            )
        }
    }
}

@Composable
private fun getFabMenuItems(
    openModalWindow: () -> Unit,
    navController: NavController
): List<FabMenuData> {
    return listOf(
        FabMenuData(
            idItem = 0,
            text = stringResource(R.string.fab_menu_data_create_deck),
            icon = R.drawable.fab_open_menu_create_folder_icon,
            navigation = {
                openModalWindow()
            }
        ),
        FabMenuData(
            idItem = 1,
            text = stringResource(R.string.fab_menu_data_create_card_list),
            icon = R.drawable.fab_open_menu_create_card_icon,
            navigation = { navController.navigate(NavigationRoute.CreationCardScreen.route) }
        )
    )
}

@Preview(
    showBackground = true,
    backgroundColor = 0xFFE6E6FF
)
@Composable
private fun ScreenPreview() {
    val navController = rememberNavController()
    val decksState: UiState<List<Deck>> = decksDataMock()
    val cardsForRepetitionState: UiState<List<Card>> = cardsForRepetitionDataMock()

    MindeckTheme {
        MainContent(
            navController,
            decksState,
            cardsForRepetitionState,
            UiState.Loading,
            false,
            { },
            { _ ->}
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
    val decksState: UiState<List<Deck>> = decksDataMock()
    val cardsForRepetitionState: UiState<List<Card>> = cardsForRepetitionDataMock()

    MindeckTheme {
        MainContent(
            navController,
            decksState,
            cardsForRepetitionState,
            UiState.Loading,
            false,
            { },
            { _ ->}
        )
    }
}

@Preview(
    showBackground = true,
    backgroundColor = 0xFFE6E6FF
)
@Composable
private fun ScreenPreviewOpenFAB() {
    val navController = rememberNavController()
    val decksState: UiState<List<Deck>> = decksDataMock()
    val cardsForRepetitionState: UiState<List<Card>> = cardsForRepetitionDataMock()
    val fabModalWindow = true

    MindeckTheme {
        MainContent(
            navController,
            decksState,
            cardsForRepetitionState,
            UiState.Loading,
            fabModalWindow,
            { },
            { _ ->}
        )
    }
}

@Preview(
    device = "spec:parent=pixel_5,orientation=landscape",
    showBackground = true,
    backgroundColor = 0xFFE6E6FF
)
@Composable
private fun ScreenPreviewOpenFABLandscape() {
    val navController = rememberNavController()
    val decksState: UiState<List<Deck>> = decksDataMock()
    val cardsForRepetitionState: UiState<List<Card>> = cardsForRepetitionDataMock()
    val fabModalWindow = true

    MindeckTheme {
        MainContent(
            navController,
            decksState,
            cardsForRepetitionState,
            UiState.Loading,
            fabModalWindow,
            { },
            { _ ->}
        )
    }
}

@Composable
private fun decksDataMock(): UiState<List<Deck>> = UiState.Success(
    listOf<Deck>(
        Deck(
            deckId = 1,
            deckName = "Kotlin Basics"
        ),
        Deck(
            deckId = 2,
            deckName = "Jetpack Compose"
        ),
        Deck(
            deckId = 3,
            deckName = "Architecture Patterns"
        ),
        Deck(
            deckId = 4,
            deckName = "Coroutines & Flow"
        )
    )
)

@Composable
private fun cardsForRepetitionDataMock(): UiState<List<Card>> = UiState.Success(
    listOf<Card>(
        Card(
            cardId = 1,
            cardName = "Basics of Kotlin",
            cardQuestion = "What is a data class in Kotlin?",
            cardAnswer = "A class used to hold data; automatically provides equals(), hashCode(), toString(), etc.",
            cardType = "text",
            cardTag = "kotlin",
            deckId = 1,
            firstReviewDate = System.currentTimeMillis() - 5 * 24 * 60 * 60 * 1000,
            lastReviewDate = System.currentTimeMillis() - 1 * 24 * 60 * 60 * 1000,
            nextReviewDate = System.currentTimeMillis() + 2 * 24 * 60 * 60 * 1000,
            repetitionCount = 3,
            lastReviewType = ReviewType.MEDIUM
        ),
        Card(
            cardId = 2,
            cardName = "Jetpack Compose",
            cardQuestion = "What is @Composable?",
            cardAnswer = "A function annotation that marks a function as composable.",
            cardType = "text",
            cardTag = "compose",
            deckId = 1,
            firstReviewDate = System.currentTimeMillis() - 10 * 24 * 60 * 60 * 1000,
            lastReviewDate = System.currentTimeMillis() - 3 * 24 * 60 * 60 * 1000,
            nextReviewDate = System.currentTimeMillis() + 1 * 24 * 60 * 60 * 1000,
            repetitionCount = 5,
            lastReviewType = ReviewType.HARD
        ),
        Card(
            cardId = 3,
            cardName = "Design Patterns",
            cardQuestion = "Explain the Singleton pattern.",
            cardAnswer = "A design pattern that ensures a class has only one instance.",
            cardType = "text",
            cardTag = "architecture",
            deckId = 2,
            firstReviewDate = System.currentTimeMillis() - 20 * 24 * 60 * 60 * 1000,
            lastReviewDate = System.currentTimeMillis() - 2 * 24 * 60 * 60 * 1000,
            nextReviewDate = System.currentTimeMillis() + 3 * 24 * 60 * 60 * 1000,
            repetitionCount = 7,
            lastReviewType = ReviewType.EASY
        ),
        Card(
            cardId = 4,
            cardName = "Coroutines",
            cardQuestion = "What does `launch {}` do in Kotlin?",
            cardAnswer = "Starts a new coroutine without blocking the current thread.",
            cardType = "text",
            cardTag = "async",
            deckId = 2,
            firstReviewDate = System.currentTimeMillis() - 7 * 24 * 60 * 60 * 1000,
            lastReviewDate = System.currentTimeMillis() - 1 * 24 * 60 * 60 * 1000,
            nextReviewDate = System.currentTimeMillis() + 5 * 24 * 60 * 60 * 1000,
            repetitionCount = 4,
            lastReviewType = ReviewType.MEDIUM
        )
    )
)