package com.mindeck.feature.home.home

sealed interface DailyReviewUi {
    data class Pending(
        val totalCount: Int = 0,
        val repeatedCount: Int = 0,
        val newCount: Int = 0,
        val newReviewCount: Int = 0,
        val reviewCount: Int = 0,
    ) : DailyReviewUi {
        val inProgress: Boolean get() = repeatedCount > 0
    }

    data object Completed : DailyReviewUi
}
