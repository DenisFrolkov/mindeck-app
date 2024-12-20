package com.mindeck.presentation.ui.components.textfields

import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.MaterialTheme
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
import com.mindeck.presentation.ui.theme.text_gray

@Composable
fun CardInputField(
    placeholder: String,
    value: String,
    singleLine: Boolean = false,
    onValueChange: (String) -> Unit,
    modifier: Modifier
) {
    BasicTextField(
        value = value,
        onValueChange = {
            onValueChange(it)
        },
        textStyle = MaterialTheme.typography.bodyMedium,
        singleLine = singleLine,
        decorationBox = { it ->
            if (value.isEmpty()) {
                Text(
                    text = placeholder,
                    color = MaterialTheme.colorScheme.outline,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = text_gray
                    )
                )
            }
            it()
        },
        modifier = modifier,
    )
}