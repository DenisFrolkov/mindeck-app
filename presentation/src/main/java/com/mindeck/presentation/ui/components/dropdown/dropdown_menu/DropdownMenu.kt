package com.mindeck.presentation.ui.components.dropdown.dropdown_menu

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.mindeck.presentation.R
import com.mindeck.presentation.ui.components.utils.dimenDpResource

@Composable
fun DropdownMenu(
    listDropdownMenuItem: List<DropdownMenuData>,
    dropdownModifier: Modifier
) {
    Box(
        modifier = dropdownModifier
    ) {
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .background(
                    MaterialTheme.colorScheme.onPrimary,
                    shape = MaterialTheme.shapes.medium
                )
                .border(
                    width = dimenDpResource(R.dimen.border_width_dot_two_five),
                    color = MaterialTheme.colorScheme.outlineVariant,
                    shape = MaterialTheme.shapes.medium
                )
        ) {
            items(listDropdownMenuItem) { item ->
                Text(
                    text = item.title,
                    style = item.titleStyle,
                    modifier = Modifier
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) { item.action.invoke() }
                        .padding(
                            horizontal = dimenDpResource(R.dimen.dropdown_menu_horizontal_text_padding),
                            vertical = dimenDpResource(R.dimen.dropdown_menu_vertical_text_padding)
                        )
                )
            }
        }
    }
}