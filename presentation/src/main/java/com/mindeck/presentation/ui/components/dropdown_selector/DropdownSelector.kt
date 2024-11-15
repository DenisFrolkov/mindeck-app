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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import com.mindeck.presentation.ui.components.dropdown_selector.DropdownState.Companion.ITEM_HEIGHT
import com.mindeck.presentation.ui.components.dropdown_selector.DropdownState.Companion.MAX_VISIBLE_ITEMS
import com.mindeck.presentation.ui.theme.White

@Composable
fun DropdownSelector(
    titleSelector: String,
    selectorItem: String,
    selectorItemList: List<String> = listOf(
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
    modifier: Modifier,
    fontFamily: FontFamily,
) {
    val dropdownState =
        remember { DropdownState(expandedHeight = (MAX_VISIBLE_ITEMS * ITEM_HEIGHT.dp - 14.dp)) }

    var selectorItemMain by remember { mutableStateOf(selectorItem) }

    Row(
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .padding(top = 8.5.dp)
                .width(120.dp)
        ) {
            Text(
                titleSelector, style = TextStyle(
                    fontSize = 14.sp, fontFamily = fontFamily
                )
            )
        }
        Spacer(modifier = Modifier.width(5.dp))
        Column(modifier = Modifier.fillMaxWidth()) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = modifier
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
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                        if (dropdownState.isExpanded) {
                            dropdownState.reset()
                        } else {
                            dropdownState.open()
                        }
                    }
            ) {
                Text(
                    text = selectorItem, style = TextStyle(
                        fontSize = 14.sp, fontFamily = fontFamily
                    )
                )
            }
            if (dropdownState.isExpanded) {
                Column(
                    modifier = Modifier.border(
                        width = 0.25.dp,
                        color = Color.Black,
                        shape = RoundedCornerShape(
                            0.dp,
                            0.dp,
                            4.dp,
                            4.dp
                        )
                    )
                ) {
                    DropdownMenu(
                        modifier = modifier,
                        selectorItemList = selectorItemList,
                        onStringClick = { selectorItemMain = it },
                        fontFamily = fontFamily,
                        dropdownState = dropdownState
                    )
                }
            }
        }
    }
}
