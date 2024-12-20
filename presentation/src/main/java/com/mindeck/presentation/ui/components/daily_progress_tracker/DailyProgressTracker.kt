package com.mindeck.presentation.ui.components.daily_progress_tracker

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mindeck.presentation.R
import com.mindeck.presentation.ui.theme.outline_variant_blue
import com.mindeck.presentation.ui.theme.on_primary_white
import com.mindeck.presentation.ui.theme.scrim_black
import com.mindeck.presentation.ui.theme.secondary_light_blue

@Composable
fun DailyProgressTracker(
    dptIcon: Painter,
    dailyProgressTrackerState: DailyProgressTrackerState = DailyProgressTrackerState(
        totalCards = 500,
        answeredCards = 30
    ),
    cardForms: List<String> = listOf("карточку", "карточки", "карточек")
) {

    val textStyle = TextStyle(
        fontSize = 12.sp,
        fontFamily = FontFamily(Font(R.font.opensans_medium))
    )

    val dptAnimationProgress = dailyProgressTrackerAnimationProgress(
        dptProgressFloat = dailyProgressTrackerState.dptProgress,
        100
    )

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .background(color = secondary_light_blue, shape = RoundedCornerShape(10.dp))
    ) {
        Column(
            modifier = Modifier
                .padding(start = 11.dp, top = 8.dp, end = 12.dp, bottom = 10.dp)
                .weight(1f)
        ) {
            DPTText(
                dptState = dailyProgressTrackerState,
                textCompilation = getPluralForm(dailyProgressTrackerState.totalCards, cardForms),
                textStyle = textStyle
            )

            Spacer(modifier = Modifier.height(10.dp))

            DPTProgress(dptAnimationProgress)
        }
        IconBox(dptIcon, modifier = Modifier.align(Alignment.CenterVertically))
    }
}

fun getPluralForm(number: Int, forms: List<String>): String {
    val n = number % 100
    val n1 = number % 10
    return when {
        n in 11..19 -> forms[2]
        n1 == 1 -> forms[0]
        n1 in 2..4 -> forms[1]
        else -> forms[2]
    }
}

@Composable
private fun IconBox(dptIcon: Painter, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .padding(end = 16.dp)
    ) {
        Icon(
            modifier = Modifier.size(width = 23.dp, height = 16.dp),
            painter = dptIcon,
            tint = outline_variant_blue,
            contentDescription = stringResource(R.string.daily_progress_tracker)
        )
    }
}

@Composable
private fun DPTText(
    dptState: DailyProgressTrackerState,
    textCompilation: String,
    textStyle: TextStyle
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = stringResource(R.string.text_is_necessary_to_repeat),
            style = textStyle
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = "${dptState.totalCards}",
            style = textStyle.copy(fontSize = 14.sp),
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = "$textCompilation!",
            style = textStyle
        )
    }
}

@Composable
private fun DPTProgress(
    dptAnimationProgress: Float,
    progressColor: Color = on_primary_white,
    backProgressColor: Color = outline_variant_blue
) {
    Box(
        modifier = Modifier
            .background(progressColor, shape = CircleShape)
            .height(4.dp)
            .fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(fraction = dptAnimationProgress)
                .background(backProgressColor, shape = CircleShape)
        )
    }
}