package com.mindeck.presentation.ui.components.textfields

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle

@Composable
fun CardInputField(
    placeholder: String = "",
    value: String,
    singleLine: Boolean = false,
    enabled: Boolean = true,
    placeholderTextStyle: TextStyle,
    onValueChange: (String) -> Unit,
    modifier: Modifier,
) {
    BasicTextField(
        value = value,
        onValueChange = { onValueChange(it) },
        enabled = enabled,
        textStyle = MaterialTheme.typography.bodyMedium.copy(MaterialTheme.colorScheme.onBackground),
        cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
        singleLine = singleLine,
        decorationBox = { innerTextField ->
            Box(
                contentAlignment = Alignment.CenterStart,
                modifier = Modifier.fillMaxWidth(),
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
        modifier = modifier,
    )
}
