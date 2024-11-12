package com.mindeck.presentation.ui.components.textfields

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mindeck.presentation.R
import com.mindeck.presentation.ui.components.dropdown_selector.DropDownMenu
import com.mindeck.presentation.ui.theme.White

@Composable
fun TegInputField(
    value: String,
    titleTextInput: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier,
    fontFamily: Int
) {

    Row(
        verticalAlignment = Alignment.Top
    ) {
        LabelDropdown(titleTextInput)
        Spacer(modifier = Modifier.width(20.dp))
        BasicTextField(
            value = value,
            onValueChange = {
                onValueChange(it)
            },
            singleLine = true,
            textStyle = TextStyle(
                fontSize = 14.sp,
                color = Color.Black,
                fontFamily = FontFamily(Font(fontFamily))
            ),
            modifier = modifier
        )
    }
}

@Composable
private fun LabelDropdown(titleSelector: String) {
    Box(
        modifier = Modifier
            .padding(start = 0.dp, top = 8.5.dp)
            .width(26.dp)
    ) {
        Text(
            titleSelector, style = TextStyle(
                fontSize = 14.sp, fontFamily = FontFamily(
                    Font(R.font.opensans_medium)
                )
            )
        )
    }
}