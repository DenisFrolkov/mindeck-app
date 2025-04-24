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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.mindeck.domain.models.Card
import com.mindeck.domain.models.ReviewType
import com.mindeck.presentation.R
import com.mindeck.presentation.state.RenderUiState
import com.mindeck.presentation.ui.components.common.ActionBar
import com.mindeck.presentation.ui.components.common.QuestionAndAnswerElement
import com.mindeck.presentation.ui.components.repeat_options.RepeatOptionData
import com.mindeck.presentation.ui.components.repeat_options.RepeatOptionsButton
import com.mindeck.presentation.ui.components.utils.dimenDpResource
import com.mindeck.presentation.ui.theme.outline_variant_blue
import com.mindeck.presentation.ui.theme.repeat_button_light_blue
import com.mindeck.presentation.ui.theme.repeat_button_light_mint
import com.mindeck.presentation.ui.theme.repeat_button_light_red
import com.mindeck.presentation.ui.theme.repeat_button_light_yellow
import com.mindeck.presentation.viewmodel.CardStudyViewModel

@Composable
fun CardStudyScreen(
    navController: NavController,
    cardId: Int? = null
) {
    val cardStudyViewModel: CardStudyViewModel =
        hiltViewModel(navController.currentBackStackEntry!!)

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

    val scrollState = rememberScrollState()

    if (cardId == null) {
        cardsForRepetitionState.RenderUiState(
            onSuccess = { allCardsForReview ->
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background)
                ) {
                    Scaffold(
                        topBar = { CardStudyTopBar(navController = navController) },
                        content = { padding ->
                            val cardCount = remember { allCardsForReview }
                            val currentCard = cardCount[currentIndex]
                            Content(
                                padding = padding,
                                card = currentCard,
                                scrollState = scrollState,
                                repeatOptionsButton = repeatOptionDataList(
                                    cardStudyViewModel = cardStudyViewModel,
                                    card = currentCard
                                ) {
                                    when {
                                        currentIndex < cardCount.size - 1 -> {
                                            currentIndex += 1
                                        }

                                        currentIndex == cardCount.size - 1 -> {
                                            navController.popBackStack()
                                        }
                                    }
                                }
                            )
                        }
                    )
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
                    stringResource(R.string.error_get_card_by_card_id),
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.error)
                )
            }
        )

    } else {
        cardByCardIdState.RenderUiState(
            onSuccess = { cardByCardId ->
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background)
                ) {
                    Scaffold(
                        topBar = { CardStudyTopBar(navController = navController) },
                        content = { padding ->
                            Content(
                                padding = padding,
                                card = cardByCardId,
                                scrollState = scrollState,
                                repeatOptionsButton = repeatOptionDataList(
                                    cardStudyViewModel = cardStudyViewModel,
                                    card = cardByCardId,
                                    clickButton = {
                                        navController.popBackStack()
                                    }
                                )
                            )
                        }
                    )
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
                    stringResource(R.string.error_get_card_by_card_id),
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.error)
                )
            }
        )
    }
}

@Composable
private fun Content(
    padding: PaddingValues,
    card: Card,
    scrollState: ScrollState,
    repeatOptionsButton: List<RepeatOptionData>
) {
    Box(
        modifier = Modifier
            .padding(padding)
            .padding(horizontal = dimenDpResource(R.dimen.padding_medium))
            .statusBarsPadding()
            .verticalScroll(state = scrollState)
    ) {
        CardInfo(padding = padding, card = card)
    }
    RepeatButtons(
        repeatOptionsButton = repeatOptionsButton,
    )
}

@Composable

private fun RepeatButtons(
    repeatOptionsButton: List<RepeatOptionData>
) {
    Row(
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.Bottom,
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = dimenDpResource(R.dimen.padding_medium))
            .padding(bottom = dimenDpResource(R.dimen.padding_medium))
            .navigationBarsPadding()
    ) {
        repeatOptionsButton.forEach {
            RepeatOptionsButton(
                buttonColor = it.color,
                textDifficultyOfRepetition = it.title,
                onClick = it.action,
                titleTextStyle = MaterialTheme.typography.bodyMedium.copy(
                    textAlign = TextAlign.Center
                ),
            )
        }
    }
}

@Composable
private fun CardInfo(
    padding: PaddingValues,
    card: Card
) {
    Column(
        modifier = Modifier.padding(padding)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
        ) {
            QuestionAndAnswerElement(
                question = card.cardQuestion,
                answer = card.cardAnswer,
                questionStyle = MaterialTheme.typography.bodyMedium.copy(
                    textAlign = TextAlign.Center
                ),
                answerStyle = MaterialTheme.typography.bodyMedium.copy(
                    textAlign = TextAlign.Center
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
    }
}

@Composable
private fun CardStudyTopBar(navController: NavController) {
    Box(
        modifier = Modifier
            .padding(top = dimenDpResource(R.dimen.padding_medium))
            .statusBarsPadding()
    ) {
        ActionBar(
            onBackClick = { navController.popBackStack() },
            onMenuClick = { },
            containerModifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = dimenDpResource(R.dimen.padding_medium)),
            iconModifier = Modifier
                .clip(shape = MaterialTheme.shapes.extraLarge)
                .background(
                    color = outline_variant_blue,
                    shape = MaterialTheme.shapes.extraLarge
                )
                .padding(all = dimenDpResource(R.dimen.padding_small))
                .size(size = dimenDpResource(R.dimen.padding_medium)),
        )
    }
}

@Composable
private fun repeatOptionDataList(
    cardStudyViewModel: CardStudyViewModel,
    card: Card,
    clickButton: () -> Unit
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
                        ReviewType.REPEAT
                    )
                    clickButton()
                }
            ),
            RepeatOptionData(
                title = stringResource(R.string.repeat_option_title_easy_text),
                color = repeat_button_light_mint,
                action = {
                    cardStudyViewModel.updateReview(
                        card.cardId,
                        card.firstReviewDate,
                        card.repetitionCount,
                        ReviewType.EASY
                    )
                    clickButton()
                }
            ),
            RepeatOptionData(
                title = stringResource(R.string.repeat_option_title_medium_text),
                color = repeat_button_light_yellow,
                action = {
                    cardStudyViewModel.updateReview(
                        card.cardId,
                        card.firstReviewDate,
                        card.repetitionCount,
                        ReviewType.MEDIUM
                    )
                    clickButton()
                }
            ),
            RepeatOptionData(
                title = stringResource(R.string.repeat_option_title_hard_text),
                color = repeat_button_light_red,
                action = {
                    cardStudyViewModel.updateReview(
                        card.cardId,
                        card.firstReviewDate,
                        card.repetitionCount,
                        ReviewType.HARD
                    )
                    clickButton()
                }
            ))
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
                        ReviewType.REPEAT
                    )
                    clickButton()
                }
            )
        )
    }
}
