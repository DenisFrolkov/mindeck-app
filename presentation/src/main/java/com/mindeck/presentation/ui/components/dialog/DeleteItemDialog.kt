package com.mindeck.presentation.ui.components.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.mindeck.presentation.R
import com.mindeck.presentation.ui.components.buttons.DeleteItemButton
import com.mindeck.presentation.ui.components.utils.dimenDpResource
import com.mindeck.presentation.ui.components.utils.dimenFloatResource

@Composable
fun DeleteItemDialog(
    onClickDeleteAll: () -> Unit,
    onClickDeletePartially: () -> Unit,
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
                .background(
                    color = MaterialTheme.colorScheme.background,
                    shape = MaterialTheme.shapes.small
                )
                .clip(MaterialTheme.shapes.small)
                .padding(dimenDpResource(R.dimen.card_input_field_item_padding))
        ) {
            Text(
                text = "stringResource(R.string.folder_management_options_message)",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Start,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(dimenDpResource(R.dimen.spacer_medium)))
            Row(
                horizontalArrangement = Arrangement.SpaceAround,
                modifier = Modifier.fillMaxWidth()
            ) {
                DeleteItemButton(
                    titleButton = stringResource(R.string.delete_all_button_text),
                    onClick = {
                        onClickDeleteAll()
                    }
                )
                DeleteItemButton(
                    titleButton = stringResource(R.string.delete_partially_button_text),
                    onClick = {
                        onClickDeletePartially()
                    }
                )
            }
        }
    }
}