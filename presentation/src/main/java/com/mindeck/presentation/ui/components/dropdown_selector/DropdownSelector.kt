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
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mindeck.presentation.R
import com.mindeck.presentation.ui.theme.White

@Composable
fun DropdownSelector(titleSelector: String, selectorItem: String, modifier: Modifier) {

    var selectorItemMain by remember { mutableStateOf(selectorItem) }

    var clickDropdown by remember { mutableStateOf(true) }

    val selectorItemList = listOf(
        "123",
        "fasfdf",
        "23123124",
        "123",
        "fasfdf",
        "23123124",
        "123",
        "23123124",
    )

    Row(
        verticalAlignment = Alignment.Top
    ) {
        LabelDropdown(titleSelector)
        Spacer(modifier = Modifier.width(5.dp))
        Dropdown(modifier, clickDropdown, selectorItemMain, selectorItemList, onStringClick = { selectorItemMain = it }) {
            clickDropdown = it
        }
    }
}

@Composable
private fun LabelDropdown(titleSelector: String) {
    Box(
        modifier = Modifier
            .padding(start = 0.dp, top = 8.5.dp)
            .width(120.dp)
    ) {
        Text(
            titleSelector, style = TextStyle(
                fontSize = 14.sp, fontFamily = FontFamily(
                    Font(R.font.opensans_medium)
                )
            )
        )
    }
}

@Composable
private fun Dropdown(
    modifier: Modifier,
    clickDropdown: Boolean,
    selectorItem: String,
    selectorItemList: List<String>,
    onStringClick: (String) -> Unit,
    onClick: (Boolean) -> Unit
) {
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
                    shape = (if (clickDropdown) RoundedCornerShape(4.dp) else RoundedCornerShape(
                        topStart = 4.dp,
                        topEnd = 4.dp,
                    ))
                )
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) {
                    onClick(!clickDropdown)
                }
        ) {
            Text(
                text = selectorItem, style = TextStyle(
                    fontSize = 14.sp, fontFamily = FontFamily(
                        Font(R.font.opensans_medium)
                    )
                )
            )
        }
        if (clickDropdown == false) {
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
                DropDownMenu(modifier, selectorItemList, onStringClick, onClick)
            }
        }
    }
}