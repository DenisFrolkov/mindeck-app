package com.mindeck.presentation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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
import com.mindeck.presentation.viewmodel.CreationCardViewModel

@Composable
fun CreationCardScreen(
    navController: NavController,
    creationCardViewModel: CreationCardViewModel
) {
    val folder = creationCardViewModel.folderUIState.collectAsState().value
    val deck = creationCardViewModel.deckByIdrUIState.collectAsState().value
    val scrollState = rememberScrollState()

    val typeDropdownList = listOf<Pair<String, Int>>(
        Pair("Простая", 1),
        Pair("Простая(с вводом ответа)", 1)
    )

    var folderDropdownSelect by rememberSaveable {
        mutableStateOf<Pair<String, Int?>>(
            Pair(
                "Выберите папку",
                null
            )
        )
    }
    var deckDropdownSelect by rememberSaveable {
        mutableStateOf<Pair<String, Int?>>(
            Pair(
                "Выберите колоду",
                null
            )
        )
    }

    var typeDropdownSelect by rememberSaveable {
        mutableStateOf<Pair<String, Int?>>(
            Pair(
                "Выберите тип карточки",
                null
            )
        )
    }

    LaunchedEffect(folderDropdownSelect.second) {
        if (folderDropdownSelect.second != null) {
            creationCardViewModel.getAllDecksByFolderId(folderDropdownSelect.second!!)
        }
    }

    var titleInputFieldValue by rememberSaveable { mutableStateOf("") }
    var cardInputQuestionValue by rememberSaveable { mutableStateOf("") }
    var cardInputAnswerValue by rememberSaveable { mutableStateOf("") }
    var tagInputValue by rememberSaveable { mutableStateOf("") }

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
            ActionHandlerButton(
                iconPainter = painterResource(R.drawable.back_icon),
                contentDescription = stringResource(R.string.back_screen_icon_button),
                onClick = { navController.popBackStack() },
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
        },
        content = { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .verticalScroll(state = scrollState)
            ) {
                FolderDropdownSelector(
                    selectedFolder = folderDropdownSelect.first,
                    folder = folder,
                    onClick = { creationCardViewModel.getAllFolders() },
                    onItemClick = {
                        folderDropdownSelect = it
                        deckDropdownSelect = Pair(
                            "Выберите колоду",
                            null
                        )
                    },
                    textStyle = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(height = dimenDpResource(R.dimen.spacer_medium)))
                DeckDropdownSelector(
                    selectedDeck = deckDropdownSelect.first,
                    deck = deck,
                    folderId = folderDropdownSelect.second,
                    onClick = { },
                    onItemClick = { deckDropdownSelect = it },
                    textStyle = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(height = dimenDpResource(R.dimen.spacer_medium)))
                DropdownSelector(
                    dropdownSelectorData = DropdownSelectorData(
                        title = stringResource(R.string.text_type_dropdown_selector),
                        itemList = typeDropdownList,
                        selectedItem = typeDropdownSelect.first,
                        onItemClick = { typeDropdownSelect = it }
                    ),
                    textStyle = MaterialTheme.typography.bodyMedium,
                )
                Spacer(modifier = Modifier.height(height = dimenDpResource(R.dimen.spacer_medium)))
                TitleInputField(
                    placeholder = stringResource(R.string.enter_name_for_card),
                    value = titleInputFieldValue,
                    onValueChange = { titleInputFieldValue = it },
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
                    value = cardInputQuestionValue,
                    onValueChange = { cardInputQuestionValue = it },
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
                    value = cardInputAnswerValue,
                    onValueChange = { cardInputAnswerValue = it },
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
                    value = tagInputValue,
                    onValueChange = {
                        tagInputValue = it
                    },
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
                                if (deckDropdownSelect.second != null) {
                                    creationCardViewModel.createCard(
                                        Card(
                                            cardName = titleInputFieldValue,
                                            cardQuestion = cardInputQuestionValue,
                                            cardAnswer = cardInputAnswerValue,
                                            cardPriority = "123441",
                                            cardType = typeDropdownSelect.first,
                                            cardTag = tagInputValue,
                                            deckId = deckDropdownSelect.second!!
                                        )
                                    )
                                    navController.popBackStack()
                                }
                            }

                    )
                }
            }
        }

    )
}

@Composable
fun FolderDropdownSelector(
    selectedFolder: String,
    folder: UiState<List<Folder>>,
    onItemClick: (Pair<String, Int>) -> Unit,
    onClick: () -> Unit,
    textStyle: TextStyle,
) {
    val dropdownSelectorState = remember { DropdownSelectorState() }

    Row() {
        Text(
            text = stringResource(R.string.text_folder_dropdown_selector),
            style = textStyle,
            modifier = Modifier
                .padding(dimenDpResource(R.dimen.padding_extra_small))
                .wrapContentSize(Alignment.CenterStart)
                .width(dimenDpResource(R.dimen.dropdown_min_weight))
        )

        Spacer(modifier = Modifier.width(dimenDpResource(R.dimen.spacer_small)))

        Column(modifier = Modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                onClick()
                dropdownSelectorState.toggle()
            }) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = MaterialTheme.colorScheme.onPrimary,
                        shape = MaterialTheme.shapes.extraSmall
                    )
                    .height(height = dimenDpResource(R.dimen.dropdown_menu_item_height))
                    .border(
                        dimenDpResource(R.dimen.border_width_dot_two_five),
                        MaterialTheme.colorScheme.outline,
                        shape = if (dropdownSelectorState.isExpanded) RoundedCornerShape(
                            topStart = dimenDpResource(R.dimen.text_input_topStart_padding),
                            topEnd = dimenDpResource(R.dimen.text_input_topEnd_padding),
                        ) else MaterialTheme.shapes.extraSmall
                    )
                    .wrapContentSize(Alignment.Center)

            ) {
                Text(
                    text = selectedFolder,
                    style = textStyle
                )
            }
            if (dropdownSelectorState.isExpanded) {
                when (folder) {
                    is UiState.Success -> {
                        Column(
                            modifier = Modifier
                                .border(
                                    dimenDpResource(R.dimen.border_width_dot_two_five),
                                    MaterialTheme.colorScheme.outline,
                                    shape = RoundedCornerShape(
                                        bottomStart = dimenDpResource(R.dimen.text_input_bottomStart_padding),
                                        bottomEnd = dimenDpResource(R.dimen.text_input_bottomEnd_padding)
                                    )
                                )
                        ) {
                            SelectorDropdownMenu(
                                selectorItemList = folder.data.map {
                                    Pair(
                                        it.folderName,
                                        it.folderId
                                    )
                                },
                                onItemClick = onItemClick,
                                dropdownSelectorState = dropdownSelectorState,
                                textStyle = textStyle
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DeckDropdownSelector(
    selectedDeck: String,
    deck: UiState<List<Deck>>,
    folderId: Int?,
    onItemClick: (Pair<String, Int>) -> Unit,
    onClick: () -> Unit,
    textStyle: TextStyle,
) {
    val dropdownSelectorState = remember { DropdownSelectorState() }

    Row() {
        Text(
            text = stringResource(R.string.text_deck_dropdown_selector),
            style = textStyle,
            modifier = Modifier
                .padding(dimenDpResource(R.dimen.padding_extra_small))
                .wrapContentSize(Alignment.CenterStart)
                .width(dimenDpResource(R.dimen.dropdown_min_weight))
        )

        Spacer(modifier = Modifier.width(dimenDpResource(R.dimen.spacer_small)))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) {
                    if (folderId != null) onClick()
                    dropdownSelectorState.toggle()
                }
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = MaterialTheme.colorScheme.onPrimary,
                        shape = MaterialTheme.shapes.extraSmall
                    )
                    .height(height = dimenDpResource(R.dimen.dropdown_menu_item_height))
                    .border(
                        dimenDpResource(R.dimen.border_width_dot_two_five),
                        MaterialTheme.colorScheme.outline,
                        shape = if (dropdownSelectorState.isExpanded) RoundedCornerShape(
                            topStart = dimenDpResource(R.dimen.text_input_topStart_padding),
                            topEnd = dimenDpResource(R.dimen.text_input_topEnd_padding),
                        ) else MaterialTheme.shapes.extraSmall
                    )
                    .wrapContentSize(Alignment.Center)

            ) {
                Text(
                    text = selectedDeck,
                    style = textStyle
                )
            }
            if (dropdownSelectorState.isExpanded) {
                if (folderId != null) {
                    when (deck) {
                        is UiState.Success -> {
                            if (deck.data.isNotEmpty()) {
                                Column(
                                    modifier = Modifier
                                        .border(
                                            dimenDpResource(R.dimen.border_width_dot_two_five),
                                            MaterialTheme.colorScheme.outline,
                                            shape = RoundedCornerShape(
                                                bottomStart = dimenDpResource(R.dimen.text_input_bottomStart_padding),
                                                bottomEnd = dimenDpResource(R.dimen.text_input_bottomEnd_padding)
                                            )
                                        )
                                ) {
                                    SelectorDropdownMenu(
                                        selectorItemList = deck.data.map {
                                            Pair(
                                                it.deckName,
                                                it.folderId
                                            )
                                        },
                                        onItemClick = onItemClick,
                                        dropdownSelectorState = dropdownSelectorState,
                                        textStyle = textStyle
                                    )
                                }
                            } else {
                                Column(
                                    modifier = Modifier
                                        .height(dimenDpResource(R.dimen.dropdown_menu_item_height))
                                        .border(
                                            dimenDpResource(R.dimen.border_width_dot_two_five),
                                            MaterialTheme.colorScheme.outline,
                                            shape = RoundedCornerShape(
                                                bottomStart = dimenDpResource(R.dimen.text_input_bottomStart_padding),
                                                bottomEnd = dimenDpResource(R.dimen.text_input_bottomEnd_padding)
                                            )
                                        )
                                ) {
                                    Box(
                                        contentAlignment = Alignment.Center,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(dimenDpResource(R.dimen.dropdown_menu_item_height))
                                            .background(
                                                color = MaterialTheme.colorScheme.onPrimary,
                                                shape = RoundedCornerShape(
                                                    bottomStart = dimenDpResource(R.dimen.dropdown_menu_item_bottomStart_padding),
                                                    bottomEnd = dimenDpResource(R.dimen.dropdown_menu_item_bottomEnd_padding)
                                                )
                                            )
                                    ) {
                                        Text(
                                            "Нет колод", style = textStyle
                                        )
                                    }
                                }
                            }
                        }
                    }
                } else {
                    Column(
                        modifier = Modifier
                            .height(dimenDpResource(R.dimen.dropdown_menu_item_height))
                            .border(
                                dimenDpResource(R.dimen.border_width_dot_two_five),
                                MaterialTheme.colorScheme.outline,
                                shape = RoundedCornerShape(
                                    bottomStart = dimenDpResource(R.dimen.text_input_bottomStart_padding),
                                    bottomEnd = dimenDpResource(R.dimen.text_input_bottomEnd_padding)
                                )
                            )
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(dimenDpResource(R.dimen.dropdown_menu_item_height))
                                .background(
                                    color = MaterialTheme.colorScheme.onPrimary,
                                    shape = RoundedCornerShape(
                                        bottomStart = dimenDpResource(R.dimen.dropdown_menu_item_bottomStart_padding),
                                        bottomEnd = dimenDpResource(R.dimen.dropdown_menu_item_bottomEnd_padding)
                                    )
                                )
                        ) {
                            Text(
                                "Папка не была выбрана", style = textStyle
                            )
                        }
                    }
                }
            }
        }
    }
}

