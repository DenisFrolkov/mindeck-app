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
fun CardInputField(
    placeholder: String,
    value: String,
    singleLine: Boolean = false,
    onValueChange: (String) -> Unit,
    fontFamily: FontFamily,
    textFontSize: TextUnit = 14.sp,
    placeholderFontSize: TextUnit = 14.sp,
    textColor: Color = scrim_black,
    placeholderColor: Color = outline_medium_gray,
    modifier: Modifier
) {
    BasicTextField(
        value = value,
        onValueChange = {
            onValueChange(it)
        },
        textStyle = TextStyle(
            fontSize = textFontSize,
            color = textColor,
            fontFamily = fontFamily,
        ),
        singleLine = singleLine,
        decorationBox = { it ->
            if (value.isEmpty()) {
                Text(
                    text = placeholder,
                    color = placeholderColor,
                    style = TextStyle(
                        fontSize = placeholderFontSize,
                        fontFamily = fontFamily
                    )
                )
            }
            it()
        },
        modifier = modifier,
    )
}