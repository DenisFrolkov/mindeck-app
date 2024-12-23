package com.mindeck.presentation.ui.components.daily_progress_tracker

class DailyProgressTrackerState(
    val totalCards: Int = 0,
    private val answeredCards: Int = 0,
    val animationDuration: Int = 100
) {

    val dptProgress
        get() = answeredCards / totalCards.toFloat()

}
