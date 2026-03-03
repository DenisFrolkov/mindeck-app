package com.mindeck.presentation.ui.components.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.window.Dialog
import com.mindeck.presentation.R
import com.mindeck.presentation.ui.components.buttons.ActionHandlerButton
import com.mindeck.presentation.ui.components.utils.dimenDpResource
import com.mindeck.presentation.ui.theme.text_white

@Composable
fun DeleteModalWindow(
    titleText: String,
    bodyText: String,
    deleteButton: () -> Unit,
    onExitClick: () -> Unit,
) {
    Dialog(onDismissRequest = onExitClick) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .background(
                    color = MaterialTheme.colorScheme.background,
                    shape = MaterialTheme.shapes.small,
                )
                .clip(MaterialTheme.shapes.small)
                .padding(dimensionResource(R.dimen.dimen_8)),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                ActionHandlerButton(
                    iconPainter = painterResource(R.drawable.img_back),
                    contentDescription = stringResource(R.string.back_screen_icon_button),
                    iconTint = MaterialTheme.colorScheme.outlineVariant,
                    onClick = {
                        onExitClick()
                    },
                )
                Text(
                    text = titleText,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(1f),
                )
                ActionHandlerButton(
                    iconPainter = painterResource(R.drawable.create_deck_img),
                    contentDescription = stringResource(R.string.add_icon_button),
                    iconTint = MaterialTheme.colorScheme.outlineVariant,
                    iconSize = dimensionResource(R.dimen.dimen_24),
                    onClick = { },
                    modifier = Modifier.alpha(0f),
                )
            }

            Text(
                text = bodyText,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = dimenDpResource(R.dimen.padding_medium)),
            )
            Spacer(modifier = Modifier.height(dimenDpResource(R.dimen.spacer_large)))
            Box(
                modifier = Modifier
                    .background(
                        color = MaterialTheme.colorScheme.onSecondary,
                        shape = MaterialTheme.shapes.small,
                    )
                    .clickable {
                        deleteButton()
                    },
            ) {
                Text(
                    text = stringResource(R.string.delete_text),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.padding(
                        vertical = dimenDpResource(R.dimen.padding_small),
                        horizontal = dimenDpResource(R.dimen.padding_extra_large),
                    ),
                )
            }
        }
    }
}
