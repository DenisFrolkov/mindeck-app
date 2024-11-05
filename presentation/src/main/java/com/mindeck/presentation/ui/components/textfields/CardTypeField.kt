package com.mindeck.presentation.ui.components.textfields

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mindeck.presentation.R
import com.mindeck.presentation.ui.theme.White

@Composable
fun CardTypeField(filedTitle: String, field: String, modifier: Modifier) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            filedTitle, style = TextStyle(
                fontSize = 12.sp, fontFamily = FontFamily(
                    Font(R.font.opensans_medium)
                )
            )
        )
        Spacer(modifier = Modifier.width(5.dp))
        Box(
            contentAlignment = Alignment.Center,
            modifier = modifier
                .background(color = White, shape = RoundedCornerShape(4.dp))
                .size(width = 120.dp, height = 30.dp)
                .border(
                    width = 0.25.dp,
                    color = Color.Black,
                    shape = RoundedCornerShape(4.dp)
                )
        ) {
            Text(
                field, style = TextStyle(
                    fontSize = 12.sp, fontFamily = FontFamily(
                        Font(R.font.opensans_medium)
                    )
                )
            )
        }
    }
}