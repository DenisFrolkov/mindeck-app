package com.mindeck.presentation.ui.components.dropdown_selector

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.mindeck.presentation.ui.theme.White

@Composable
fun DropdownSelector(
    title: String,
    selectedItem: String,
    items: List<String> = listOf(
        "12312312",
        "12312312",
        "12312312",
        "12312312",
        "12312312",
        "12312312",
        "12312312",
        "12312312",
        "12312312",
        "12312312",
        "12312312",
        "12312312",
        "12312312"
    ),
    onItemClick: (String) -> Unit,
    textStyle: TextStyle,
    modifier: Modifier,
    titleModifier: Modifier
) {
    val dropdownState = remember { DropdownState() }

    val baseSelectorModifier = modifier
        .fillMaxWidth()
        .background(color = White, shape = RoundedCornerShape(4.dp))
        .height(height = 36.dp)
        .border(
            width = 0.25.dp,
            color = Color.Black,
            shape = (if (dropdownState.isExpanded) RoundedCornerShape(
                topStart = 4.dp,
                topEnd = 4.dp,
            ) else RoundedCornerShape(4.dp))
        )
        .wrapContentSize(Alignment.Center)
        .clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = null
        ) {
            dropdownState.toggle()
        }

    val baseSelectorItemModifier = Modifier
        .border(
            width = 0.25.dp,
            color = Color.Black,
            shape = RoundedCornerShape(
                bottomStart = 4.dp,
                bottomEnd = 4.dp
            )
        )

    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            title, style = textStyle,
            modifier = titleModifier
                .width(120.dp)
        )
        Spacer(modifier = Modifier.width(5.dp))
        Column(modifier = Modifier.fillMaxWidth()) {
            Box(
                modifier = baseSelectorModifier
            ) {
                Text(
                    text = selectedItem, style = textStyle
                )
            }
            if (dropdownState.isExpanded) {
                Column(
                    modifier = baseSelectorItemModifier
                ) {
                    DropdownMenu(
                        selectorItemList = items,
                        onStringClick = onItemClick,
                        dropdownState = dropdownState,
                        modifier = modifier,
                        textStyle = textStyle
                    )
                }
            }
        }
    }
}