package com.mindeck.presentation.ui.screens

import android.widget.Toast
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mindeck.domain.models.CardType
import com.mindeck.domain.models.Deck
import com.mindeck.presentation.R
import com.mindeck.presentation.state.CreateCardFormState
import com.mindeck.presentation.state.ModalState
import com.mindeck.presentation.state.UiState
import com.mindeck.presentation.ui.components.buttons.CustomButton
import com.mindeck.presentation.ui.components.dialog.ChooseModalWindow
import com.mindeck.presentation.ui.components.selector.SelectorRow
import com.mindeck.presentation.ui.components.textfields.CardInputField
import com.mindeck.presentation.ui.components.topBar.AppTopBar
import com.mindeck.presentation.ui.navigation.LocalNavigator
import com.mindeck.presentation.ui.theme.MindeckTheme
import com.mindeck.presentation.viewmodel.CreationCardNavigationEvent
import com.mindeck.presentation.viewmodel.CreationCardViewModel

@Composable
fun CreationCardScreen(
    deckId: Int?,
    modifier: Modifier = Modifier,
) {
    val navigator = LocalNavigator.current

    val context = LocalContext.current
    val viewModel = hiltViewModel<CreationCardViewModel>()
    val formState by viewModel.formState.collectAsStateWithLifecycle()
    val deckState by viewModel.deckSelectionHandler.decksState.collectAsStateWithLifecycle()
    val createDeckState by viewModel.deckSelectionHandler.createDeckState.collectAsStateWithLifecycle()
    val createCardState by viewModel.createCardState.collectAsStateWithLifecycle()
    val modalState by viewModel.modalState.collectAsStateWithLifecycle()

    LaunchedEffect(deckId) {
        deckId?.let { viewModel.setDeckId(it) }
    }

    LaunchedEffect(Unit) {
        viewModel.navigationEvent.collect { event ->
            when (event) {
                is CreationCardNavigationEvent.ShowToast -> {
                    Toast.makeText(context, context.getString(event.messageRes), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    CreationCardScreenContent(
        formState = formState,
        deckState = deckState,
        createDeckState = createDeckState,
        createCardState = createCardState,
        modalState = modalState,
        actions = CreationCardScreenActions(
            onNavigateBack = navigator::pop,
            onShowDeckModal = viewModel::showDeckModal,
            onShowTypeModal = viewModel::showTypeModal,
            onUpdateForm = viewModel::updateForm,
            onCreateCard = viewModel::createCard,
            onHideModal = viewModel::hideModal,
            onSetDeckId = viewModel::setDeckId,
            onSetType = viewModel::setType,
            onCreateDeck = viewModel::createDeck,
        ),
        modifier = modifier,
    )
}

@Composable
internal fun CreationCardScreenContent(
    formState: CreateCardFormState,
    deckState: UiState<List<Deck>>,
    createDeckState: UiState<Unit>,
    createCardState: UiState<Unit>,
    modalState: ModalState,
    actions: CreationCardScreenActions,
    modifier: Modifier = Modifier,
) {
    val cardTypes = listOf(
        Pair(stringResource(R.string.card_type_simple), CardType.SIMPLE),
        Pair(stringResource(R.string.card_type_complex), CardType.COMPLEX),
    )

    val selectedDeckName = remember(deckState, formState.selectedDeckId) {
        formState.selectedDeckId?.let { id ->
            (deckState as? UiState.Success)?.data?.find { it.deckId == id }?.deckName
        }
    }

    val selectedTypeName = formState.selectedType?.let { cardType ->
        when (cardType) {
            CardType.SIMPLE -> stringResource(R.string.card_type_simple)
            CardType.COMPLEX -> stringResource(R.string.card_type_complex)
        }
    } ?: stringResource(R.string.choose_type_title_dialog)

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .systemBarsPadding(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            AppTopBar(
                onBackClick = actions.onNavigateBack,
                modifier = Modifier.padding(horizontal = MindeckTheme.dimensions.paddingMd),
            )
        },
        content = { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = MindeckTheme.dimensions.paddingMd),
            ) {
                SelectorRow(
                    label = stringResource(R.string.text_deck_dropdown_selector),
                    selectedText = selectedDeckName
                        ?: stringResource(R.string.choose_deck_title_dialog),
                    onClick = actions.onShowDeckModal,
                )

                Spacer(modifier = Modifier.height(MindeckTheme.dimensions.spacerSm))

                SelectorRow(
                    label = stringResource(R.string.text_type_dropdown_selector),
                    selectedText = selectedTypeName,
                    onClick = actions.onShowTypeModal,
                )

                Spacer(modifier = Modifier.height(MindeckTheme.dimensions.spacerSm))

                CardFormInputs(
                    formState = formState,
                    onValueChange = actions.onUpdateForm,
                )

                Spacer(modifier = Modifier.height(MindeckTheme.dimensions.spacerLg))

                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    CreateCardButton(
                        text = stringResource(R.string.save_text),
                        isValid = formState.isValid(),
                        actionState = createCardState,
                        onSaveClick = actions.onCreateCard,
                    )
                }
            }
        },
    )

    when (modalState) {
        is ModalState.DeckSelection -> {
            ChooseModalWindow(
                titleText = stringResource(R.string.choose_deck_title_dialog),
                items = (deckState as? UiState.Success)?.data?.map {
                    Pair(
                        it.deckName,
                        it.deckId,
                    )
                },
                actionState = createDeckState,
                onExitClick = actions.onHideModal,
                onSaveClick = actions.onSetDeckId,
                selectedId = formState.selectedDeckId,
                showAddIcon = true,
                createTitle = stringResource(R.string.create_item_dialog_text_input_name_deck),
                createButtonLabel = stringResource(R.string.save_text),
                createPlaceholder = stringResource(R.string.rename_item_dialog_text_input_title_deck),
                onCreateClick = actions.onCreateDeck,
            )
        }

        is ModalState.TypeSelection -> {
            ChooseModalWindow(
                titleText = stringResource(R.string.choose_type_title_dialog),
                items = cardTypes.map { Pair(it.first, it.second.stableId) },
                actionState = UiState.Idle,
                selectedId = formState.selectedType?.stableId,
                onExitClick = actions.onHideModal,
                onSaveClick = { id -> actions.onSetType(CardType.fromStableId(id)) },
            )
        }

        else -> Unit
    }
}

@Composable
private fun CardFormInputs(
    formState: CreateCardFormState,
    onValueChange: (CreateCardFormState.() -> CreateCardFormState) -> Unit,
) {
    CardInputField(
        placeholder = stringResource(R.string.enter_name_for_card),
        value = formState.title,
        singleLine = true,
        onValueChange = { onValueChange { copy(title = it) } },
        placeholderTextStyle = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onSurfaceVariant),
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = MaterialTheme.shapes.large,
            )
            .height(height = MindeckTheme.dimensions.dp46)
            .border(
                width = MindeckTheme.dimensions.dp0_25,
                color = MaterialTheme.colorScheme.outlineVariant,
                shape = MaterialTheme.shapes.large,
            )
            .padding(horizontal = MindeckTheme.dimensions.paddingSm),
    )

    Spacer(modifier = Modifier.height(MindeckTheme.dimensions.spacerMd))

    CardInputField(
        placeholder = stringResource(R.string.enter_question_for_card),
        value = formState.question,
        onValueChange = { onValueChange { copy(question = it) } },
        placeholderTextStyle = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onSurfaceVariant),
        modifier = Modifier
            .background(
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = RoundedCornerShape(
                    topStart = MindeckTheme.dimensions.dp6,
                    topEnd = MindeckTheme.dimensions.dp6,
                ),
            )
            .heightIn(
                min = MindeckTheme.dimensions.dp46,
                max = MindeckTheme.dimensions.dp200,
            )
            .border(
                width = MindeckTheme.dimensions.dp0_25,
                color = MaterialTheme.colorScheme.outlineVariant,
                shape = RoundedCornerShape(
                    topStart = MindeckTheme.dimensions.dp6,
                    topEnd = MindeckTheme.dimensions.dp6,
                ),
            )
            .padding(horizontal = MindeckTheme.dimensions.paddingSm),
    )

    CardInputField(
        placeholder = stringResource(R.string.enter_answer_for_card),
        value = formState.answer,
        onValueChange = { onValueChange { copy(answer = it) } },
        placeholderTextStyle = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onSurfaceVariant),
        modifier = Modifier
            .background(
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = RoundedCornerShape(
                    bottomStart = MindeckTheme.dimensions.dp6,
                    bottomEnd = MindeckTheme.dimensions.dp6,
                ),
            )
            .heightIn(
                min = MindeckTheme.dimensions.dp46,
                max = MindeckTheme.dimensions.dp200,
            )
            .border(
                width = MindeckTheme.dimensions.dp0_25,
                color = MaterialTheme.colorScheme.outlineVariant,
                shape = RoundedCornerShape(
                    bottomStart = MindeckTheme.dimensions.dp6,
                    bottomEnd = MindeckTheme.dimensions.dp6,
                ),
            )
            .padding(horizontal = MindeckTheme.dimensions.paddingSm),
    )

    Spacer(modifier = Modifier.height(MindeckTheme.dimensions.spacerMd))

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(MindeckTheme.dimensions.dp16),
    ) {
        Text(
            text = stringResource(R.string.text_tag_input_field),
            style = MaterialTheme.typography.bodyMedium,
        )
        CardInputField(
            value = formState.tag,
            singleLine = true,
            onValueChange = { onValueChange { copy(tag = it) } },
            placeholderTextStyle = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onSurfaceVariant),
            modifier = Modifier
                .background(
                    MaterialTheme.colorScheme.surfaceVariant,
                    MaterialTheme.shapes.large,
                )
                .size(
                    width = MindeckTheme.dimensions.dp140,
                    height = MindeckTheme.dimensions.dp46,
                )
                .border(
                    MindeckTheme.dimensions.dp0_25,
                    MaterialTheme.colorScheme.outlineVariant,
                    MaterialTheme.shapes.large,
                )
                .padding(horizontal = MindeckTheme.dimensions.paddingSm),
        )
    }
}

@Composable
private fun CreateCardButton(
    text: String,
    isValid: Boolean,
    actionState: UiState<Unit>,
    onSaveClick: () -> Unit,
) {
    val isLoading = actionState is UiState.Loading
    val errorState = actionState as? UiState.Error

    val animatedButtonColor by animateColorAsState(
        targetValue = when {
            !isValid -> MaterialTheme.colorScheme.surfaceVariant
            else -> MaterialTheme.colorScheme.primary
        },
        animationSpec = tween(DURATION_300),
        label = "buttonColor",
    )

    val animatedTextColor by animateColorAsState(
        targetValue = when {
            !isValid -> MaterialTheme.colorScheme.onSurfaceVariant
            isLoading -> MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.6f)
            else -> MaterialTheme.colorScheme.onPrimary
        },
        animationSpec = tween(DURATION_300),
        label = "textColor",
    )

    CustomButton(
        text = if (isLoading) stringResource(R.string.loading_text) else text,
        color = animatedButtonColor,
        textColor = animatedTextColor,
        onClick = { if (!isLoading && isValid) onSaveClick() },
        modifier = Modifier.size(
            height = MindeckTheme.dimensions.dp42,
            width = MindeckTheme.dimensions.dp140,
        ),
    )

    errorState?.let { error ->
        Text(
            text = stringResource(error.messageRes, *error.args.toTypedArray()),
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = MindeckTheme.dimensions.dp4,
                ),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.error,
        )
    }
}

const val DURATION_300 = 300

data class CreationCardScreenActions(
    val onNavigateBack: () -> Unit,
    val onShowDeckModal: () -> Unit,
    val onShowTypeModal: () -> Unit,
    val onUpdateForm: (CreateCardFormState.() -> CreateCardFormState) -> Unit,
    val onCreateCard: () -> Unit,
    val onHideModal: () -> Unit,
    val onSetDeckId: (Int) -> Unit,
    val onSetType: (CardType) -> Unit,
    val onCreateDeck: (String) -> Unit,
)

@Preview(showSystemUi = true)
@Composable
private fun CreationCardScreenContentEmptyPreview() {
    MindeckTheme {
        CreationCardScreenContent(
            formState = CreateCardFormState(),
            deckState = UiState.Success(
                listOf(
                    Deck(deckId = 1, deckName = "Английский язык"),
                    Deck(deckId = 2, deckName = "Математика"),
                ),
            ),
            createDeckState = UiState.Idle,
            createCardState = UiState.Idle,
            modalState = ModalState.None,
            actions = previewActions,
        )
    }
}

@Preview(showSystemUi = true)
@Composable
private fun CreationCardScreenContentFilledPreview() {
    MindeckTheme {
        CreationCardScreenContent(
            formState = CreateCardFormState(
                title = "Kotlin корутины",
                question = "Что такое coroutine?",
                answer = "Лёгковесная сопрограмма для асинхронного кода",
                selectedDeckId = 1,
                selectedType = CardType.SIMPLE,
            ),
            deckState = UiState.Success(
                listOf(Deck(deckId = 1, deckName = "Английский язык")),
            ),
            createDeckState = UiState.Idle,
            createCardState = UiState.Idle,
            modalState = ModalState.None,
            actions = previewActions,
        )
    }
}

private val previewActions = CreationCardScreenActions(
    onNavigateBack = {},
    onShowDeckModal = {},
    onShowTypeModal = {},
    onUpdateForm = {},
    onCreateCard = {},
    onHideModal = {},
    onSetDeckId = {},
    onSetType = {},
    onCreateDeck = {},
)
