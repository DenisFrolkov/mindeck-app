package com.mindeck.presentation.ui.components.textfields

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mindeck.presentation.R
import com.mindeck.presentation.ui.components.utils.dimenDpResource
import com.mindeck.presentation.ui.theme.scrim_black

@Composable
fun TegInputField(
    titleTextInput: String,
    value: String,
    onValueChange: (String) -> Unit,
    textStyle: TextStyle,
    placeholderTextStyle: TextStyle,
    modifier: Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = titleTextInput,
            style = textStyle
        )
        Spacer(modifier = Modifier.width(dimenDpResource(R.dimen.spacer_large)))
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            singleLine = true,
            textStyle = placeholderTextStyle,
            modifier = modifier
        )
    }
}
