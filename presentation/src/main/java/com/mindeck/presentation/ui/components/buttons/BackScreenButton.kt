package com.mindeck.presentation.ui.components.buttons

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.mindeck.presentation.R
import com.mindeck.presentation.ui.theme.Blue
import com.mindeck.presentation.ui.theme.White

@Composable
fun BackScreenButton() {
    Box(
        contentAlignment = Alignment.TopStart,
        modifier = Modifier
            .padding(start = 14.dp, top = 14.dp)
            .clip(shape = RoundedCornerShape(50.dp))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {

            }
    ) {
        Icon(
            painter = painterResource(R.drawable.back_icon),
            tint = White,
            contentDescription = "Back To Previous Page",
            modifier = Modifier
                .background(color = Blue, shape = RoundedCornerShape(50.dp))
                .padding(all = 12.dp)
                .size(size = 16.dp)
        )
    }
}