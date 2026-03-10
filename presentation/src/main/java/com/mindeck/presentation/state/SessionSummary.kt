package com.mindeck.presentation.state

// Счётчики карточек для текущей сессии повторения.
// Отображаются на главном экране для информирования пользователя перед началом занятия.
data class SessionSummary(
    // Новые карточки (до дневного лимита)
    val newCount: Int,
    // Карточки в фазе обучения: LEARNING + LAPSE
    val learningCount: Int,
    // Карточки на повторении: REVIEW
    val reviewCount: Int,
) {
    val totalCount: Int get() = newCount + learningCount + reviewCount
}
