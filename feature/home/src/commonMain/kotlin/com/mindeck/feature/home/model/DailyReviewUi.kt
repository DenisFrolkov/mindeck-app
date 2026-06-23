package com.mindeck.feature.home.model

sealed interface DailyReviewUi {
    data class Pending(
        val totalCount: Int = 0,
        val reviewedCount: Int = 0,
        val newCount: Int = 0,
        val newReviewCount: Int = 0,
        val reviewCount: Int = 0,
    ) : DailyReviewUi {
        val inProgress: Boolean get() = reviewedCount > 0
    }

    data object Completed : DailyReviewUi
}
