package com.mindeck.presentation.ui.screens

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.mindeck.domain.models.Card
import com.mindeck.domain.models.ReviewType
import com.mindeck.presentation.R
import com.mindeck.presentation.state.RenderState
import com.mindeck.presentation.state.UiState
import com.mindeck.presentation.ui.components.buttons.ActionHandlerButton
import com.mindeck.presentation.ui.components.common.QuestionAndAnswerElement
import com.mindeck.presentation.ui.components.repeatOptions.RepeatOptionData
import com.mindeck.presentation.ui.components.repeatOptions.RepeatOptionsButton
import com.mindeck.presentation.ui.components.utils.dimenDpResource
import com.mindeck.presentation.ui.navigation.CardStudyRoute
import com.mindeck.presentation.ui.navigation.DeckRoute
import com.mindeck.presentation.ui.navigation.NavigationRoute
import com.mindeck.presentation.ui.navigation.Navigator
import com.mindeck.presentation.ui.navigation.StackNavigator
import com.mindeck.presentation.ui.theme.MindeckTheme
import com.mindeck.presentation.ui.theme.repeat_button_light_blue
import com.mindeck.presentation.ui.theme.repeat_button_light_mint
import com.mindeck.presentation.ui.theme.repeat_button_light_red
import com.mindeck.presentation.ui.theme.repeat_button_light_yellow
import com.mindeck.presentation.viewmodel.CardStudyViewModel

@Composable
fun CardStudyScreen(
    navigator: Navigator,
    cardStudyViewModel: CardStudyViewModel = hiltViewModel<CardStudyViewModel>(),
    cardId: Int? = null,
) {
    LaunchedEffect(Unit) {
        if (cardId != null) {
            cardStudyViewModel.loadCardById(cardId)
        } else {
            val currentTime = System.currentTimeMillis()
            cardStudyViewModel.loadCardRepetition(currentTime)
        }
    }

    val cardByCardIdState = cardStudyViewModel.cardByCardIdUIState.collectAsState().value
    val cardsForRepetitionState = cardStudyViewModel.cardsForRepetitionState.collectAsState().value
    var currentIndex by remember { mutableIntStateOf(0) }

    CardStudyContent(
        cardsForRepetitionState,
        navigator,
        currentIndex,
    )
}

@Composable
private fun CardStudyContent(
    cardsState: UiState<Any>,
    navigator: Navigator,
    currentIndex: Int,
) {
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = { CardStudyTopBar(navigator = navigator) },
        content = { padding ->
            cardsState.RenderState(
                onSuccess = { cards ->
//                    if (cards is List<*>) {
//                        val cardCount = remember { cards }
//                        val currentCard = cardCount[currentIndex]
//
//                        DeckContent(
//                            padding = padding,
//                            card = currentCard,
//                            scrollState = scrollState,
//                            repeatOptionsButton = repeatOptionDataList(
//                                cardStudyViewModel = cardStudyViewModel,
//                                card = currentCard
//                            ) {
//                                when {
//                                    currentIndex < cardCount.size - 1 -> {
// //                                        currentIndex1 += 1
//                                    }
//
//                                    currentIndex == cardCount.size - 1 -> {
//                                        navController.popBackStack()
//                                    }
//                                }
//                            }
//                        )
//                    } else {
//
//                    }
                },
                onLoading = {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = dimenDpResource(R.dimen.padding_large))
                            .wrapContentSize(Alignment.Center),
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(dimenDpResource(R.dimen.circular_progress_indicator_size)),
                            color = MaterialTheme.colorScheme.primary,
                            strokeWidth = dimenDpResource(R.dimen.circular_progress_indicator_weight_two),
                        )
                    }
                },
                onError = {
                    Text(
                        stringResource(R.string.error_get_card_by_card_id),
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.error),
                    )
                },
            )
        },
    )

//    if (cardId == null) {
//        cardsForRepetitionState.RenderUiState(
//            onSuccess = { allCardsForReview ->
//                Surface(
//                    modifier = Modifier
//                        .fillMaxSize()
//                        .background(MaterialTheme.colorScheme.background)
//                ) {
//                    Scaffold(
//                        topBar = { CardStudyTopBar(navController = navController) },
//                        content = { padding ->
//                            val cardCount = remember { allCardsForReview }
//                            val currentCard = cardCount[currentIndex]
//
//                            DeckContent(
//                                padding = padding,
//                                card = cardId == null currentCard else ,
//                                scrollState = scrollState,
//                                repeatOptionsButton = repeatOptionDataList(
//                                    cardStudyViewModel = cardStudyViewModel,
//                                    card = currentCard
//                                ) {
//                                    when {
//                                        currentIndex < cardCount.size - 1 -> {
//                                            currentIndex1 += 1
//                                        }
//
//                                        currentIndex == cardCount.size - 1 -> {
//                                            navController.popBackStack()
//                                        }
//                                    }
//                                }
//                            )
//
//                            DeckContent(
//                                padding = padding,
//                                card = cardByCardId,
//                                scrollState = scrollState,
//                                repeatOptionsButton = repeatOptionDataList(
//                                    cardStudyViewModel = cardStudyViewModel,
//                                    card = cardByCardId,
//                                    clickButton = {
//                                        navController.popBackStack()
//                                    }
//                                )
//                            )
//
//                        }
//                    )
//                }
//            },
//            onLoading = {
//                Box(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(top = dimenDpResource(R.dimen.padding_large))
//                        .wrapContentSize(Alignment.Center)
//                ) {
//                    CircularProgressIndicator(
//                        modifier = Modifier.size(dimenDpResource(R.dimen.circular_progress_indicator_size)),
//                        color = MaterialTheme.colorScheme.primary,
//                        strokeWidth = dimenDpResource(R.dimen.circular_progress_indicator_weight_two)
//                    )
//                }
//            },
//            onError = {
//                Text(
//                    stringResource(R.string.error_get_card_by_card_id),
//                    modifier = Modifier.fillMaxWidth(),
//                    textAlign = TextAlign.Center,
//                    style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.error)
//                )
//            }
//        )
//
//    } else {
//        cardByCardIdState.RenderUiState(
//            onSuccess = { cardByCardId ->
//                Surface(
//                    modifier = Modifier
//                        .fillMaxSize()
//                        .background(MaterialTheme.colorScheme.background)
//                ) {
//                    Scaffold(
//                        topBar = { CardStudyTopBar(navController = navController) },
//                        content = { padding ->
//                            DeckContent(
//                                padding = padding,
//                                card = cardByCardId,
//                                scrollState = scrollState,
//                                repeatOptionsButton = repeatOptionDataList(
//                                    cardStudyViewModel = cardStudyViewModel,
//                                    card = cardByCardId,
//                                    clickButton = {
//                                        navController.popBackStack()
//                                    }
//                                )
//                            )
//                        }
//                    )
//                }
//            },
//            onLoading = {
//                Box(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(top = dimenDpResource(R.dimen.padding_large))
//                        .wrapContentSize(Alignment.Center)
//                ) {
//                    CircularProgressIndicator(
//                        modifier = Modifier.size(dimenDpResource(R.dimen.circular_progress_indicator_size)),
//                        color = MaterialTheme.colorScheme.primary,
//                        strokeWidth = dimenDpResource(R.dimen.circular_progress_indicator_weight_two)
//                    )
//                }
//            },
//            onError = {
//                Text(
//                    stringResource(R.string.error_get_card_by_card_id),
//                    modifier = Modifier.fillMaxWidth(),
//                    textAlign = TextAlign.Center,
//                    style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.error)
//                )
//            }
//        )
//    }
}

@Composable
private fun DeckContent(
    padding: PaddingValues,
    card: Card,
    scrollState: ScrollState,
    repeatOptionsButton: List<RepeatOptionData>,
) {
    Box(
        modifier = Modifier
            .padding(padding)
            .padding(horizontal = dimenDpResource(R.dimen.padding_medium))
            .statusBarsPadding()
            .verticalScroll(state = scrollState),
    ) {
        CardInfo(padding = padding, card = card)
    }
    RepeatButtons(
        repeatOptionsButton = repeatOptionsButton,
    )
}

@Composable
private fun RepeatButtons(
    repeatOptionsButton: List<RepeatOptionData>,
) {
    Row(
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.Bottom,
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = dimenDpResource(R.dimen.padding_medium))
            .padding(bottom = dimenDpResource(R.dimen.padding_medium))
            .navigationBarsPadding(),
    ) {
        repeatOptionsButton.forEach {
            RepeatOptionsButton(
                buttonColor = it.color,
                textDifficultyOfRepetition = it.title,
                onClick = it.action,
                titleTextStyle = MaterialTheme.typography.bodyMedium.copy(
                    textAlign = TextAlign.Center,
                ),
            )
        }
    }
}

@Composable
private fun CardInfo(
    padding: PaddingValues,
    card: Card,
) {
    Column(
        modifier = Modifier.padding(padding),
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize(),
        ) {
            QuestionAndAnswerElement(
                question = card.cardQuestion,
                answer = card.cardAnswer,
                questionStyle = MaterialTheme.typography.bodyMedium.copy(
                    textAlign = TextAlign.Center,
                ),
                answerStyle = MaterialTheme.typography.bodyMedium.copy(
                    textAlign = TextAlign.Center,
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.onPrimary)
                    .border(
                        dimenDpResource(R.dimen.border_width_dot_five),
                        MaterialTheme.colorScheme.outline,
                        MaterialTheme.shapes.extraSmall,
                    ),
            )
        }
    }
}

@Composable
private fun CardStudyTopBar(
    navigator: Navigator,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = dimenDpResource(R.dimen.padding_medium))
            .padding(top = dimenDpResource(R.dimen.padding_medium))
            .statusBarsPadding(),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        ActionHandlerButton(
            iconPainter = painterResource(R.drawable.back_icon),
            contentDescription = stringResource(R.string.back_screen_icon_button),
            iconTint = MaterialTheme.colorScheme.onPrimary,
            onClick = { navigator.pop() },
        )
        ActionHandlerButton(
            iconPainter = painterResource(R.drawable.menu_icon),
            contentDescription = stringResource(R.string.back_screen_icon_button),
            iconTint = MaterialTheme.colorScheme.onPrimary,
            onClick = { },
        )
    }
}

@Composable
private fun repeatOptionDataList(
    cardStudyViewModel: CardStudyViewModel,
    card: Card,
    clickButton: () -> Unit,
): List<RepeatOptionData> {
    return if (card.repetitionCount != 0) {
        listOf(
            RepeatOptionData(
                title = stringResource(R.string.repeat_option_title_repeat_text),
                color = repeat_button_light_blue,
                action = {
                    cardStudyViewModel.updateReview(
                        card.cardId,
                        card.firstReviewDate,
                        card.repetitionCount,
                        ReviewType.REPEAT,
                    )
                    clickButton()
                },
            ),
            RepeatOptionData(
                title = stringResource(R.string.repeat_option_title_easy_text),
                color = repeat_button_light_mint,
                action = {
                    cardStudyViewModel.updateReview(
                        card.cardId,
                        card.firstReviewDate,
                        card.repetitionCount,
                        ReviewType.EASY,
                    )
                    clickButton()
                },
            ),
            RepeatOptionData(
                title = stringResource(R.string.repeat_option_title_medium_text),
                color = repeat_button_light_yellow,
                action = {
                    cardStudyViewModel.updateReview(
                        card.cardId,
                        card.firstReviewDate,
                        card.repetitionCount,
                        ReviewType.MEDIUM,
                    )
                    clickButton()
                },
            ),
            RepeatOptionData(
                title = stringResource(R.string.repeat_option_title_hard_text),
                color = repeat_button_light_red,
                action = {
                    cardStudyViewModel.updateReview(
                        card.cardId,
                        card.firstReviewDate,
                        card.repetitionCount,
                        ReviewType.HARD,
                    )
                    clickButton()
                },
            ),
        )
    } else {
        listOf(
            RepeatOptionData(
                title = stringResource(R.string.repeat_option_title_repeat_text),
                color = repeat_button_light_blue,
                action = {
                    cardStudyViewModel.updateReview(
                        card.cardId,
                        card.firstReviewDate,
                        card.repetitionCount,
                        ReviewType.REPEAT,
                    )
                    clickButton()
                },
            ),
        )
    }
}

@Preview(
    showBackground = true,
    backgroundColor = 0xFFE6E6FF,
)
@Composable
private fun ScreenPreview() {
    val backStack = remember { mutableStateListOf<NavigationRoute>(CardStudyRoute(1)) }
    val navigator = remember { StackNavigator(backStack) }
    val cardsForRepetitionState: UiState<List<Card>> = cardsForRepetitionDataMock()

    MindeckTheme {
        CardStudyContent(
            cardsForRepetitionState,
            navigator,
            1,
        )
    }
}

@Preview(
    device = "spec:parent=pixel_5,orientation=landscape",
    showBackground = true,
    backgroundColor = 0xFFE6E6FF,
)
@Composable
private fun ScreenPreviewLandscape() {
    val backStack = remember { mutableStateListOf<NavigationRoute>(DeckRoute(1)) }
    val navigator = remember { StackNavigator(backStack) }
    val cardsForRepetitionState: UiState<List<Card>> = cardsForRepetitionDataMock()

    MindeckTheme {
        CardStudyContent(
            cardsForRepetitionState,
            navigator = navigator,
            1,
        )
    }
}

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
            lastReviewType = ReviewType.MEDIUM,
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
            lastReviewType = ReviewType.HARD,
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
            lastReviewType = ReviewType.EASY,
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
            lastReviewType = ReviewType.MEDIUM,
        ),
    ),
)
