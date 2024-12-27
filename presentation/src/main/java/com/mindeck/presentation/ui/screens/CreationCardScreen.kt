package com.mindeck.presentation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.runtime.Composable
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
import com.mindeck.presentation.R
import com.mindeck.presentation.ui.components.buttons.ActionHandlerButton
import com.mindeck.presentation.ui.components.buttons.SaveDataButton
import com.mindeck.presentation.ui.components.dropdown.dropdown_selector.DropdownSelector
import com.mindeck.presentation.ui.components.dropdown.dropdown_selector.DropdownSelectorData
import com.mindeck.presentation.ui.components.textfields.CardInputField
import com.mindeck.presentation.ui.components.textfields.TegInputField
import com.mindeck.presentation.ui.components.textfields.TitleInputField
import com.mindeck.presentation.ui.components.utils.dimenDpResource
import com.mindeck.presentation.ui.components.utils.textInputModifier
import com.mindeck.presentation.ui.theme.text_gray
import com.mindeck.presentation.ui.theme.text_white
import com.mindeck.presentation.viewmodel.CreationCardViewModel

@Composable
fun CreationCardScreen(
    navController: NavController,
    creationCardViewModel: CreationCardViewModel
) {
    val scrollState = rememberScrollState()

    var folderDropdownSelect by rememberSaveable { mutableStateOf("Общая папка") }
    var deckDropdownSelect by rememberSaveable { mutableStateOf("Общая колода") }
    var priorityDropdownSelect by rememberSaveable { mutableStateOf("Карточка с вводом ответа") }
    var tapeDropdownSelect by rememberSaveable { mutableStateOf("Простой") }

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
                    .background(color = MaterialTheme.colorScheme.outlineVariant, shape = MaterialTheme.shapes.extraLarge)
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
                DropdownSelectors(
                    textStyle = MaterialTheme.typography.bodyMedium,
                    folderDropdownSelect = folderDropdownSelect,
                    deckDropdownSelect = deckDropdownSelect,
                    priorityDropdownSelect = priorityDropdownSelect,
                    tapeDropdownSelect = tapeDropdownSelect,
                    onPassFolderDropdownSelect = { folderDropdownSelect = it },
                    onPassDeckDropdownSelect = { deckDropdownSelect = it },
                    onPassPriorityDropdownSelect = { priorityDropdownSelect = it },
                    onPassTapeDropdownSelect = { tapeDropdownSelect = it },
                )

                Spacer(modifier = Modifier.height(height = dimenDpResource(R.dimen.spacer_large)))

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
                        .heightIn(min = dimenDpResource(R.dimen.text_input_min_height), max = dimenDpResource(R.dimen.text_input_max_height))
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
                        .heightIn(min = dimenDpResource(R.dimen.text_input_min_height), max = dimenDpResource(R.dimen.text_input_max_height))
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
                        .size(width = dimenDpResource(R.dimen.tag_text_input_min_weight), height = dimenDpResource(R.dimen.text_input_min_height))
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
                                creationCardViewModel.createCard(
                                    Card(
                                        cardName = titleInputFieldValue,
                                        cardQuestion = cardInputQuestionValue,
                                        cardAnswer = cardInputAnswerValue,
                                        cardPriority = "123441",
                                        cardType = "4123",
                                        cardTag = tagInputValue,
                                        deckId = 2
                                    )
                                )
                                navController.popBackStack()
                            }

                    )
                }
            }
        }

    )
}

//Изменить реализацию drawBehind в DropdownMenu в DropdownSelector
@Composable
private fun DropdownSelectors(
    textStyle: TextStyle,
    folderDropdownSelect: String,
    deckDropdownSelect: String,
    priorityDropdownSelect: String,
    tapeDropdownSelect: String,
    onPassFolderDropdownSelect: (String) -> Unit,
    onPassDeckDropdownSelect: (String) -> Unit,
    onPassPriorityDropdownSelect: (String) -> Unit,
    onPassTapeDropdownSelect: (String) -> Unit,
) {
    DropdownSelector(
        dropdownSelectorData = DropdownSelectorData(
            title = stringResource(R.string.text_folder_dropdown_selector),
            selectedItem = folderDropdownSelect,
            onItemClick = onPassFolderDropdownSelect
        ),
        textStyle = textStyle,
    )
    Spacer(modifier = Modifier.height(height = dimenDpResource(R.dimen.spacer_medium)))
    DropdownSelector(
        dropdownSelectorData = DropdownSelectorData(
            title = stringResource(R.string.text_deck_dropdown_selector),
            selectedItem = deckDropdownSelect,
            onItemClick = onPassDeckDropdownSelect
        ),
        textStyle = textStyle,
    )
    Spacer(modifier = Modifier.height(height = dimenDpResource(R.dimen.spacer_medium)))
    DropdownSelector(
        dropdownSelectorData = DropdownSelectorData(
            title = stringResource(R.string.text_priority_dropdown_selector),
            selectedItem = priorityDropdownSelect,
            onItemClick = onPassPriorityDropdownSelect
        ),
        textStyle = textStyle,
    )
    Spacer(modifier = Modifier.height(height = dimenDpResource(R.dimen.spacer_medium)))
    DropdownSelector(
        dropdownSelectorData = DropdownSelectorData(
            title = stringResource(R.string.text_tape_dropdown_selector),
            selectedItem = tapeDropdownSelect,
            onItemClick = onPassTapeDropdownSelect
        ),
        textStyle = textStyle,
    )
}

