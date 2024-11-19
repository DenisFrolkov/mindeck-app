package com.mindeck.presentation.ui.components.daily_progress_tracker

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import com.mindeck.presentation.R

@Composable
fun dailyProgressTrackerAnimationProgress(dptProgressFloat: Float, animationDuration: Int) : Float{
    val dptAnimationProgress by animateFloatAsState(
        targetValue = dptProgressFloat,
        animationSpec = tween(durationMillis = animationDuration), label = stringResource(R.string.label_animated_daily_progress_tracker_float)
    )

    return dptAnimationProgress
}