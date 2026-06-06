package com.mindeck.feature.home.home

sealed interface DailyReviewUi {
    data class Pending(
        val totalCount: Int,
        val repeatedCount: Int,
        val newCount: Int,
        val newReviewCount: Int,
        val reviewCount: Int,
    ) : DailyReviewUi {
        val inProgress: Boolean get() = repeatedCount > 0
    }

    data object Completed : DailyReviewUi
}
