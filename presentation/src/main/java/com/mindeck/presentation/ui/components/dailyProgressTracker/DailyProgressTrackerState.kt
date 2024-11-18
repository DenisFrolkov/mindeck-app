package com.mindeck.presentation.ui.components.dailyProgressTracker

class DailyProgressTrackerState(val totalCards: Int = 0, private val answeredCards: Int = 0) {

    val dptProgress
        get() = answeredCards / totalCards.toFloat()

}
