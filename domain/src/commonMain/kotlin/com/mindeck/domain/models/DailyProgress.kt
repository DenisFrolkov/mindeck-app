package com.mindeck.domain.models

data class DailyProgress(
    val reviewedCount: Int,
    val dueCount: Int,
) {
    val totalCount: Int get() = reviewedCount + dueCount

    val isCompleted: Boolean get() = reviewedCount > 0 && dueCount == 0
}
