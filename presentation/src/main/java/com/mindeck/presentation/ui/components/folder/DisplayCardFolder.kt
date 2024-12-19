package com.mindeck.presentation.ui.components.folder

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.mindeck.presentation.R
import com.mindeck.presentation.ui.theme.outline_medium_gray
import com.mindeck.presentation.ui.theme.on_primary_white

@Composable
fun DisplayCardFolder(
    folderIcon: Painter,
    numberOfCards: Int,
    folderName: String,
    backgroundColor: Color,
    iconColor: Color,
    onClick: () -> Unit,
    textStyle: TextStyle,
    modifier: Modifier = Modifier
) {

    fun getSpacerModifier(color: Color = outline_medium_gray, width: Dp) = Modifier
        .fillMaxHeight()
        .width(width)
        .background(color = color)

    fun formatNumber(number: Int, limit: Int = 999): String {
        return if (number > limit) "$limit+" else number.toString()
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .border(0.25.dp, outline_medium_gray, RoundedCornerShape(4.dp))
            .clip(shape = RoundedCornerShape(4.dp))
            .height(48.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                onClick()
            }
    ) {

        Text(
            text = formatNumber(numberOfCards),
            style = textStyle,
            modifier = Modifier
                .background(on_primary_white)
                .size(48.dp)
                .wrapContentSize(Alignment.Center)
        )

        Spacer(modifier = getSpacerModifier(width = 0.25.dp, color = outline_medium_gray))

        Box(
            contentAlignment = Alignment.CenterStart,
            modifier = Modifier
                .weight(1f)
                .fillMaxSize()
                .background(on_primary_white)
                .padding(horizontal = 6.dp)
        ) {
            Text(
                text = folderName,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = textStyle,
            )
        }

        Spacer(modifier = getSpacerModifier(width = 0.25.dp, color = outline_medium_gray))

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(48.dp)
                .background(color = backgroundColor)
        ) {
            Icon(
                modifier = Modifier.size(20.dp),
                painter = folderIcon,
                tint = iconColor,
                contentDescription = stringResource(R.string.folder_icon)
            )
        }
    }
}