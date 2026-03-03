package com.mindeck.presentation.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.mindeck.domain.models.CardType
import com.mindeck.domain.models.CardWithDeck
import com.mindeck.presentation.R
import com.mindeck.presentation.state.ModalState
import com.mindeck.presentation.state.UiState
import com.mindeck.presentation.ui.components.common.QuestionAndAnswerElement
import com.mindeck.presentation.ui.components.dialog.DeleteModalWindow
import com.mindeck.presentation.ui.components.dropdown.AppDropdownMenu
import com.mindeck.presentation.ui.components.dropdown.AppDropdownMenuItem
import com.mindeck.presentation.ui.components.topBar.AppTopBar
import com.mindeck.presentation.ui.components.utils.dimenDpResource
import com.mindeck.presentation.ui.navigation.Navigator
import com.mindeck.presentation.ui.theme.text_black
import com.mindeck.presentation.viewmodel.CardUiEvent
import com.mindeck.presentation.viewmodel.CardViewModel

@Composable
fun CardScreen(
    navigator: Navigator,
    cardId: Int,
    modifier: Modifier = Modifier,
) {
    CardScreenContent(
        navigator = navigator,
        cardId = cardId,
        cardViewModel = hiltViewModel<CardViewModel>(),
        modifier = modifier,
    )
}

@Composable
private fun CardScreenContent(
    navigator: Navigator,
    cardId: Int,
    cardViewModel: CardViewModel,
    modifier: Modifier = Modifier,
) {
    val cardWithDeckState by cardViewModel.cardWithDeck.collectAsState()

    val cardWithDeck = (cardWithDeckState as? UiState.Success)?.data
    val card = cardWithDeck?.card
    val deckName = cardWithDeck?.deckName

    val modalState by cardViewModel.modalState.collectAsState()

    val context = LocalContext.current

    val isLoading = cardWithDeckState is UiState.Loading

    val deckDisplayText = when (cardWithDeckState) {
        is UiState.Success -> deckName ?: "..."
        else -> "..."
    }

    val cardTypeDisplayText = when (cardWithDeckState) {
        is UiState.Success -> card?.cardType?.let {
            when (it) {
                CardType.SIMPLE -> stringResource(R.string.card_type_simple)
                CardType.COMPLEX -> stringResource(R.string.card_type_complex)
            }
        } ?: "..."

        else -> "..."
    }

    LaunchedEffect(cardId) {
        cardViewModel.loadCardById(cardId = cardId)
    }

    LaunchedEffect(Unit) {
        cardViewModel.uiEvent.collect { event ->
            when (event) {
                is CardUiEvent.DeletionSuccessful -> {
                    Toast.makeText(
                        context,
                        "Card \"${event.cardName}\" deleted successfully",
                        Toast.LENGTH_SHORT,
                    ).show()
                    navigator.pop()
                }
            }
        }
    }

    val scrollState = rememberScrollState()

    Scaffold(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            AppTopBar(
                visibleMenuButton = cardWithDeckState is UiState.Success,
                onBackClick = { navigator.pop() },
                onMenuClick = { cardViewModel.showDropdownMenu() },
                modifier = Modifier.padding(horizontal = dimensionResource(R.dimen.padding_medium)),
            )
        },
        content = { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .verticalScroll(state = scrollState)
                    .padding(horizontal = dimenDpResource(R.dimen.padding_medium))
                    .navigationBarsPadding(),
            ) {
                Spacer(modifier = Modifier.height(dimenDpResource(R.dimen.spacer_medium)))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        text = stringResource(R.string.text_deck_dropdown_selector),
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(dimensionResource(R.dimen.dimen_8)),
                    )

                    Box(
                        modifier = Modifier
                            .background(
                                color = MaterialTheme.colorScheme.onPrimary,
                                shape = MaterialTheme.shapes.extraSmall,
                            )
                            .size(
                                height = dimensionResource(R.dimen.dimen_36),
                                width = dimensionResource(R.dimen.dimen_200),
                            )
                            .border(
                                dimensionResource(R.dimen.dimen_0_25),
                                color = MaterialTheme.colorScheme.outline,
                                shape = MaterialTheme.shapes.extraSmall,
                            )
                            .wrapContentSize(Alignment.Center),
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(dimensionResource(R.dimen.dimen_20)),
                                strokeWidth = dimensionResource(R.dimen.dimen_2),
                            )
                        } else {
                            Text(
                                text = deckDisplayText,
                                overflow = TextOverflow.Ellipsis,
                                style = MaterialTheme.typography.bodyMedium.copy(color = text_black),
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(dimenDpResource(R.dimen.spacer_medium)))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        text = stringResource(R.string.text_type_dropdown_selector),
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(dimensionResource(R.dimen.dimen_8)),
                    )

                    Box(
                        modifier = Modifier
                            .background(
                                color = MaterialTheme.colorScheme.onPrimary,
                                shape = MaterialTheme.shapes.extraSmall,
                            )
                            .size(
                                height = dimensionResource(R.dimen.dimen_36),
                                width = dimensionResource(R.dimen.dimen_200),
                            )
                            .border(
                                dimensionResource(R.dimen.dimen_0_25),
                                color = MaterialTheme.colorScheme.outline,
                                shape = MaterialTheme.shapes.extraSmall,
                            )
                            .wrapContentSize(Alignment.Center),
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(dimensionResource(R.dimen.dimen_20)),
                                strokeWidth = dimensionResource(R.dimen.dimen_2),
                            )
                        } else {
                            Text(
                                text = cardTypeDisplayText,
                                overflow = TextOverflow.Ellipsis,
                                style = MaterialTheme.typography.bodyMedium.copy(color = text_black),
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(height = dimenDpResource(R.dimen.spacer_large)))
                DeckSuccessState(cardWithDeckState = cardWithDeckState)
            }
            card?.let {
                AppDropdownMenu(
                    padding = padding,
                    isExpanded = modalState is ModalState.DropdownMenu,
                    onDismiss = { cardViewModel.hideModal() },
                ) {
                    AppDropdownMenuItem(
                        text = stringResource(R.string.dropdown_menu_data_remove_card),
                        onClick = {
                            cardViewModel.hideModal()
                            cardViewModel.showDeleteDialog()
                        },
                    )
                    AppDropdownMenuItem(
                        text = stringResource(R.string.dropdown_menu_data_edit_card),
                        onClick = { },
                    )
                    AppDropdownMenuItem(
                        text = stringResource(R.string.dropdown_menu_data_study_card),
                        onClick = { },
                    )
                }
            }
        },
    )

    when (modalState) {
        is ModalState.DeleteDialog -> {
            card?.let {
                DeleteModalWindow(
                    titleText = stringResource(R.string.delete_card_dialog_title),
                    bodyText = stringResource(R.string.delete_card_dialog_body, it.cardName),
                    deleteButton = {
                        cardViewModel.hideModal()
                        cardViewModel.deleteCard(it)
                    },
                    onExitClick = { cardViewModel.hideModal() },
                )
            }
        }

        else -> Unit
    }
}

@Composable
private fun DeckSuccessState(
    cardWithDeckState: UiState<CardWithDeck>,
) {
    when (cardWithDeckState) {
        is UiState.Success -> {
            val card = cardWithDeckState.data.card
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(
                    text = card.cardName,
                    style = MaterialTheme.typography.bodyLarge,
                )
            }
            Spacer(modifier = Modifier.height(height = dimenDpResource(R.dimen.spacer_medium)))
            Box(
                modifier = Modifier.fillMaxSize(),
            ) {
                QuestionAndAnswerElement(
                    question = card.cardQuestion,
                    answer = card.cardAnswer,
                    questionStyle = MaterialTheme.typography.bodyMedium.copy(
                        textAlign = TextAlign.Start,
                    ),
                    answerStyle = MaterialTheme.typography.bodyMedium.copy(
                        textAlign = TextAlign.Start,
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = MaterialTheme.colorScheme.onPrimary,
                            shape = MaterialTheme.shapes.large,
                        )
                        .border(
                            width = dimensionResource(R.dimen.dimen_0_25),
                            color = MaterialTheme.colorScheme.outline,
                            shape = MaterialTheme.shapes.large,
                        )
                        .padding(horizontal = dimensionResource(R.dimen.dimen_10)),
                )
            }

            if (card.cardTag.isNotEmpty()) {
                Spacer(modifier = Modifier.height(height = dimenDpResource(R.dimen.spacer_medium)))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.dimen_16)),
                ) {
                    Text(
                        text = stringResource(R.string.text_tag_input_field),
                        style = MaterialTheme.typography.bodyMedium,
                    )
                    Box(
                        modifier = Modifier
                            .background(
                                MaterialTheme.colorScheme.onPrimary,
                                MaterialTheme.shapes.large,
                            )
                            .size(
                                width = dimensionResource(R.dimen.dimen_140),
                                height = dimensionResource(R.dimen.dimen_46),
                            )
                            .border(
                                dimensionResource(R.dimen.dimen_0_25),
                                MaterialTheme.colorScheme.outline,
                                MaterialTheme.shapes.large,
                            )
                            .padding(horizontal = dimensionResource(R.dimen.dimen_10)),
                        contentAlignment = Alignment.CenterStart,
                    ) {
                        Text(
                            text = card.cardTag,
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    }
                }
            }
        }

        is UiState.Loading -> {
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
        }

        is UiState.Error -> {
            Text(
                stringResource(R.string.error_get_info_about_deck),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.error),
            )
        }

        is UiState.Idle -> Unit
    }
}
