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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mindeck.domain.models.Card
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
import com.mindeck.presentation.ui.navigation.CardStudyRoute
import com.mindeck.presentation.ui.navigation.LocalNavigator
import com.mindeck.presentation.ui.theme.MindeckTheme
import com.mindeck.presentation.viewmodel.CardUiEvent
import com.mindeck.presentation.viewmodel.CardViewModel

@Composable
fun CardScreen(
    cardId: Int,
    modifier: Modifier = Modifier,
) {
    val navigator = LocalNavigator.current

    val viewModel = hiltViewModel<CardViewModel>()

    val context = LocalContext.current
    val cardWithDeckState by viewModel.cardWithDeck.collectAsStateWithLifecycle()
    val deleteCardState by viewModel.deleteCardState.collectAsStateWithLifecycle()
    val modalState by viewModel.modalState.collectAsStateWithLifecycle()

    LaunchedEffect(cardId) {
        viewModel.loadCardById(cardId = cardId)
    }

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is CardUiEvent.DeletionSuccessful -> {
                    Toast.makeText(
                        context,
                        context.getString(R.string.card_deleted_success, event.cardName),
                        Toast.LENGTH_SHORT,
                    ).show()
                    navigator.pop()
                }
            }
        }
    }

    CardScreenContent(
        cardWithDeckState = cardWithDeckState,
        deleteCardState = deleteCardState,
        modalState = modalState,
        actions = CardScreenActions(
            onBack = navigator::pop,
            onMenuClick = viewModel::showDropdownMenu,
            onDismissModal = viewModel::hideModal,
            onShowDeleteDialog = viewModel::showDeleteDialog,
            onDeleteCard = viewModel::deleteCard,
            onStudyCard = { navigator.push(CardStudyRoute(it)) },
        ),
        modifier = modifier,
    )
}

@Composable
private fun CardScreenContent(
    cardWithDeckState: UiState<CardWithDeck>,
    deleteCardState: UiState<Unit>,
    modalState: ModalState,
    actions: CardScreenActions,
    modifier: Modifier = Modifier,
) {
    val cardWithDeck = (cardWithDeckState as? UiState.Success)?.data

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .systemBarsPadding(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            AppTopBar(
                showMenuButton = cardWithDeckState is UiState.Success,
                onBackClick = actions.onBack,
                onMenuClick = actions.onMenuClick,
                modifier = Modifier.padding(horizontal = dimensionResource(R.dimen.dimen_16)),
            )
        },
        content = { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .verticalScroll(state = rememberScrollState())
                    .padding(horizontal = dimensionResource(R.dimen.dimen_16)),
            ) {
                CardContent(cardWithDeckState = cardWithDeckState)
            }

            cardWithDeck?.card?.let {
                AppDropdownMenu(
                    padding = padding,
                    isExpanded = modalState is ModalState.DropdownMenu,
                    onDismiss = actions.onDismissModal,
                ) {
                    AppDropdownMenuItem(
                        text = stringResource(R.string.dropdown_menu_data_remove_card),
                        onClick = {
                            actions.onDismissModal()
                            actions.onShowDeleteDialog()
                        },
                    )
                    AppDropdownMenuItem(
                        text = stringResource(R.string.dropdown_menu_data_edit_card),
                        onClick = { },
                    )
                    AppDropdownMenuItem(
                        text = stringResource(R.string.dropdown_menu_data_study_card),
                        onClick = {
                            actions.onDismissModal()
                            actions.onStudyCard(it.cardId)
                        },
                    )
                }
            }
        },
    )

    when (modalState) {
        is ModalState.DeleteDialog -> {
            cardWithDeck?.card?.let {
                DeleteModalWindow(
                    titleText = stringResource(R.string.delete_card_dialog_title),
                    bodyText = stringResource(R.string.delete_card_dialog_body, it.cardName),
                    actionState = deleteCardState,
                    onDeleteClick = {
                        actions.onDeleteCard(it)
                    },
                    onExitClick = actions.onDismissModal,
                )
            }
        }

        else -> Unit
    }
}

@Composable
private fun CardContent(
    cardWithDeckState: UiState<CardWithDeck>,
) {
    when (cardWithDeckState) {
        is UiState.Success -> {
            val cardWithDeck = cardWithDeckState.data
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.dimen_12)))
            CardInfoItem(
                label = stringResource(R.string.text_deck_dropdown_selector),
                value = cardWithDeck.deckName,
            )
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.dimen_12)))
            CardInfoItem(
                label = stringResource(R.string.text_type_dropdown_selector),
                value = when (cardWithDeck.card.cardType) {
                    CardType.SIMPLE -> stringResource(R.string.card_type_simple)
                    CardType.COMPLEX -> stringResource(R.string.card_type_complex)
                },
            )
            Spacer(modifier = Modifier.height(height = dimensionResource(R.dimen.dimen_20)))
            Text(
                text = cardWithDeck.card.cardName,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.height(height = dimensionResource(R.dimen.dimen_12)))
            QuestionAndAnswerElement(
                question = cardWithDeck.card.cardQuestion,
                answer = cardWithDeck.card.cardAnswer,
                questionStyle = MaterialTheme.typography.bodyMedium,
                answerStyle = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = MaterialTheme.shapes.large,
                    )
                    .border(
                        width = dimensionResource(R.dimen.dimen_0_25),
                        color = MaterialTheme.colorScheme.outlineVariant,
                        shape = MaterialTheme.shapes.large,
                    )
                    .padding(horizontal = dimensionResource(R.dimen.dimen_10)),
            )

            if (cardWithDeck.card.cardTag.isNotEmpty()) {
                Spacer(modifier = Modifier.height(height = dimensionResource(R.dimen.dimen_12)))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.dimen_16)),
                ) {
                    Text(
                        text = stringResource(R.string.text_tag_input_field),
                        style = MaterialTheme.typography.bodyMedium,
                    )

                    LazyRow(
                        modifier = Modifier
                            .background(
                                color = MaterialTheme.colorScheme.surfaceVariant,
                                shape = MaterialTheme.shapes.large,
                            )
                            .size(
                                width = dimensionResource(R.dimen.dimen_140),
                                height = dimensionResource(R.dimen.dimen_46),
                            )
                            .border(
                                dimensionResource(R.dimen.dimen_0_25),
                                MaterialTheme.colorScheme.outlineVariant,
                                MaterialTheme.shapes.large,
                            )
                            .padding(horizontal = dimensionResource(R.dimen.dimen_10)),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        item {
                            Text(
                                text = cardWithDeck.card.cardTag,
                                maxLines = 1,
                                style = MaterialTheme.typography.bodyMedium,
                            )
                        }
                    }
                }
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
                Spacer(modifier = Modifier.height(dimensionResource(R.dimen.dimen_40)))
                Text(
                    stringResource(R.string.error_get_info_about_card),
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.error),
                )
                Icon(
                    painter = painterResource(R.drawable.img_error),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error,
                    modifier = Modifier.size(dimensionResource(R.dimen.dimen_36)),
                )
            }
        }

        UiState.Idle -> Unit
    }
}

@Composable
private fun CardInfoItem(
    label: String,
    value: String,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(dimensionResource(R.dimen.dimen_8)),
        )
        Box(
            modifier = Modifier
                .background(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = MaterialTheme.shapes.extraSmall,
                )
                .size(
                    height = dimensionResource(R.dimen.dimen_36),
                    width = dimensionResource(R.dimen.dimen_200),
                )
                .border(
                    dimensionResource(R.dimen.dimen_0_25),
                    color = MaterialTheme.colorScheme.outlineVariant,
                    shape = MaterialTheme.shapes.extraSmall,
                )
                .wrapContentSize(Alignment.Center),
        ) {
            Text(
                text = value,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}

data class CardScreenActions(
    val onBack: () -> Unit,
    val onMenuClick: () -> Unit,
    val onDismissModal: () -> Unit,
    val onShowDeleteDialog: () -> Unit,
    val onDeleteCard: (card: Card) -> Unit,
    val onStudyCard: (cardId: Int) -> Unit,
)

@Preview(showSystemUi = true)
@Composable
private fun CardScreenContentPreview() {
    MindeckTheme {
        CardScreenContent(
            cardWithDeckState = UiState.Success(
                CardWithDeck(card = previewCard, deckId = 1, deckName = "Английский язык"),
            ),
            deleteCardState = UiState.Idle,
            modalState = ModalState.None,
            actions = previewActions,
        )
    }
}

@Preview(showSystemUi = true)
@Composable
private fun CardScreenContentLoadingPreview() {
    MindeckTheme {
        CardScreenContent(
            cardWithDeckState = UiState.Loading,
            deleteCardState = UiState.Idle,
            modalState = ModalState.None,
            actions = previewActions,
        )
    }
}

@Preview(showSystemUi = true)
@Composable
private fun CardScreenContentErrorPreview() {
    MindeckTheme {
        CardScreenContent(
            cardWithDeckState = UiState.Error(R.string.error_get_info_about_card),
            deleteCardState = UiState.Idle,
            modalState = ModalState.None,
            actions = previewActions,
        )
    }
}

private val previewActions = CardScreenActions(
    onBack = {},
    onMenuClick = {},
    onDismissModal = {},
    onShowDeleteDialog = {},
    onDeleteCard = {},
    onStudyCard = {},
)

private val previewCard = Card(
    cardName = "Kotlin корутины",
    cardQuestion = "Что такое coroutine?",
    cardAnswer = "Лёгковесная сопрограмма для асинхронного кода в Kotlin",
    cardType = CardType.SIMPLE,
    cardTag = "kotlin",
    deckId = 1,
)
