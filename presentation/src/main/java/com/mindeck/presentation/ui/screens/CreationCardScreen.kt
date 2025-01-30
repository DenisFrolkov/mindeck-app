package com.mindeck.presentation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
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
import androidx.compose.ui.text.TextStyle
import androidx.navigation.NavController
import com.mindeck.domain.models.Card
import com.mindeck.domain.models.Deck
import com.mindeck.domain.models.Folder
import com.mindeck.presentation.R
import com.mindeck.presentation.state.CardState
import com.mindeck.presentation.state.DropdownState
import com.mindeck.presentation.ui.components.buttons.ActionHandlerButton
import com.mindeck.presentation.ui.components.buttons.SaveDataButton
import com.mindeck.presentation.ui.components.dropdown.dropdown_selector.DropdownSelector
import com.mindeck.presentation.ui.components.textfields.CardInputField
import com.mindeck.presentation.ui.components.textfields.TegInputField
import com.mindeck.presentation.ui.components.textfields.TitleInputField
import com.mindeck.presentation.ui.components.utils.dimenDpResource
import com.mindeck.presentation.ui.components.utils.textInputModifier
import com.mindeck.presentation.ui.theme.text_gray
import com.mindeck.presentation.ui.theme.text_white
import com.mindeck.presentation.state.UiState
import com.mindeck.presentation.state.UiState.Loading.mapToUiState
import com.mindeck.presentation.viewmodel.CreationCardViewModel

@Composable
fun CreationCardScreen(
    navController: NavController,
    creationCardViewModel: CreationCardViewModel
) {
    val typeDropdownList = listOf(
        Pair(stringResource(R.string.text_folder_dropdown_selector_simple), 0),
        Pair(stringResource(R.string.text_folder_dropdown_selector_simple_with_answer_input), 1)
    )


    val dropdownState by creationCardViewModel.dropdownState
    val cardState by creationCardViewModel.cardState
    val validation = creationCardViewModel.validation.collectAsState().value
    val folder = creationCardViewModel.foldersState.collectAsState().value
    val deck = creationCardViewModel.deckState.collectAsState().value

    LaunchedEffect(dropdownState.selectedFolder.second) {
        if (dropdownState.selectedFolder.second != null) {
            creationCardViewModel.getAllDecksByFolderId(dropdownState.selectedFolder.second!!)
        }
    }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
    ) {
        Scaffold(
            topBar = {
                Box(
                    modifier = Modifier
                        .statusBarsPadding()
                        .padding(horizontal = dimenDpResource(R.dimen.padding_medium))
                        .padding(top = dimenDpResource(R.dimen.padding_large))
                ) {
                    TopBar(
                        onClick = {
                            navController.popBackStack()
                            creationCardViewModel.clear()
                        }
                    )
                }
            },
            content = { padding ->
                Content(
                    navController,
                    validation,
                    folder,
                    deck,
                    creationCardViewModel,
                    padding,
                    typeDropdownList,
                    dropdownState,
                    cardState,
                )
            }
        )
    }
}

@Composable
private fun TopBar(onClick: () -> Unit) {
    ActionHandlerButton(
        iconPainter = painterResource(R.drawable.back_icon),
        contentDescription = stringResource(R.string.back_screen_icon_button),
        onClick = onClick,
        iconTint = MaterialTheme.colorScheme.onPrimary,
        iconModifier = Modifier
            .background(
                color = MaterialTheme.colorScheme.outlineVariant,
                shape = MaterialTheme.shapes.extraLarge
            )
            .padding(all = dimenDpResource(R.dimen.padding_small))
            .size(size = dimenDpResource(R.dimen.padding_medium))
            .clip(shape = MaterialTheme.shapes.extraLarge)
    )
    Spacer(modifier = Modifier.height(dimenDpResource(R.dimen.spacer_large)))
}

@Composable
private fun Content(
    navController: NavController,
    validation: Boolean?,
    folder: UiState<List<Folder>>,
    deck: UiState<List<Deck>>,
    creationCardViewModel: CreationCardViewModel,
    padding: PaddingValues,
    typeDropdownList: List<Pair<String, Int>>,
    dropdownState: DropdownState,
    cardState: CardState,
) {
    Column(
        modifier = Modifier
            .padding(padding)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = dimenDpResource(R.dimen.padding_medium))
            .navigationBarsPadding()
    ) {
        DropdownSelectors(
            dropdownState,
            validation == true || validation == null,
            folder,
            creationCardViewModel,
            deck,
            typeDropdownList
        )
        Spacer(modifier = Modifier.height(height = dimenDpResource(R.dimen.spacer_medium)))
        InputFields(cardState, creationCardViewModel, validation == true || validation == null)
        Spacer(modifier = Modifier.height(height = dimenDpResource(R.dimen.spacer_large)))
        SaveButton(creationCardViewModel, cardState, dropdownState, navController)
    }
}

@Composable
private fun DropdownSelectors(
    dropdownState: DropdownState,
    validation: Boolean,
    folder: UiState<List<Folder>>,
    creationCardViewModel: CreationCardViewModel,
    deck: UiState<List<Deck>>,
    typeDropdownList: List<Pair<String, Int>>
) {
    FolderDropdownSelector(
        selectedFolder = dropdownState.selectedFolder,
        validation = validation,
        folder = folder,
        onClick = {
            creationCardViewModel.getAllFolders()
        },
        onItemClick = {
            if (dropdownState.selectedFolder != it) {
                creationCardViewModel.updateDropdownState { copy(selectedFolder = it) }
                creationCardViewModel.getAllDecksByFolderId(it.second)
                creationCardViewModel.updateDropdownState {
                    copy(
                        selectedDeck = Pair(
                            "Выберите колоду",
                            null
                        )
                    )
                }
            }
        },
        textStyle = MaterialTheme.typography.bodyMedium
    )
    Spacer(modifier = Modifier.height(height = dimenDpResource(R.dimen.spacer_medium)))
    DeckDropdownSelector(
        selectedDeck = dropdownState.selectedDeck,
        validation = validation,
        deck = deck,
        folderId = dropdownState.selectedFolder.second,
        onItemClick = {
            creationCardViewModel.updateDropdownState { copy(selectedDeck = it) }
        },
        onClick = { },
        textStyle = MaterialTheme.typography.bodyMedium
    )
    Spacer(modifier = Modifier.height(height = dimenDpResource(R.dimen.spacer_medium)))
    TypeDropdownSelector(
        typeDropdownList = typeDropdownList,
        dropdownState = dropdownState,
        validation = validation,
        onItemClick = { creationCardViewModel.updateDropdownState { copy(selectedType = it) } },
        onClick = {},
        textStyle = MaterialTheme.typography.bodyMedium
    )
}

@Composable
private fun FolderDropdownSelector(
    selectedFolder: Pair<String, Int?>,
    validation: Boolean,
    folder: UiState<List<Folder>>,
    onItemClick: (Pair<String, Int>) -> Unit,
    onClick: () -> Unit,
    textStyle: TextStyle,
) {
    DropdownSelector(
        label = stringResource(R.string.text_folder_dropdown_selector),
        validation = validation,
        selectedItem = selectedFolder,
        itemsState = folder.mapToUiState { folderPairs ->
            folderPairs.map { Pair(it.folderName, it.folderId) }
        },
        onItemClick = onItemClick,
        onClick = onClick,
        textStyle = textStyle
    )
}

@Composable
private fun DeckDropdownSelector(
    selectedDeck: Pair<String, Int?>,
    validation: Boolean,
    deck: UiState<List<Deck>>,
    folderId: Int?,
    onItemClick: (Pair<String, Int>) -> Unit,
    onClick: () -> Unit,
    textStyle: TextStyle,
) {
    DropdownSelector(
        label = stringResource(R.string.text_deck_dropdown_selector),
        validation = validation,
        selectedItem = selectedDeck,
        itemsState = if (folderId != null) deck.mapToUiState { decks ->
            decks.map { Pair(it.deckName, it.deckId) }
        } else UiState.Success(emptyList()),
        onItemClick = onItemClick,
        onClick = onClick,
        isEnabled = folderId != null,
        textStyle = textStyle
    )
}

@Composable
private fun TypeDropdownSelector(
    typeDropdownList: List<Pair<String, Int>>,
    validation: Boolean,
    dropdownState: DropdownState,
    onItemClick: (Pair<String, Int>) -> Unit,
    onClick: () -> Unit,
    textStyle: TextStyle
) {
    DropdownSelector(
        label = stringResource(R.string.text_type_dropdown_selector),
        validation = validation,
        selectedItem = dropdownState.selectedType,
        itemsState = UiState.Success(typeDropdownList),
        onItemClick = onItemClick,
        onClick = onClick,
        textStyle = textStyle,
    )
}

@Composable
private fun InputFields(
    cardState: CardState,
    creationCardViewModel: CreationCardViewModel,
    validation: Boolean
) {
    TitleInputField(
        placeholder = stringResource(R.string.enter_name_for_card),
        value = cardState.title,
        onValueChange = { creationCardViewModel.updateCardState { copy(title = it) } },
        readOnly = false,
        textStyle = MaterialTheme.typography.bodyMedium,
        placeholderTextStyle = MaterialTheme.typography.bodyMedium.copy(
            color = if (cardState.title.isNotBlank() || validation) text_gray else MaterialTheme.colorScheme.error
        ),
        modifier = textInputModifier(
            backgroundColor = if (cardState.title.isNotBlank() || validation) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onError,
            size = dimenDpResource(R.dimen.text_input_size_padding)
        )
            .fillMaxWidth()
            .heightIn(min = dimenDpResource(R.dimen.text_input_min_height))
            .wrapContentSize(Alignment.CenterStart)
    )
    Spacer(modifier = Modifier.height(height = dimenDpResource(R.dimen.spacer_medium)))
    CardInputField(
        placeholder = stringResource(R.string.enter_question_for_card),
        value = cardState.question,
        onValueChange = { creationCardViewModel.updateCardState { copy(question = it) } },
        textStyle = MaterialTheme.typography.bodyMedium,
        placeholderTextStyle = MaterialTheme.typography.bodyMedium.copy(
            color = if (cardState.question.isNotBlank() || validation) text_gray else MaterialTheme.colorScheme.error
        ),
        modifier = textInputModifier(
            backgroundColor = if (cardState.question.isNotBlank() || validation) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onError,
            topStart = dimenDpResource(R.dimen.text_input_topStart_padding),
            topEnd = dimenDpResource(R.dimen.text_input_topEnd_padding),
            bottomStart = dimenDpResource(R.dimen.text_input_bottomStart_zero_padding),
            bottomEnd = dimenDpResource(R.dimen.text_input_bottomEnd_zero_padding)
        )
            .fillMaxWidth()
            .heightIn(
                min = dimenDpResource(R.dimen.text_input_min_height),
                max = dimenDpResource(R.dimen.text_input_max_height)
            )
            .wrapContentSize(Alignment.CenterStart)
    )
    CardInputField(
        placeholder = stringResource(R.string.enter_answer_for_card),
        value = cardState.answer,
        onValueChange = { creationCardViewModel.updateCardState { copy(answer = it) } },
        textStyle = MaterialTheme.typography.bodyMedium,
        placeholderTextStyle = MaterialTheme.typography.bodyMedium.copy(
            color = if (cardState.answer.isNotBlank() || validation) text_gray else MaterialTheme.colorScheme.error
        ),
        modifier = textInputModifier(
            backgroundColor = if (cardState.answer.isNotBlank() || validation) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onError,
            topEnd = dimenDpResource(R.dimen.text_input_topEnd_zero_padding),
            topStart = dimenDpResource(R.dimen.text_input_topStart_zero_padding),
            bottomStart = dimenDpResource(R.dimen.text_input_topStart_padding),
            bottomEnd = dimenDpResource(R.dimen.text_input_topStart_padding)
        )
            .fillMaxWidth()
            .heightIn(
                min = dimenDpResource(R.dimen.text_input_min_height),
                max = dimenDpResource(R.dimen.text_input_max_height)
            )
            .wrapContentSize(Alignment.CenterStart),

        )
    Spacer(modifier = Modifier.height(height = dimenDpResource(R.dimen.spacer_medium)))
    TegInputField(
        titleTextInput = stringResource(R.string.text_tag_input_field),
        value = cardState.tag,
        onValueChange = { creationCardViewModel.updateCardState { copy(tag = it) } },
        textStyle = MaterialTheme.typography.bodyMedium,
        placeholderTextStyle = MaterialTheme.typography.bodyMedium.copy(
            color = text_gray
        ),
        modifier = textInputModifier(size = dimenDpResource(R.dimen.text_input_size_padding))
            .size(
                width = dimenDpResource(R.dimen.tag_text_input_min_weight),
                height = dimenDpResource(R.dimen.text_input_min_height)
            )
            .wrapContentSize(Alignment.CenterStart)
    )
}

@Composable
private fun SaveButton(
    creationCardViewModel: CreationCardViewModel,
    cardState: CardState,
    dropdownState: DropdownState,
    navController: NavController
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentSize(Alignment.Center)
    ) {
        SaveDataButton(
            text = stringResource(R.string.text_save_card_button),
            textStyle = MaterialTheme.typography.bodyMedium.copy(
                color = text_white
            ),
            buttonModifier = Modifier
                .background(
                    color = MaterialTheme.colorScheme.outlineVariant,
                    shape = MaterialTheme.shapes.small
                )
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) {
                    if (creationCardViewModel.validateInput(cardState, dropdownState)) {
                        creationCardViewModel.createCard(
                            Card(
                                cardName = cardState.title,
                                cardQuestion = cardState.question,
                                cardAnswer = cardState.answer,
                                cardType = dropdownState.selectedType.first,
                                cardTag = cardState.tag,
                                deckId = dropdownState.selectedDeck.second!!
                            )
                        )
                        navController.popBackStack()
                    }
                }
        )
    }
}

