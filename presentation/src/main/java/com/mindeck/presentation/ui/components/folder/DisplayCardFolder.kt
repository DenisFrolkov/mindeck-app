package com.mindeck.presentation.ui.components.folder

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mindeck.presentation.R
import com.mindeck.presentation.ui.theme.Gray
import com.mindeck.presentation.ui.theme.LightMint
import com.mindeck.presentation.ui.theme.LimeGreen
import com.mindeck.presentation.ui.theme.White

@Composable
fun DisplayCardFolder() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .border(0.25.dp, Gray, RoundedCornerShape(4.dp))
            .clip(shape = RoundedCornerShape(4.dp))
            .height(48.dp)
    ) {

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .background(White)
                .size(48.dp)
        ) {
            Text(
                text = "999+",
                style = TextStyle(fontSize = 14.sp),
                modifier = Modifier.padding()
            )
        }

        Spacer(
            modifier = Modifier
                .fillMaxHeight()
                .width(0.25.dp)
                .background(color = Gray)
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
                text = "Повторите, чтобы не забыть!", style = TextStyle(fontSize = 14.sp)
            )
        }

        Spacer(
            modifier = Modifier
                .fillMaxHeight()
                .width(0.25.dp)
                .background(color = Gray)
        )

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(48.dp)
                .background(color = LightMint)
        ) {
            Icon(
                modifier = Modifier.size(20.dp),
                painter = painterResource(R.drawable.repeat_card_item),
                tint = LimeGreen,
                contentDescription = ""
            )
        }
    }
}