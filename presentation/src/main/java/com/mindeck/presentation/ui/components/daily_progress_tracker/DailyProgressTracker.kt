package com.mindeck.presentation.ui.components.daily_progress_tracker

import androidx.annotation.PluralsRes
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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import com.mindeck.domain.models.Card
import com.mindeck.presentation.R
import com.mindeck.presentation.state.UiState
import com.mindeck.presentation.state.onSuccess
import com.mindeck.presentation.ui.components.utils.dimenDpResource
import com.mindeck.presentation.ui.components.utils.dimenFloatResource

@Composable
fun DailyProgressTracker(
    cardsRepetitionState: UiState<List<Card>>,
    dptIcon: Painter,
    dailyProgressTrackerState: DailyProgressTrackerState
) {

    val dptAnimationProgress = dailyProgressTrackerAnimationProgress(
        dptProgressFloat = dailyProgressTrackerState.dptProgress,
        animationDuration = dailyProgressTrackerState.animationDuration
    )

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.secondary,
                shape = MaterialTheme.shapes.medium
            )
    ) {
        Column(
            modifier = Modifier
                .padding(
                    start = dimenDpResource(R.dimen.daily_progress_tracker_start_padding),
                    top = dimenDpResource(R.dimen.daily_progress_tracker_top_padding),
                    end = dimenDpResource(R.dimen.daily_progress_tracker_end_padding),
                    bottom = dimenDpResource(R.dimen.daily_progress_tracker_bottom_padding)
                )
                .weight(dimenFloatResource(R.dimen.float_one_significance))
        ) {
            cardsRepetitionState.onSuccess { cardsRepetition ->
                DPTText(
                    plurals = R.plurals.deck_amount,
                    count = cardsRepetition.size,
                    textStyle = MaterialTheme.typography.labelMedium
                )
            }
            Spacer(modifier = Modifier.height(dimenDpResource(R.dimen.spacer_medium)))
            DPTProgress(
                dptAnimationProgress = dptAnimationProgress,
                progressColor = MaterialTheme.colorScheme.onPrimary,
                backProgressColor = MaterialTheme.colorScheme.outlineVariant
            )
        }
        IconBox(dptIcon, modifier = Modifier.align(Alignment.CenterVertically))
    }
}

@Composable
private fun DPTText(
    @PluralsRes plurals: Int,
    count: Int,
    textStyle: TextStyle
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = stringResource(R.string.text_is_necessary_to_repeat),
            style = textStyle
        )
        Spacer(modifier = Modifier.width(dimenDpResource(R.dimen.spacer_extra_small)))
        Text(
            text = pluralStringResource(plurals, count, count),
            style = textStyle
        )
    }
}

@Composable
private fun DPTProgress(
    dptAnimationProgress: Float,
    progressColor: Color,
    backProgressColor: Color
) {
    Box(
        modifier = Modifier
            .background(color = progressColor, shape = MaterialTheme.shapes.extraLarge)
            .height(dimenDpResource(R.dimen.spacer_extra_small))
            .fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(dptAnimationProgress)
                .background(backProgressColor, shape = MaterialTheme.shapes.extraLarge)
        )
    }
}

@Composable
private fun IconBox(dptIcon: Painter, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .padding(end = dimenDpResource(R.dimen.padding_medium))
    ) {
        Icon(
            modifier = Modifier.size(
                width = dimenDpResource(R.dimen.daily_progress_tracker_icon_width),
                height = dimenDpResource(R.dimen.daily_progress_tracker_icon_height)
            ),
            painter = dptIcon,
            tint = MaterialTheme.colorScheme.outlineVariant,
            contentDescription = stringResource(R.string.daily_progress_tracker)
        )
    }
}