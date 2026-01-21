package com.mindeck.presentation.ui.components.textfields

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle

@Composable
fun TitleInputField(
    placeholder: String,
    value: String,
    singleLine: Boolean = false,
    onValueChange: (String) -> Unit,
    readOnly: Boolean,
    textStyle: TextStyle,
    placeholderTextStyle: TextStyle,
    modifier: Modifier,
) {
    Box(
        modifier = modifier,
    ) {
        BasicTextField(
            value = value,
            onValueChange = { onValueChange(it) },
            textStyle = textStyle,
            singleLine = singleLine,
            readOnly = readOnly,
            decorationBox = { innerTextField ->
                Box(
                    contentAlignment = Alignment.CenterStart,
                    modifier = Modifier.fillMaxSize(),
                ) {
                    if (value.isEmpty()) {
                        Text(
                            text = placeholder,
                            style = placeholderTextStyle,
                        )
                    }
                    innerTextField()
                }
            },
            modifier = Modifier.fillMaxSize(),
        )
    }
}
