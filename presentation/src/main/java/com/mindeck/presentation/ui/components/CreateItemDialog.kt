package com.mindeck.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mindeck.presentation.R
import com.mindeck.presentation.ui.components.buttons.ActionHandlerButton
import com.mindeck.presentation.ui.components.buttons.SaveDataButton
import com.mindeck.presentation.ui.components.textfields.CardInputField
import com.mindeck.presentation.ui.theme.BackgroundScreen
import com.mindeck.presentation.ui.theme.Black
import com.mindeck.presentation.ui.theme.Blue
import com.mindeck.presentation.ui.theme.MediumGray
import com.mindeck.presentation.ui.theme.White

@Composable
fun CreateItemDialog(
    titleDialog: String,
    placeholder: String,
    buttonText: String,
    value: String,
    onValueChange: (String) -> Unit,
    onBackClick: () -> Unit,
    onClickButton: () -> Unit,
    fontFamily: FontFamily,
    titleTextStyle: TextStyle,
    textFontSize: TextUnit = 14.sp,
    placeholderFontSize: TextUnit = 14.sp,
    textColor: Color = Black,
    placeholderColor: Color = MediumGray,
    modifier: Modifier,
    iconModifier: Modifier,
    buttonModifier: Modifier
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxHeight(0.85f)
            .padding(horizontal = 60.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .background(color = BackgroundScreen, shape = RoundedCornerShape(6))
                .clip(RoundedCornerShape(6))
                .padding(10.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                ActionHandlerButton(
                    iconPainter = painterResource(R.drawable.back_icon),
                    contentDescription = stringResource(R.string.back_screen_icon_button),
                    iconModifier = iconModifier
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) {
                            onBackClick()
                        },
                )
                Text(
                    text = titleDialog,
                    style = titleTextStyle,
                    color = Black,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Spacer(modifier = Modifier.height(30.dp))
            CardInputField(
                placeholder = placeholder,
                value = value,
                singleLine = true,
                onValueChange = onValueChange,
                fontFamily = fontFamily,
                textFontSize = textFontSize,
                placeholderFontSize = placeholderFontSize,
                textColor = textColor,
                placeholderColor = placeholderColor,
                modifier = modifier
                    .fillMaxWidth()
                    .background(
                        White
                    )
                    .border(
                        width = 0.25.dp,
                        color = MediumGray,
                        shape = RoundedCornerShape(4.dp)
                    )
                    .padding(10.dp)
            )
            Spacer(modifier = Modifier.height(30.dp))
            SaveDataButton(
                text = buttonText,
                onClick = onClickButton,
                fontSize = 14.sp,
                fontFamily = fontFamily,
                color = White,
                buttonModifier = buttonModifier
            )
        }
    }
}