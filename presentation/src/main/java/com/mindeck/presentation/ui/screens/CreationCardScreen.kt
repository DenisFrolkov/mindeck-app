package com.mindeck.presentation.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.mindeck.presentation.R
import com.mindeck.presentation.ui.components.buttons.ActionHandlerButton
import com.mindeck.presentation.ui.components.buttons.SaveDataButton
import com.mindeck.presentation.ui.components.dropdown_selector.DropdownSelector
import com.mindeck.presentation.ui.components.dropdown_selector.DropdownSelectorDataClass
import com.mindeck.presentation.ui.components.textfields.CardInputField
import com.mindeck.presentation.ui.components.textfields.TegInputField
import com.mindeck.presentation.ui.components.textfields.TitleInputField
import com.mindeck.presentation.ui.theme.BackgroundScreen
import com.mindeck.presentation.ui.theme.Blue
import com.mindeck.presentation.ui.theme.MediumGray
import com.mindeck.presentation.ui.theme.White
import kotlin.math.roundToInt

data class DropdownState(
    var folder: String = "Общая папка",
    var deck: String = "Общая колода",
    var priority: String = "Карточка с вводом ответа",
    var tape: String = "Простой"
)

data class InputFields(
    var title: String = "",
    var question: String = "",
    var answer: String = "",
    var tag: String = ""
)

@SuppressLint("ResourceType")
@Composable
fun CreationCardScreen(
    navController: NavController
) {
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
            .padding(top = insets, start = 16.dp, end = 16.dp)
    ) {
        ActionHandlerButton(
            iconPainter = painterResource(R.drawable.back_icon),
            contentDescription = stringResource(R.string.back_screen_icon_button),
            iconModifier = Modifier
                .background(color = Blue, shape = RoundedCornerShape(50.dp))
                .padding(all = 12.dp)
                .size(size = 16.dp)
                .clip(shape = RoundedCornerShape(50.dp))
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) {
                    navController.popBackStack()
                }
        )

        Spacer(modifier = Modifier.height(20.dp))

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
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentSize(Alignment.Center)
        ) {
            SaveDataButton(
                text = stringResource(R.string.text_save_card_button),
                fontFamily = fontFamily
            )
        }
    }
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
        dropdownSelectorDataClass = DropdownSelectorDataClass(
            title = stringResource(R.string.text_folder_dropdown_selector),
            selectedItem = folderDropdownSelect,
            onItemClick = onPassFolderDropdownSelect
        ),
        textStyle = textStyle,
    )
    Spacer(modifier = Modifier.height(height = 14.dp))
    DropdownSelector(
        dropdownSelectorDataClass = DropdownSelectorDataClass(
            title = stringResource(R.string.text_deck_dropdown_selector),
            selectedItem = deckDropdownSelect,
            onItemClick = onPassDeckDropdownSelect
        ),
        textStyle = textStyle,
    )
    Spacer(modifier = Modifier.height(height = 14.dp))
    DropdownSelector(
        dropdownSelectorDataClass = DropdownSelectorDataClass(
            title = stringResource(R.string.text_priority_dropdown_selector),
            selectedItem = priorityDropdownSelect,
            onItemClick = onPassPriorityDropdownSelect
        ),
        textStyle = textStyle,
    )
    Spacer(modifier = Modifier.height(height = 14.dp))
    DropdownSelector(
        dropdownSelectorDataClass = DropdownSelectorDataClass(
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

