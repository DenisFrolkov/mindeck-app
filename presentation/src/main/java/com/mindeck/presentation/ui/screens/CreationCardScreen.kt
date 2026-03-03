package com.mindeck.presentation.ui.screens

import android.widget.Toast
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.mindeck.domain.models.CardType
import com.mindeck.domain.models.Deck
import com.mindeck.presentation.R
import com.mindeck.presentation.state.CreateCardFormState
import com.mindeck.presentation.state.ModalState
import com.mindeck.presentation.state.UiState
import com.mindeck.presentation.ui.components.dialog.ChooseModalWindow
import com.mindeck.presentation.ui.components.textfields.CardInputField
import com.mindeck.presentation.ui.components.topBar.AppTopBar
import com.mindeck.presentation.ui.navigation.Navigator
import com.mindeck.presentation.ui.theme.text_black
import com.mindeck.presentation.ui.theme.text_gray
import com.mindeck.presentation.viewmodel.CreationCardNavigationEvent
import com.mindeck.presentation.viewmodel.CreationCardViewModel

@Composable
fun CreationCardScreen(
    navigator: Navigator,
    deckId: Int?,
    modifier: Modifier = Modifier,
) {
    CreationCardScreenContent(
        navigator = navigator,
        deckId = deckId,
        viewModel = hiltViewModel<CreationCardViewModel>(),
        modifier = modifier,
    )
}

@Composable
fun CreationCardScreenContent(
    navigator: Navigator,
    deckId: Int?,
    viewModel: CreationCardViewModel,
    modifier: Modifier = Modifier,
) {
    val formState by viewModel.formState.collectAsState()
    val deckState by viewModel.deckSelectionHandler.decksState.collectAsState()
    val createCardState by viewModel.createCardState.collectAsState()
    val modalState by viewModel.modalState.collectAsState()

    val context = LocalContext.current

    LaunchedEffect(deckId) {
        deckId?.let { viewModel.setDeckId(it) }
    }

    LaunchedEffect(Unit) {
        viewModel.navigationEvent.collect { event ->
            when (event) {
                is CreationCardNavigationEvent.ShowToast -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }
                CreationCardNavigationEvent.ToBack -> { navigator.pop() }
            }
        }
    }

    Scaffold(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            AppTopBar(
                onBackClick = { navigator.pop() },
                modifier = Modifier.padding(horizontal = dimensionResource(R.dimen.dimen_16)),
            )
        },
        content = { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = dimensionResource(R.dimen.dimen_16))
                    .navigationBarsPadding(),
            ) {
                DeckSelector(
                    selectedDeckId = formState.selectedDeckId,
                    deckState = deckState,
                    onDeckSelectorClick = { viewModel.showDeckModal() },
                )

                Spacer(modifier = Modifier.height(dimensionResource(R.dimen.dimen_8)))

                TypeSelector(
                    selectedType = formState.selectedType,
                    onTypeSelectorClick = { viewModel.showTypeModal() },
                )

                Spacer(modifier = Modifier.height(dimensionResource(R.dimen.dimen_12)))

                CardFormInputs(
                    formState = formState,
                    onValueChange = { update -> viewModel.updateForm(update) },
                )

                Spacer(modifier = Modifier.height(dimensionResource(R.dimen.dimen_20)))

                CreateCardButton(
                    buttonText = stringResource(R.string.text_save_card_button),
                    isValid = formState.isValid(),
                    isLoading = createCardState is UiState.Loading,
                    onSaveClick = { viewModel.createCard() },
                )

                if (createCardState is UiState.Error) {
                    Spacer(modifier = Modifier.height(dimensionResource(R.dimen.dimen_8)))
                    Text(
                        text = (createCardState as UiState.Error).error,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.error,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            }
        },
    )

    when (modalState) {
        is ModalState.DeckSelection -> {
            DeckSelectionModal(
                viewModel = viewModel,
                deckState = deckState,
                selectedDeckId = formState.selectedDeckId,
            )
        }
        is ModalState.TypeSelection -> {
            TypeSelectionModal(
                selectedType = formState.selectedType,
                onDismiss = { viewModel.hideModal() },
                onTypeSelected = { cardType ->
                    viewModel.hideModal()
                    viewModel.setType(cardType)
                },
            )
        }

        else -> Unit
    }
}

@Composable
private fun DeckSelector(
    selectedDeckId: Int?,
    deckState: UiState<List<Deck>>,
    onDeckSelectorClick: () -> Unit,
) {
    val selectedDeckName = remember(deckState, selectedDeckId) {
        selectedDeckId?.let { id ->
            (deckState as? UiState.Success)?.data?.find { it.deckId == id }?.deckName
        }
    }

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
                .clickable(onClick = onDeckSelectorClick)
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
            Text(
                text = selectedDeckName ?: stringResource(R.string.choose_deck_title_dialog),
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodyMedium.copy(color = text_black),
            )
        }
    }
}

@Composable
private fun TypeSelector(
    selectedType: CardType?,
    onTypeSelectorClick: () -> Unit,
) {
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
                .clickable(onClick = onTypeSelectorClick)
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
            Text(
                text = selectedType?.let { cardType ->
                    when (cardType) {
                        CardType.SIMPLE -> stringResource(R.string.card_type_simple)
                        CardType.COMPLEX -> stringResource(R.string.card_type_complex)
                    }
                } ?: stringResource(R.string.choose_type_title_dialog),
                style = MaterialTheme.typography.bodyMedium.copy(color = text_black),
            )
        }
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
        textStyle = MaterialTheme.typography.bodyMedium,
        placeholderTextStyle = MaterialTheme.typography.bodyMedium.copy(text_gray),
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.onPrimary,
                shape = MaterialTheme.shapes.large,
            )
            .height(height = dimensionResource(R.dimen.dimen_46))
            .border(
                width = dimensionResource(R.dimen.dimen_0_25),
                color = MaterialTheme.colorScheme.outline,
                shape = MaterialTheme.shapes.large,
            )
            .padding(horizontal = dimensionResource(R.dimen.dimen_10)),
    )

    Spacer(modifier = Modifier.height(dimensionResource(R.dimen.dimen_12)))

    CardInputField(
        placeholder = stringResource(R.string.enter_question_for_card),
        value = formState.question,
        onValueChange = { onValueChange { copy(question = it) } },
        textStyle = MaterialTheme.typography.bodyMedium,
        placeholderTextStyle = MaterialTheme.typography.bodyMedium.copy(text_gray),
        modifier = Modifier
            .background(
                color = MaterialTheme.colorScheme.onPrimary,
                shape = RoundedCornerShape(
                    topStart = dimensionResource(R.dimen.dimen_6),
                    topEnd = dimensionResource(R.dimen.dimen_6),
                ),
            )
            .heightIn(
                min = dimensionResource(R.dimen.dimen_46),
                max = dimensionResource(R.dimen.dimen_200),
            )
            .border(
                width = dimensionResource(R.dimen.dimen_0_25),
                color = MaterialTheme.colorScheme.outline,
                shape = RoundedCornerShape(
                    topStart = dimensionResource(R.dimen.dimen_6),
                    topEnd = dimensionResource(R.dimen.dimen_6),
                ),
            )
            .padding(horizontal = dimensionResource(R.dimen.dimen_10)),
    )

    CardInputField(
        placeholder = stringResource(R.string.enter_answer_for_card),
        value = formState.answer,
        onValueChange = { onValueChange { copy(answer = it) } },
        textStyle = MaterialTheme.typography.bodyMedium,
        placeholderTextStyle = MaterialTheme.typography.bodyMedium.copy(text_gray),
        modifier = Modifier
            .background(
                color = MaterialTheme.colorScheme.onPrimary,
                shape = RoundedCornerShape(
                    bottomStart = dimensionResource(R.dimen.dimen_6),
                    bottomEnd = dimensionResource(R.dimen.dimen_6),
                ),
            )
            .heightIn(
                min = dimensionResource(R.dimen.dimen_46),
                max = dimensionResource(R.dimen.dimen_200),
            )
            .border(
                width = dimensionResource(R.dimen.dimen_0_25),
                color = MaterialTheme.colorScheme.outline,
                shape = RoundedCornerShape(
                    bottomStart = dimensionResource(R.dimen.dimen_6),
                    bottomEnd = dimensionResource(R.dimen.dimen_6),
                ),
            )
            .padding(horizontal = dimensionResource(R.dimen.dimen_10)),
    )

    Spacer(modifier = Modifier.height(dimensionResource(R.dimen.dimen_12)))

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.dimen_16)),
    ) {
        Text(
            text = stringResource(R.string.text_tag_input_field),
            style = MaterialTheme.typography.bodyMedium,
        )
        CardInputField(
            value = formState.tag,
            singleLine = true,
            onValueChange = { onValueChange { copy(tag = it) } },
            textStyle = MaterialTheme.typography.bodyMedium,
            placeholderTextStyle = MaterialTheme.typography.bodyMedium.copy(text_gray),
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
        )
    }
}

@Composable
private fun DeckSelectionModal(
    viewModel: CreationCardViewModel,
    deckState: UiState<List<Deck>>,
    selectedDeckId: Int?,
) {
    val createDeckState by viewModel.deckSelectionHandler.createDeckState.collectAsState()

    ChooseModalWindow(
        titleText = stringResource(R.string.choose_deck_title_dialog),
        chooseObject = (deckState as? UiState.Success)?.data?.map { Pair(it.deckName, it.deckId) },
        isLoading = createDeckState is UiState.Loading,
        errorMsg = (createDeckState as? UiState.Error)?.error,
        onExitClick = { viewModel.hideModal() },
        onSaveClick = { deckId ->
            viewModel.hideModal()
            viewModel.setDeckId(deckId)
        },
        selectedId = selectedDeckId,
        showAddIcon = true,
        createTitleText = stringResource(R.string.create_item_dialog_text_input_name_deck),
        createButtonText = stringResource(R.string.save_text),
        createPlaceholder = stringResource(R.string.rename_item_dialog_text_input_title_deck),
        onCreateClick = { deckName -> viewModel.createDeck(deckName) },
    )
}

@Composable
private fun TypeSelectionModal(
    selectedType: CardType?,
    onDismiss: () -> Unit,
    onTypeSelected: (CardType) -> Unit,
) {
    val cardTypes = listOf(
        Pair(stringResource(R.string.card_type_simple), CardType.SIMPLE),
        Pair(stringResource(R.string.card_type_complex), CardType.COMPLEX),
    )

    ChooseModalWindow(
        titleText = stringResource(R.string.choose_type_title_dialog),
        chooseObject = cardTypes.map { Pair(it.first, it.second.stableId) },
        isLoading = false,
        errorMsg = null,
        selectedId = selectedType?.stableId,
        onExitClick = onDismiss,
        onSaveClick = { id -> onTypeSelected(CardType.fromStableId(id)) },
    )
}

@Composable
private fun CreateCardButton(
    buttonText: String,
    isValid: Boolean,
    isLoading: Boolean,
    onSaveClick: () -> Unit,
) {
    val animatedButtonColor by animateColorAsState(
        targetValue = when {
            !isValid -> MaterialTheme.colorScheme.secondary
            else -> MaterialTheme.colorScheme.onSecondary
        },
        animationSpec = tween(300),
        label = "buttonColor",
    )

    val animatedTextColor by animateColorAsState(
        targetValue = when {
            !isValid -> MaterialTheme.colorScheme.outline
            isLoading -> MaterialTheme.colorScheme.onSecondaryContainer
            else -> MaterialTheme.colorScheme.scrim
        },
        animationSpec = tween(300),
        label = "textColor",
    )

    AnimatedContent(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentSize(Alignment.Center),
        targetState = isLoading,
    ) { target ->
        Box(
            modifier = Modifier
                .background(
                    color = animatedButtonColor,
                    shape = MaterialTheme.shapes.medium,
                )
                .then(
                    if (!isValid) {
                        Modifier
                    } else {
                        Modifier.clickable {
                            onSaveClick()
                        }
                    },
                ),
        ) {
            Text(
                text = if (target) stringResource(R.string.loading_text) else buttonText,
                style = MaterialTheme.typography.bodyMedium,
                color = animatedTextColor,
                modifier = Modifier.padding(
                    vertical = dimensionResource(R.dimen.dimen_12),
                    horizontal = dimensionResource(R.dimen.dimen_32),
                ),
            )
        }
    }
}
