package com.mindeck.presentation.ui.components.dropdown.dropdown_menu

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.mindeck.presentation.ui.theme.outline_variant_blue
import com.mindeck.presentation.ui.theme.on_primary_white

@Composable
fun DropdownMenu(listDropdownMenuItem: List<DropdownMenuData>, textStyle: TextStyle, dropdownModifier: Modifier) {
    Box(
        modifier = dropdownModifier
    ) {
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .background(on_primary_white, shape = RoundedCornerShape(8.dp))
                .border(
                    width = 0.25.dp,
                    color = outline_variant_blue,
                    shape = RoundedCornerShape(8.dp)
                )
        ) {
            items(listDropdownMenuItem) {
                Text(
                    text = it.title,
                    style = textStyle,
                    modifier = Modifier
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) { it.action.invoke() }
                        .padding(horizontal = 28.dp, vertical = 14.dp)
                )
            }
        }
    }
}