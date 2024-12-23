package com.mindeck.presentation.ui.components.textfields

import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle

@Composable
fun CardInputField(
    placeholder: String,
    value: String,
    singleLine: Boolean = false,
    textStyle: TextStyle,
    placeholderTextStyle: TextStyle,
    onValueChange: (String) -> Unit,
    modifier: Modifier
) {
    BasicTextField(
        value = value,
        onValueChange = {
            onValueChange(it)
        },
        textStyle = textStyle,
        singleLine = singleLine,
        decorationBox = { it ->
            if (value.isEmpty()) {
                Text(
                    text = placeholder,
                    color = MaterialTheme.colorScheme.outline,
                    style = placeholderTextStyle
                )
            }
            it()
        },
        modifier = modifier,
    )
}