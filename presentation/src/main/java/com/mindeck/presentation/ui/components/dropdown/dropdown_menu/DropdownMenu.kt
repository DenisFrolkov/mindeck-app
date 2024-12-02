package com.mindeck.presentation.ui.components.dropdown.dropdown_menu

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mindeck.presentation.R
import com.mindeck.presentation.ui.theme.Blue
import com.mindeck.presentation.ui.theme.White

@Composable
fun DropdownMenu(listDropdownMenuItem: List<DropdownMenuData>, dropdownModifier: Modifier) {
    Box(
        modifier = dropdownModifier
            .fillMaxWidth()
            .padding(top = 4.dp)
            .wrapContentSize(Alignment.TopEnd)
    ) {
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .background(White)
                .border(
                    width = 0.25.dp,
                    color = Blue,
                    shape = RoundedCornerShape(8.dp)
                )
        ) {
            items(listDropdownMenuItem) {
                Text(
                    text = it.title,
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontFamily = FontFamily(Font(R.font.opensans_medium))
                    ),
                    modifier = Modifier
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) { it.action }
                        .padding(horizontal = 28.dp, vertical = 14.dp)
                )
            }
        }
    }
}