package com.mindeck.presentation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mindeck.domain.models.Card
import com.mindeck.presentation.R
import com.mindeck.presentation.state.ModalState
import com.mindeck.presentation.state.UiState
import com.mindeck.presentation.ui.components.dropdown.AppDropdownMenu
import com.mindeck.presentation.ui.components.dropdown.AppDropdownMenuItem
import com.mindeck.presentation.ui.components.repeatOptions.RepeatOptionData
import com.mindeck.presentation.ui.components.repeatOptions.RepeatOptionsButton
import com.mindeck.presentation.ui.components.topBar.AppTopBar
import com.mindeck.presentation.ui.navigation.Navigator
import com.mindeck.presentation.ui.theme.repeat_option_again_dark
import com.mindeck.presentation.ui.theme.repeat_option_again_light
import com.mindeck.presentation.ui.theme.repeat_option_easy_dark
import com.mindeck.presentation.ui.theme.repeat_option_easy_light
import com.mindeck.presentation.ui.theme.repeat_option_hard_dark
import com.mindeck.presentation.ui.theme.repeat_option_hard_light
import com.mindeck.presentation.ui.theme.repeat_option_medium_dark
import com.mindeck.presentation.ui.theme.repeat_option_medium_light
import com.mindeck.presentation.viewmodel.CardStudyViewModel

@Composable
fun CardStudyScreen(
    navigator: Navigator,
    cardId: Int? = null,
    modifier: Modifier = Modifier,
) {
    val viewModel = hiltViewModel<CardStudyViewModel>()
    val modalState by viewModel.modalState.collectAsStateWithLifecycle()
    val cardsState by viewModel.cardsState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        if (cardId != null) {
            viewModel.loadCardById(cardId)
        } else {
            val currentTime = System.currentTimeMillis()
            viewModel.loadCardRepetition(currentTime)
        }
    }

    CardStudyScreenContent(
        modalState = modalState,
        cardsForRepetitionState = cardsState,
        actions = CardStudyScreenActions(
            onNavigateBack = navigator::pop,
            onShowDropdownMenu = viewModel::showDropdownMenu,
            onHideModal = viewModel::hideModal,
        ),
        modifier = modifier,
    )
}

@Composable
internal fun CardStudyScreenContent(
    modalState: ModalState,
    cardsForRepetitionState: UiState<List<Card>>,
    actions: CardStudyScreenActions,
    modifier: Modifier = Modifier,
) {
    val isDark = isSystemInDarkTheme()
    var currentIndex by remember { mutableIntStateOf(0) }
    val scrollState = rememberScrollState()

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .systemBarsPadding(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            AppTopBar(
                showMenuButton = cardsForRepetitionState is UiState.Success,
                onBackClick = actions.onNavigateBack,
                onMenuClick = actions.onShowDropdownMenu,
                modifier = Modifier.padding(horizontal = dimensionResource(R.dimen.padding_medium)),
            )
        },
        content = { padding ->
            when (cardsForRepetitionState) {
                is UiState.Success -> {
                    val cards = cardsForRepetitionState.data
                    val learnCard = {
                        currentIndex++
                    }
                    LaunchedEffect(currentIndex) { scrollState.scrollTo(0) }

                    Column(
                        modifier = Modifier
                            .padding(padding)
                            .padding(horizontal = dimensionResource(R.dimen.dimen_16))
                            .statusBarsPadding()
                            .verticalScroll(state = scrollState),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        if (cards.isEmpty() || currentIndex >= cards.size) {
                            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.dimen_30)))
                            Text(
                                text = stringResource(R.string.study_session_complete_text),
                                style = MaterialTheme.typography.titleLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                textAlign = TextAlign.Center,
                                modifier = Modifier,
                            )
                        } else {
                            val card = cards[currentIndex]
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
                    }
                    if (cards.isEmpty() || currentIndex >= cards.size) {
                        RepeatButtons(
                            options = listOf(
                                RepeatOptionData(
                                    title = stringResource(R.string.repeat_option_title_repeat_text),
                                    color = if (isDark) repeat_option_again_dark else repeat_option_again_light,
                                    onClick = { learnCard() },
                                ),
                                RepeatOptionData(
                                    title = stringResource(R.string.repeat_option_title_easy_text),
                                    color = if (isDark) repeat_option_easy_dark else repeat_option_easy_light,
                                    onClick = { learnCard() },
                                ),
                                RepeatOptionData(
                                    title = stringResource(R.string.repeat_option_title_medium_text),
                                    color = if (isDark) repeat_option_medium_dark else repeat_option_medium_light,
                                    onClick = { learnCard() },
                                ),
                                RepeatOptionData(
                                    title = stringResource(R.string.repeat_option_title_hard_text),
                                    color = if (isDark) repeat_option_hard_dark else repeat_option_hard_light,
                                    onClick = { learnCard() },
                                ),
                            ),
                        )
                    }

                    AppDropdownMenu(
                        padding = padding,
                        isExpanded = modalState is ModalState.DropdownMenu,
                        onDismiss = actions.onHideModal,
                    ) {
                        AppDropdownMenuItem(
                            text = stringResource(R.string.dropdown_menu_data_previous_card),
                            onClick = {
                                actions.onHideModal()
                                if (currentIndex > 0) {
                                    currentIndex--
                                }
                            },
                        )
                    }
                }

                UiState.Loading -> {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentSize(Alignment.Center),
                    ) {
                        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.dimen_14)))
                        CircularProgressIndicator(
                            color = MaterialTheme.colorScheme.primary,
                            strokeWidth = dimensionResource(R.dimen.dimen_2),
                        )
                    }
                }

                is UiState.Error -> {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.dimen_16)),
                    ) {
                        Text(
                            stringResource(R.string.error_get_card_for_study),
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.error,
                        )
                    }
                }

                UiState.Idle -> Unit
            }
        },
    )
}

@Composable
private fun RepeatButtons(
    options: List<RepeatOptionData>,
) {
    Row(
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.Bottom,
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = dimensionResource(R.dimen.dimen_16))
            .padding(bottom = dimensionResource(R.dimen.dimen_16)),
    ) {
        options.forEach {
            RepeatOptionsButton(
                buttonColor = it.color,
                label = it.title,
                onClick = it.onClick,
                textStyle = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}

data class CardStudyScreenActions(
    val onNavigateBack: () -> Unit,
    val onShowDropdownMenu: () -> Unit,
    val onHideModal: () -> Unit,
)
