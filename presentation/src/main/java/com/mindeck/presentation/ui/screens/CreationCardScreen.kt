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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.mindeck.presentation.R
import com.mindeck.presentation.ui.components.buttons.BackScreenButton
import com.mindeck.presentation.ui.components.dropdown_selector.DropdownSelector
import com.mindeck.presentation.ui.components.textfields.TitleInputField
import com.mindeck.presentation.ui.theme.BackgroundScreen
import com.mindeck.presentation.ui.theme.MediumGray
import com.mindeck.presentation.ui.theme.White

@SuppressLint("ResourceType")
@Composable
fun CreationCardScreen() {
    val insets = WindowInsets.systemBars.asPaddingValues().calculateTopPadding()

    var value by rememberSaveable { mutableStateOf("") }

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
                value = value,
                onValueChange = { value = it },
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
        }
    }
}