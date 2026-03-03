package com.mindeck.presentation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.mindeck.presentation.R
import com.mindeck.presentation.state.ModalState
import com.mindeck.presentation.state.UiState
import com.mindeck.presentation.ui.components.dropdown.AppDropdownMenu
import com.mindeck.presentation.ui.components.dropdown.AppDropdownMenuItem
import com.mindeck.presentation.ui.components.repeatOptions.RepeatOptionData
import com.mindeck.presentation.ui.components.repeatOptions.RepeatOptionsButton
import com.mindeck.presentation.ui.components.topBar.AppTopBar
import com.mindeck.presentation.ui.navigation.Navigator
import com.mindeck.presentation.ui.theme.repeat_button_light_blue
import com.mindeck.presentation.ui.theme.repeat_button_light_mint
import com.mindeck.presentation.ui.theme.repeat_button_light_red
import com.mindeck.presentation.ui.theme.repeat_button_light_yellow
import com.mindeck.presentation.viewmodel.CardStudyViewModel

@Composable
fun CardStudyScreen(
    navigator: Navigator,
    cardId: Int? = null,
    modifier: Modifier = Modifier,
) {
    CardStudyScreenContent(
        navigator = navigator,
        viewModel = hiltViewModel<CardStudyViewModel>(),
        cardId = cardId,
        modifier = modifier,
    )
}

@Composable
private fun CardStudyScreenContent(
    navigator: Navigator,
    viewModel: CardStudyViewModel = hiltViewModel<CardStudyViewModel>(),
    cardId: Int? = null,
    modifier: Modifier = Modifier,
) {
    LaunchedEffect(Unit) {
        if (cardId != null) {
            viewModel.loadCardById(cardId)
        } else {
            val currentTime = System.currentTimeMillis()
            viewModel.loadCardRepetition(currentTime)
        }
    }

    val modalState by viewModel.modalState.collectAsState()
    val cardsForRepetitionState by viewModel.cardsForRepetitionState.collectAsState()
    var currentIndex by remember { mutableIntStateOf(0) }
    val scrollState = rememberScrollState()

    Scaffold(
        modifier = modifier
            .fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            AppTopBar(
                visibleMenuButton = cardsForRepetitionState is UiState.Success,
                onBackClick = { navigator.pop() },
                onMenuClick = { viewModel.showDropdownMenu() },
                modifier = Modifier.padding(horizontal = dimensionResource(R.dimen.padding_medium)),
            )
        },
        content = { padding ->
            when (val state = cardsForRepetitionState) {
                is UiState.Success -> {
                    val cards = state.data
                    if (cards.isEmpty() || currentIndex >= cards.size) {
                        LaunchedEffect(currentIndex) { navigator.pop() }
                    } else {
                        val card = cards[currentIndex]
                        LaunchedEffect(currentIndex) { scrollState.scrollTo(0) }
                        Column(
                            modifier = Modifier
                                .padding(padding)
                                .padding(horizontal = dimensionResource(R.dimen.dimen_16))
                                .statusBarsPadding()
                                .verticalScroll(state = scrollState),
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            Text(
                                card.cardQuestion,
                                style = MaterialTheme.typography.titleLarge,
                                modifier = Modifier,
                            )
                            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.dimen_10)))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(MaterialTheme.shapes.large)
                                    .background(MaterialTheme.colorScheme.primary)
                                    .height(dimensionResource(R.dimen.dimen_1))
                                    .padding(horizontal = dimensionResource(R.dimen.dimen_16)),
                            )
                            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.dimen_10)))
                            Text(
                                card.cardAnswer,
                                style = MaterialTheme.typography.titleLarge,
                                modifier = Modifier,
                            )
                        }
                        RepeatButtons(
                            repeatOptionsButton = listOf(
                                RepeatOptionData(
                                    title = stringResource(R.string.repeat_option_title_repeat_text),
                                    color = repeat_button_light_blue,
                                    action = { currentIndex++ },
                                ),
                                RepeatOptionData(
                                    title = stringResource(R.string.repeat_option_title_easy_text),
                                    color = repeat_button_light_mint,
                                    action = { currentIndex++ },
                                ),
                                RepeatOptionData(
                                    title = stringResource(R.string.repeat_option_title_medium_text),
                                    color = repeat_button_light_yellow,
                                    action = { currentIndex++ },
                                ),
                                RepeatOptionData(
                                    title = stringResource(R.string.repeat_option_title_hard_text),
                                    color = repeat_button_light_red,
                                    action = { currentIndex++ },
                                ),
                            ),
                        )
                    }

                    AppDropdownMenu(
                        padding = padding,
                        isExpanded = modalState is ModalState.DropdownMenu,
                        onDismiss = { viewModel.hideModal() },
                    ) {
                        AppDropdownMenuItem(
                            text = stringResource(R.string.dropdown_menu_data_previous_card),
                            onClick = {
                                viewModel.hideModal()
                                if (currentIndex > 0) {
                                    currentIndex--
                                }
                            },
                        )
                    }
                }

                UiState.Loading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = dimensionResource(R.dimen.dimen_24))
                            .wrapContentSize(Alignment.Center),
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(dimensionResource(R.dimen.circular_progress_indicator_size)),
                            color = MaterialTheme.colorScheme.primary,
                            strokeWidth = dimensionResource(R.dimen.dimen_1_5),
                        )
                    }
                }

                is UiState.Error -> {
                    Text(
                        text = stringResource(R.string.error_get_card_by_card_id),
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.error),
                    )
                }

                UiState.Idle -> Unit
            }
        },
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
            .padding(horizontal = dimensionResource(R.dimen.dimen_16))
            .padding(bottom = dimensionResource(R.dimen.dimen_16))
            .navigationBarsPadding(),
    ) {
        repeatOptionsButton.forEach {
            RepeatOptionsButton(
                buttonColor = it.color,
                textDifficultyOfRepetition = it.title,
                onClick = it.action,
                titleTextStyle = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}
