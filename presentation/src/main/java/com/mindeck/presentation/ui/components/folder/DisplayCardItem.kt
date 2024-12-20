package com.mindeck.presentation.ui.components.folder

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.mindeck.presentation.R
import com.mindeck.presentation.ui.components.utils.dimenResource
import com.mindeck.presentation.ui.components.utils.formatNumber
import com.mindeck.presentation.ui.components.utils.getSpacerModifier
import com.mindeck.presentation.ui.theme.on_primary_white

@Composable
fun DisplayCardItem(
    showCount: Boolean,
    itemIcon: Painter,
    numberOfCards: Int = 0,
    itemName: String,
    backgroundColor: Color,
    iconColor: Color,
    textStyle: TextStyle,
    textMaxLines: Int = 1,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {

        if (showCount) {
            Text(
                text = formatNumber(numberOfCards),
                style = textStyle,
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.onPrimary)
                    .size(dimenResource(R.dimen.display_card_item_size))
                    .wrapContentSize(Alignment.Center)
            )

            VerticalDivider(
                modifier = Modifier.fillMaxHeight(),
                thickness = dimenResource(R.dimen.vertical_divider_height),
                color = MaterialTheme.colorScheme.outline
            )
        }

        Box(
            contentAlignment = Alignment.CenterStart,
            modifier = Modifier
                .weight(1f)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.onPrimary)
                .padding(horizontal = dimenResource(R.dimen.padding_extra_small))
        ) {
            Text(
                text = itemName,
                maxLines = textMaxLines,
                overflow = TextOverflow.Ellipsis,
                style = textStyle,
            )
        }

        VerticalDivider(
            modifier = Modifier.fillMaxHeight(),
            thickness = dimenResource(R.dimen.vertical_divider_height),
            color = MaterialTheme.colorScheme.outline
        )

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(dimenResource(R.dimen.display_card_item_size))
                .background(color = backgroundColor)
        ) {
            Icon(
                modifier = Modifier.size(dimenResource(R.dimen.icon_size)),
                painter = itemIcon,
                tint = iconColor,
                contentDescription = stringResource(R.string.folder_icon)
            )
        }
    }
}