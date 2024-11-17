package com.mindeck.presentation.ui.components.dailyProgressTracker

import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mindeck.presentation.R
import com.mindeck.presentation.ui.theme.Blue
import com.mindeck.presentation.ui.theme.PaleBlue
import com.mindeck.presentation.ui.theme.White

@Composable
fun DailyProgressTracker() {

    val totalCards = 999
    val answeredCards = 40
    val progress = answeredCards / totalCards.toFloat()

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .background(color = PaleBlue, shape = RoundedCornerShape(10.dp))
    ) {
        Column(
            modifier = Modifier
                .padding(start = 11.dp, top = 8.dp, end = 12.dp, bottom = 10.dp)
                .weight(1f)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    "Необходимо повторить",
                    style = TextStyle(
                        fontSize = 10.sp, fontFamily = FontFamily(
                            Font(R.font.opensans_medium)
                        )
                    ),
                )
                Text(
                    " $totalCards",
                    style = TextStyle(
                        fontSize = 12.sp, fontFamily = FontFamily(
                            Font(R.font.opensans_medium)
                        )
                    ),
                )
                Text(
                    " карточек!",
                    style = TextStyle(
                        fontSize = 10.sp, fontFamily = FontFamily(
                            Font(R.font.opensans_medium)
                        )
                    ),
                )
            }

            Spacer(modifier = Modifier.height(10.dp))
            Box(
                modifier = Modifier
                    .background(White, shape = CircleShape)
                    .height(4.dp)
                    .fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(fraction = progress)
                        .background(Blue, shape = CircleShape)
                )
            }
        }
        Box(
            modifier = Modifier
                .padding(end = 10.dp)
                .align(Alignment.CenterVertically)
        ) {
            Icon(
                modifier = Modifier.size(width = 23.dp, height = 16.dp),
                painter = painterResource(R.drawable.dpt_icon),
                tint = Blue,
                contentDescription = "DPT"
            )
        }
    }
}