package com.mindeck.presentation.ui.components.textfields

import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.sp

@Composable
fun CardInputField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier,
    fontFamily: Int
) {
    BasicTextField(
        value = value,
        onValueChange = {
            onValueChange(it)
        },
        textStyle = TextStyle(
            fontSize = 14.sp,
            color = Color.Black,
            fontFamily = FontFamily(Font(fontFamily)),
        ),
        singleLine = false,
        decorationBox = { it ->
            if (value.isEmpty()) {
                Text(
                    text = placeholder,
                    color = Color.Gray,
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontFamily = FontFamily(Font(fontFamily))
                    )
                )
            }
            it()
        },
        modifier = modifier,
    )
}