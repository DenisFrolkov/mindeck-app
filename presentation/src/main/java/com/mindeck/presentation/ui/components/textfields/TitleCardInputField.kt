package com.mindeck.presentation.ui.components.textfields

import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.mindeck.presentation.ui.theme.scrim_black
import com.mindeck.presentation.ui.theme.outline_medium_gray

@Composable
fun TitleInputField(
    placeholder: String,
    value: String,
    onValueChange: (String) -> Unit,
    textStyle: TextStyle,
    placeholderTextStyle: TextStyle,
    modifier: Modifier,
) {
    BasicTextField(
        value = value,
        onValueChange = {
            onValueChange(it)
        },
        textStyle = textStyle,
        singleLine = true,
        decorationBox = { it ->
            if (value.isEmpty()) {
                Text(
                    text = placeholder,
                    style = placeholderTextStyle
                )
            }
            it()
        },
        modifier = modifier,
    )
}
