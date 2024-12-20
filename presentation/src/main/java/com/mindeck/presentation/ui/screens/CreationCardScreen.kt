package com.mindeck.presentation.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
import com.mindeck.presentation.ui.theme.background_light_blue
import com.mindeck.presentation.ui.theme.outline_variant_blue
import com.mindeck.presentation.ui.theme.outline_medium_gray
import com.mindeck.presentation.ui.theme.on_primary_white
import com.mindeck.presentation.ui.theme.text_white
import com.mindeck.presentation.viewmodel.CreationCardViewModel

@SuppressLint("ResourceType")
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

    val maxHeight = 200.dp
    val minHeight = 46.dp

    val fontFamily = FontFamily(Font(R.font.opensans_medium))
    val textStyle = TextStyle(fontSize = 14.sp, fontFamily = fontFamily)

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(color = background_light_blue)
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(horizontal = 16.dp)
            .padding(top = 16.dp), containerColor = background_light_blue,
        topBar = {
            ActionHandlerButton(
                iconPainter = painterResource(R.drawable.back_icon),
                contentDescription = stringResource(R.string.back_screen_icon_button),
                onClick = { navController.popBackStack() },
                iconModifier = Modifier
                    .background(color = outline_variant_blue, shape = RoundedCornerShape(50.dp))
                    .padding(all = 12.dp)
                    .size(size = 16.dp)
                    .clip(shape = RoundedCornerShape(50.dp))
            )

            Spacer(modifier = Modifier.height(20.dp))
        },
        content = { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .verticalScroll(state = scrollState)
            ) {
                DropdownSelectors(
                    textStyle = textStyle,
                    folderDropdownSelect = folderDropdownSelect,
                    deckDropdownSelect = deckDropdownSelect,
                    priorityDropdownSelect = priorityDropdownSelect,
                    tapeDropdownSelect = tapeDropdownSelect,
                    onPassFolderDropdownSelect = { folderDropdownSelect = it },
                    onPassDeckDropdownSelect = { deckDropdownSelect = it },
                    onPassPriorityDropdownSelect = { priorityDropdownSelect = it },
                    onPassTapeDropdownSelect = { tapeDropdownSelect = it },
                )

                Spacer(modifier = Modifier.height(height = 20.dp))

                TitleInputField(
                    placeholder = stringResource(R.string.enter_name_for_card),
                    value = titleInputFieldValue,
                    onValueChange = { titleInputFieldValue = it },
                    fontFamily = fontFamily,
                    modifier = textInputModifier(size = 4.dp)
                        .fillMaxWidth()
                        .heightIn(min = minHeight)
                        .wrapContentSize(Alignment.CenterStart)
                )
                Spacer(modifier = Modifier.height(height = 10.dp))
                CardInputField(
                    placeholder = stringResource(R.string.enter_question_for_card),
                    value = cardInputQuestionValue,
                    onValueChange = { cardInputQuestionValue = it },
                    modifier = textInputModifier(
                        topStart = 4.dp,
                        topEnd = 4.dp,
                        bottomStart = 0.dp,
                        bottomEnd = 0.dp
                    )
                        .fillMaxWidth()
                        .heightIn(min = minHeight, max = maxHeight)
                        .wrapContentSize(Alignment.CenterStart)
                )
                CardInputField(
                    placeholder = stringResource(R.string.enter_answer_for_card),
                    value = cardInputAnswerValue,
                    onValueChange = { cardInputAnswerValue = it },
                    modifier = textInputModifier(
                        topEnd = 0.dp,
                        topStart = 0.dp,
                        bottomStart = 4.dp,
                        bottomEnd = 4.dp
                    )
                        .fillMaxWidth()
                        .heightIn(min = minHeight, max = maxHeight)
                        .wrapContentSize(Alignment.CenterStart),

                    )
                Spacer(modifier = Modifier.height(height = 14.dp))
                TegInputField(
                    titleTextInput = stringResource(R.string.text_teg_input_field),
                    value = tagInputValue,
                    onValueChange = {
                        tagInputValue = it
                    },
                    fontFamily = fontFamily,
                    modifier = textInputModifier(size = 4.dp)
                        .size(width = 120.dp, height = 36.dp)
                        .wrapContentSize(Alignment.CenterStart)
                )
                Spacer(modifier = Modifier.height(height = 20.dp))
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
                                        deckId = 1
                                    )
                                )
                            }

                    )
                }
            }
        }

    )
}

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
    Spacer(modifier = Modifier.height(height = 14.dp))
    DropdownSelector(
        dropdownSelectorData = DropdownSelectorData(
            title = stringResource(R.string.text_deck_dropdown_selector),
            selectedItem = deckDropdownSelect,
            onItemClick = onPassDeckDropdownSelect
        ),
        textStyle = textStyle,
    )
    Spacer(modifier = Modifier.height(height = 14.dp))
    DropdownSelector(
        dropdownSelectorData = DropdownSelectorData(
            title = stringResource(R.string.text_priority_dropdown_selector),
            selectedItem = priorityDropdownSelect,
            onItemClick = onPassPriorityDropdownSelect
        ),
        textStyle = textStyle,
    )
    Spacer(modifier = Modifier.height(height = 14.dp))
    DropdownSelector(
        dropdownSelectorData = DropdownSelectorData(
            title = stringResource(R.string.text_tape_dropdown_selector),
            selectedItem = tapeDropdownSelect,
            onItemClick = onPassTapeDropdownSelect
        ),
        textStyle = textStyle,
    )
}

@Composable
fun textInputModifier(
    topStart: Dp = 0.dp,
    topEnd: Dp = 0.dp,
    bottomStart: Dp = 0.dp,
    bottomEnd: Dp = 0.dp,
    size: Dp = 0.dp
) = Modifier
    .clip(
        shape =
        if (topStart + topEnd + bottomEnd + bottomStart > size) {
            RoundedCornerShape(
                topStart = topStart,
                topEnd = topEnd,
                bottomStart = bottomStart,
                bottomEnd = bottomEnd
            )
        } else {
            RoundedCornerShape(
                size
            )
        },
    )
    .background(
        on_primary_white
    )
    .border(
        width = 0.25.dp,
        color = outline_medium_gray,
        shape =
        if (topStart + topEnd + bottomEnd + bottomStart > size) {
            RoundedCornerShape(
                topStart = topStart,
                topEnd = topEnd,
                bottomStart = bottomStart,
                bottomEnd = bottomEnd
            )
        } else {
            RoundedCornerShape(
                size
            )
        }
    )
    .padding(start = 12.dp)

