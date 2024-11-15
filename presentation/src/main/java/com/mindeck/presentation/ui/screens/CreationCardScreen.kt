package com.mindeck.presentation.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mindeck.presentation.R
import com.mindeck.presentation.ui.components.buttons.BackScreenButton
import com.mindeck.presentation.ui.components.buttons.SaveDataButton
import com.mindeck.presentation.ui.components.dropdown_selector.DropdownSelector
import com.mindeck.presentation.ui.components.dropdown_selector.DropdownSelectorDataClass
import com.mindeck.presentation.ui.components.textfields.CardInputField
import com.mindeck.presentation.ui.components.textfields.TegInputField
import com.mindeck.presentation.ui.components.textfields.TitleInputField
import com.mindeck.presentation.ui.theme.BackgroundScreen
import com.mindeck.presentation.ui.theme.MediumGray
import com.mindeck.presentation.ui.theme.White

@SuppressLint("ResourceType")
@Composable
fun CreationCardScreen() {
    val insets = WindowInsets.systemBars.asPaddingValues().calculateTopPadding()

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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = BackgroundScreen)
            .padding(top = insets)
    ) {
        BackScreenButton()
        Spacer(modifier = Modifier.height(20.dp))
        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            DropdownSelector(
                dropdownSelectorDataClass = DropdownSelectorDataClass(
                    title = stringResource(R.string.text_folder_dropdown_selector),
                    selectedItem = folderDropdownSelect,
                    onItemClick = { folderDropdownSelect = it }
                ),
                textStyle = textStyle,
            )
            Spacer(modifier = Modifier.height(height = 14.dp))
            DropdownSelector(
                dropdownSelectorDataClass = DropdownSelectorDataClass(
                    title = stringResource(R.string.text_deck_dropdown_selector),
                    selectedItem = deckDropdownSelect,
                    onItemClick = { deckDropdownSelect = it }
                ),
                textStyle = textStyle,
            )
            Spacer(modifier = Modifier.height(height = 14.dp))
            DropdownSelector(
                dropdownSelectorDataClass = DropdownSelectorDataClass(
                    title = stringResource(R.string.text_priority_dropdown_selector),
                    selectedItem = priorityDropdownSelect,
                    onItemClick = { priorityDropdownSelect = it }
                ),
                textStyle = textStyle,
            )
            Spacer(modifier = Modifier.height(height = 14.dp))
            DropdownSelector(
                dropdownSelectorDataClass = DropdownSelectorDataClass(
                    title = stringResource(R.string.text_tape_dropdown_selector),
                    selectedItem = tapeDropdownSelect,
                    onItemClick = { tapeDropdownSelect = it }
                ),
                textStyle = textStyle,
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
                fontFamily = fontFamily,
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
                fontFamily = fontFamily,
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
            Box(modifier = Modifier.fillMaxWidth().wrapContentSize(Alignment.Center)) {
                SaveDataButton(
                    text = "Сохранить карточку",
                    fontFamily = fontFamily
                )
            }
        }
    }
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
        White
    )
    .border(
        width = 0.25.dp,
        color = MediumGray,
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

