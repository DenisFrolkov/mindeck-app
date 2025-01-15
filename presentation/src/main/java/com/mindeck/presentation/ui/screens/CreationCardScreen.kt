package com.mindeck.presentation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
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
import com.mindeck.presentation.ui.components.buttons.ActionHandlerButton
import com.mindeck.presentation.ui.components.buttons.SaveDataButton
import com.mindeck.presentation.ui.components.dropdown.dropdown_selector.DropdownSelector
import com.mindeck.presentation.ui.components.dropdown.dropdown_selector.DropdownSelectorData
import com.mindeck.presentation.ui.components.dropdown.dropdown_selector.DropdownSelectorState
import com.mindeck.presentation.ui.components.dropdown.dropdown_selector.SelectorDropdownMenu
import com.mindeck.presentation.ui.components.textfields.CardInputField
import com.mindeck.presentation.ui.components.textfields.TegInputField
import com.mindeck.presentation.ui.components.textfields.TitleInputField
import com.mindeck.presentation.ui.components.utils.dimenDpResource
import com.mindeck.presentation.ui.components.utils.textInputModifier
import com.mindeck.presentation.ui.theme.text_gray
import com.mindeck.presentation.ui.theme.text_white
import com.mindeck.presentation.uiState.UiState
import com.mindeck.presentation.uiState.mapToUiState
import com.mindeck.presentation.viewmodel.CardState
import com.mindeck.presentation.viewmodel.CreationCardViewModel
import com.mindeck.presentation.viewmodel.DropdownState

@Composable
fun CreationCardScreen(
    navController: NavController,
    creationCardViewModel: CreationCardViewModel
) {
    val folder = creationCardViewModel.foldersState.collectAsState().value
    val deck = creationCardViewModel.deckState.collectAsState().value

    val typeDropdownList = listOf(
        Pair("Простая", 1),
        Pair("Простая(с вводом ответа)", 2)
    )

    val validation = creationCardViewModel.validateInput()

    val dropdownState by creationCardViewModel.dropdownState
    val cardState = creationCardViewModel.cardState.value

    LaunchedEffect(dropdownState.selectedFolder.second) {
        if (dropdownState.selectedFolder.second != null) {
            creationCardViewModel.getAllDecksByFolderId(dropdownState.selectedFolder.second!!)
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(horizontal = dimenDpResource(R.dimen.padding_medium))
            .padding(top = dimenDpResource(R.dimen.padding_medium)),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopBar(
                onClick = {
                    navController.popBackStack()
                    creationCardViewModel.clear()
                }
            )
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

@Composable
private fun Content(
    navController: NavController,
    validation: Boolean,
    folder: UiState<List<Folder>>,
    deck: UiState<List<Deck>>,
    creationCardViewModel: CreationCardViewModel,
    padding: PaddingValues,
    typeDropdownList: List<Pair<String, Int>>,
    dropdownState: DropdownState,
    cardState: CardState,
) {

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .padding(padding)
            .verticalScroll(scrollState)
    ) {
        FolderDropdownSelector(
            selectedFolder = dropdownState.selectedFolder.first,
            folder = folder,
            onClick = { creationCardViewModel.getAllFolders() },
            onItemClick = {
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
            },
            textStyle = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.height(height = dimenDpResource(R.dimen.spacer_medium)))
        DeckDropdownSelector(
            selectedDeck = dropdownState.selectedDeck.first,
            deck = deck,
            folderId = dropdownState.selectedFolder.second,
            onClick = { },
            onItemClick = { creationCardViewModel.updateDropdownState { copy(selectedDeck = it) } },
            textStyle = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.height(height = dimenDpResource(R.dimen.spacer_medium)))
        DropdownSelector(
            textStyle = MaterialTheme.typography.bodyMedium,
            label = stringResource(R.string.text_type_dropdown_selector),
            selectedItem = dropdownState.selectedType.first,
            itemsState = UiState.Success(typeDropdownList),
            onItemClick = { creationCardViewModel.updateDropdownState { copy(selectedType = it) } },
            onClick = {}

        )
        Spacer(modifier = Modifier.height(height = dimenDpResource(R.dimen.spacer_medium)))
        TitleInputField(
            placeholder = stringResource(R.string.enter_name_for_card),
            value = cardState.title,
            onValueChange = { creationCardViewModel.updateCardState { copy(title = it) } },
            readOnly = false,
            textStyle = MaterialTheme.typography.bodyMedium,
            placeholderTextStyle = MaterialTheme.typography.bodyMedium.copy(
                color = text_gray
            ),
            modifier = textInputModifier(size = dimenDpResource(R.dimen.text_input_size_padding))
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
                color = text_gray
            ),
            modifier = textInputModifier(
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
                color = text_gray
            ),
            modifier = textInputModifier(
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
        Spacer(modifier = Modifier.height(height = dimenDpResource(R.dimen.spacer_large)))
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
                        if (validation) {
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
private fun FolderDropdownSelector(
    selectedFolder: String,
    folder: UiState<List<Folder>>,
    onItemClick: (Pair<String, Int>) -> Unit,
    onClick: () -> Unit,
    textStyle: TextStyle,
) {
    DropdownSelector(
        label = stringResource(R.string.text_folder_dropdown_selector),
        selectedItem = selectedFolder,
        itemsState = folder.mapToUiState { folders ->
            folders.map { Pair(it.folderName, it.folderId) }
        },
        onItemClick = onItemClick,
        onClick = onClick,
        textStyle = textStyle
    )
}

@Composable
private fun DeckDropdownSelector(
    selectedDeck: String,
    deck: UiState<List<Deck>>,
    folderId: Int?,
    onItemClick: (Pair<String, Int>) -> Unit,
    onClick: () -> Unit,
    textStyle: TextStyle,
) {
    DropdownSelector(
        label = stringResource(R.string.text_deck_dropdown_selector),
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

