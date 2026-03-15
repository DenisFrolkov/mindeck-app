package com.mindeck.presentation.ui.components.textfields

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import com.mindeck.presentation.R
import com.mindeck.presentation.ui.theme.MindeckTheme

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

@Preview(showBackground = true)
@Composable
private fun CardInputFieldEmptyPreview() {
    MindeckTheme {
        CardInputField(
            placeholder = "Введите вопрос...",
            value = "",
            onValueChange = {},
            placeholderTextStyle = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onSurfaceVariant),
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    MaterialTheme.colorScheme.surface,
                    MaterialTheme.shapes.large,
                )
                .border(
                    dimensionResource(R.dimen.border_width_dot_two_five),
                    MaterialTheme.colorScheme.outline,
                    MaterialTheme.shapes.large,
                )
                .padding(dimensionResource(R.dimen.card_input_field_item_padding)),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CardInputFieldFilledPreview() {
    MindeckTheme {
        CardInputField(
            value = "Что такое sealed class?",
            onValueChange = {},
            placeholderTextStyle = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onSurfaceVariant),
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    MaterialTheme.colorScheme.surface,
                    MaterialTheme.shapes.large,
                )
                .border(
                    dimensionResource(R.dimen.border_width_dot_two_five),
                    MaterialTheme.colorScheme.outline,
                    MaterialTheme.shapes.large,
                )
                .padding(dimensionResource(R.dimen.card_input_field_item_padding)),
        )
    }
}
