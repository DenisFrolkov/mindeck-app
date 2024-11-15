package com.mindeck.presentation.ui.components.textfields

import android.annotation.SuppressLint
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mindeck.presentation.ui.theme.Black

@Composable
fun TegInputField(
    titleTextInput: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier,
    fontFamily: FontFamily,
    titleFontSize: TextUnit = 14.sp,
    textFontSize: TextUnit = 14.sp,
    titleColor: Color = Black,
    textColor: Color = Black,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = titleTextInput,
            style = TextStyle(
                fontSize = titleFontSize,
                color = titleColor,
                fontFamily = fontFamily
            )
        )
        Spacer(modifier = Modifier.width(20.dp))
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            singleLine = true,
            textStyle = TextStyle(
                fontSize = textFontSize,
                color = textColor,
                fontFamily = fontFamily
            ),
            modifier = modifier
        )
    }
}
