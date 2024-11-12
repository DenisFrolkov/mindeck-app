package com.mindeck.presentation.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import com.mindeck.presentation.R
import com.mindeck.presentation.ui.components.buttons.BackScreenButton
import com.mindeck.presentation.ui.components.dropdown_selector.DropdownSelector
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

    var titleInputFieldValue by rememberSaveable { mutableStateOf("") }
    var cardInputQuestionValue by rememberSaveable { mutableStateOf("") }
    var cardInputAnswerValue by rememberSaveable { mutableStateOf("") }
    var tagInputValue by rememberSaveable { mutableStateOf("") }

    val maxHeight = 200.dp

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = BackgroundScreen)
            .padding(top = insets)
    ) {
        BackScreenButton()
        Spacer(modifier = Modifier.height(20.dp))
        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            DropdownSelector(titleSelector = "Папка:", "Общая папка", modifier = Modifier)
            Spacer(modifier = Modifier.height(height = 14.dp))
            DropdownSelector(titleSelector = "Колода:", "Общая колода", modifier = Modifier)
            Spacer(modifier = Modifier.height(height = 14.dp))
            DropdownSelector(titleSelector = "Приоритет:", "Простой", modifier = Modifier)
            Spacer(modifier = Modifier.height(height = 14.dp))
            DropdownSelector(
                titleSelector = "Тип:",
                "Карточка с вводом ответа",
                modifier = Modifier
            )
            Spacer(modifier = Modifier.height(height = 20.dp))
            TitleInputField(
                value = titleInputFieldValue,
                onValueChange = { titleInputFieldValue = it },
                placeholder = "Введите название карточки",
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(shape = RoundedCornerShape(4.dp))
                    .background(
                        White
                    )
                    .height(50.dp)
                    .border(
                        width = 0.25.dp,
                        color = MediumGray,
                        shape = RoundedCornerShape(4.dp)
                    )
                    .wrapContentSize(Alignment.CenterStart)
                    .padding(start = 12.dp),
                fontFamily = R.font.opensans_medium
            )
            Spacer(modifier = Modifier.height(height = 10.dp))
            CardInputField(
                value = cardInputQuestionValue,
                onValueChange = { cardInputQuestionValue = it },
                placeholder = "Введите вопрос",
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(shape = RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp))
                    .background(
                        White
                    )
                    .border(
                        width = 0.25.dp,
                        color = MediumGray,
                        shape = RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp)
                    )
                    .padding(start = 12.dp, top = 14.dp, bottom = 14.dp)
                    .heightIn(max = maxHeight),
                fontFamily = R.font.opensans_medium
            )
            CardInputField(
                value = cardInputAnswerValue,
                onValueChange = { cardInputAnswerValue = it },
                placeholder = "Введите ответ",
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(shape = RoundedCornerShape(bottomStart = 4.dp, bottomEnd = 4.dp))
                    .background(
                        White
                    )
                    .height(50.dp)
                    .border(
                        width = 0.25.dp,
                        color = MediumGray,
                        shape = RoundedCornerShape(bottomStart = 4.dp, bottomEnd = 4.dp)
                    )
                    .wrapContentSize(Alignment.CenterStart)
                    .padding(start = 12.dp)
                    .heightIn(max = maxHeight),
                fontFamily = R.font.opensans_medium
            )
            Spacer(modifier = Modifier.height(height = 14.dp))
            TegInputField(
                value = tagInputValue,
                onValueChange = {
                    tagInputValue = it
                },
                titleTextInput = "Тег:",
                modifier = Modifier
                    .clip(shape = RoundedCornerShape(4.dp))
                    .background(
                        White
                    )
                    .height(40.dp)
                    .border(
                        width = 0.25.dp,
                        color = MediumGray,
                        shape = RoundedCornerShape(4.dp)
                    )
                    .wrapContentSize(Alignment.CenterStart)
                    .padding(start = 12.dp),
                fontFamily = FontFamily(Font(R.font.opensans_medium))
            )
        }
    }
}