package com.mindeck.presentation.ui.components.folder

import android.annotation.SuppressLint
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mindeck.presentation.ui.theme.MediumGray
import com.mindeck.presentation.ui.theme.LightMint
import com.mindeck.presentation.ui.theme.LimeGreen
import com.mindeck.presentation.ui.theme.White

@SuppressLint("SupportAnnotationUsage")
@Composable
fun DisplayCardFolder(
    numberOfCards: Int,
    @StringRes folderName: String,
    @DrawableRes folderIcon: Int,
    modifier: Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .border(0.25.dp, MediumGray, RoundedCornerShape(4.dp))
            .clip(shape = RoundedCornerShape(4.dp))
            .height(48.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {

            }
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .background(White)
                .size(48.dp)
        ) {
            Text(
                text = if (numberOfCards > 999) "999+" else "$numberOfCards",
                style = TextStyle(fontSize = 14.sp),
                modifier = Modifier.padding()
            )
        }

        Spacer(
            modifier = Modifier
                .fillMaxHeight()
                .width(0.25.dp)
                .background(color = MediumGray)
        )

        Box(
            contentAlignment = Alignment.CenterStart,
            modifier = Modifier
                .weight(1f)
                .fillMaxSize()
                .background(White)
                .padding(horizontal = 6.dp)
        ) {
            Text(
                text = folderName,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = TextStyle(fontSize = 14.sp)
            )
        }

        Spacer(
            modifier = Modifier
                .fillMaxHeight()
                .width(0.25.dp)
                .background(color = MediumGray)
        )

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(48.dp)
                .background(color = LightMint)
        ) {
            Icon(
                modifier = Modifier.size(20.dp),
                painter = painterResource(folderIcon),
                tint = LimeGreen,
                contentDescription = "Folder Icon"
            )
        }
    }
}