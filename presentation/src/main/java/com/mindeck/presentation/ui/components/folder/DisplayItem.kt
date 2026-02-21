package com.mindeck.presentation.ui.components.folder

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import com.mindeck.presentation.R
import com.mindeck.presentation.ui.components.dataclasses.DisplayItemData
import com.mindeck.presentation.ui.components.dataclasses.DisplayItemStyle
import com.mindeck.presentation.ui.components.utils.dimenDpResource
import com.mindeck.presentation.ui.components.utils.formatNumber

@Composable
fun DisplayItem(
    modifier: Modifier = Modifier,
    shape: CornerBasedShape,
    showCount: Boolean,
    displayItemData: DisplayItemData,
    displayItemStyle: DisplayItemStyle,
    onClick: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .border(
                dimensionResource(R.dimen.dimen_0_25),
                MaterialTheme.colorScheme.outline,
                shape,
            )
            .clip(shape = shape)
            .height(dimensionResource(R.dimen.dimen_48))
            .clickable(onClick = onClick),
    ) {
        if (showCount) {
            Text(
                text = formatNumber(displayItemData.numberOfCards),
                style = displayItemStyle.textStyle,
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.onPrimary)
                    .size(dimenDpResource(R.dimen.dimen_48))
                    .wrapContentSize(Alignment.Center),
            )

            VerticalDivider(
                modifier = Modifier.fillMaxHeight(),
                thickness = dimenDpResource(R.dimen.dimen_0_25),
                color = MaterialTheme.colorScheme.outline,
            )
        }

        Box(
            contentAlignment = Alignment.CenterStart,
            modifier = Modifier
                .weight(1f)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.onPrimary)
                .padding(horizontal = dimenDpResource(R.dimen.dimen_8)),
        ) {
            Text(
                text = displayItemData.itemName,
                maxLines = displayItemStyle.textMaxLines,
                overflow = TextOverflow.Ellipsis,
                style = displayItemStyle.textStyle,
            )
        }

        VerticalDivider(
            modifier = Modifier.fillMaxHeight(),
            thickness = dimenDpResource(R.dimen.dimen_0_25),
            color = MaterialTheme.colorScheme.outline,
        )

        Box(
            modifier = Modifier
                .size(dimenDpResource(R.dimen.dimen_48))
                .background(color = displayItemStyle.backgroundColor),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                modifier = Modifier.size(dimenDpResource(R.dimen.dimen_24)),
                painter = painterResource(displayItemData.itemIcon),
                tint = displayItemStyle.iconColor,
                contentDescription = null,
            )
        }
    }
}
