package com.mindeck.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.mindeck.presentation.R
import com.mindeck.presentation.ui.components.buttons.ActionHandlerButton
import com.mindeck.presentation.ui.components.buttons.SaveDataButton
import com.mindeck.presentation.ui.components.textfields.CardInputField
import com.mindeck.presentation.ui.components.utils.dimenDpResource
import com.mindeck.presentation.ui.components.utils.dimenFloatResource
import com.mindeck.presentation.ui.theme.background_light_blue
import com.mindeck.presentation.ui.theme.scrim_black
import com.mindeck.presentation.ui.theme.on_primary_white
import com.mindeck.presentation.ui.theme.text_white

@Composable
fun CreateItemDialog(
    titleDialog: String,
    placeholder: String,
    buttonText: String,
    value: String,
    onValueChange: (String) -> Unit,
    onBackClick: () -> Unit,
    onClickButton: () -> Unit,
    modifier: Modifier,
    iconModifier: Modifier,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxHeight(dimenFloatResource(R.dimen.alpha_menu_dialog_height))
            .padding(horizontal = dimenDpResource(R.dimen.card_input_field_background_horizontal_padding))
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .background(color = background_light_blue, shape = MaterialTheme.shapes.small)
                .clip(MaterialTheme.shapes.small)
                .padding(dimenDpResource(R.dimen.card_input_field_item_padding))
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                ActionHandlerButton(
                    iconPainter = painterResource(R.drawable.back_icon),
                    contentDescription = stringResource(R.string.back_screen_icon_button),
                    onClick = onBackClick,
                    iconModifier = iconModifier,
                )
                Text(
                    text = titleDialog,
                    style = MaterialTheme.typography.bodyMedium,
                    color = scrim_black,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Spacer(modifier = Modifier.height(dimenDpResource(R.dimen.spacer_medium)))
            CardInputField(
                placeholder = placeholder,
                value = value,
                singleLine = true,
                onValueChange = onValueChange,
                modifier = modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.onPrimary)
                    .border(
                        dimenDpResource(R.dimen.border_width),
                        MaterialTheme.colorScheme.outline,
                        MaterialTheme.shapes.extraSmall
                    )
                    .padding(dimenDpResource(R.dimen.card_input_field_item_padding))
            )
            Spacer(modifier = Modifier.height(dimenDpResource(R.dimen.spacer_medium)))
            SaveDataButton(
                text = buttonText,
                textStyle = MaterialTheme.typography.bodyMedium.copy(
                    color = text_white
                ),
                buttonModifier = Modifier
                    .background(
                        color = MaterialTheme.colorScheme.outlineVariant,
                        shape = MaterialTheme.shapes.medium
                    )
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                        onClickButton()
                    }
            )
        }
    }
}