package com.mindeck.presentation.ui.components.fab

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import com.mindeck.presentation.R
import com.mindeck.presentation.ui.theme.White

@Composable
fun FABMenu(listItemsMenu: List<FabMenuDataClass>) {
    listItemsMenu.forEach { menuItem ->
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Text(
                menuItem.text, style = TextStyle(
                    color = White, fontFamily = FontFamily(
                        Font(R.font.opensans_medium)
                    )
                ),
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(vertical = 3.dp)
            )
            Icon(
                painter = menuItem.icon,
                contentDescription = menuItem.iconContentDescription,
                modifier = Modifier.size(18.dp),
                tint = White
            )
        }
        if (menuItem.idItem != listItemsMenu.size - 1) {
            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                thickness = 1.dp,
                color = White
            )
        }
    }
}