package com.mindeck.presentation.ui.components.fab

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.mindeck.presentation.ui.theme.on_primary_white

@Composable
fun FABMenu(fabState: FabState, listItemsMenu: List<FabMenuData>, textStyle: TextStyle, iconColor: Color) {

    listItemsMenu.forEach { menuItem ->
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) {
                    fabState.reset()
                    menuItem.navigation.invoke()
                }
        ) {
            Text(
                menuItem.text, style = textStyle,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(vertical = 3.dp)
            )
            Icon(
                painter = painterResource(menuItem.icon),
                contentDescription = menuItem.iconContentDescription,
                modifier = Modifier.size(18.dp),
                tint = iconColor
            )
        }
        if (menuItem.idItem != listItemsMenu.size - 1) {
            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                thickness = 1.dp,
                color = on_primary_white
            )
        }
    }
}