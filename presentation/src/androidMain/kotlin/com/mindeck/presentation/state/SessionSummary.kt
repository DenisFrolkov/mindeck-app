package com.mindeck.presentation.state

data class SessionSummary(
    val newCount: Int,
    val learningCount: Int,
    val reviewCount: Int,
) {
    val totalCount: Int get() = newCount + learningCount + reviewCount
}
