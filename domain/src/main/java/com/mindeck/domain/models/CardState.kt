package com.mindeck.domain.models

// Состояние карточки в системе интервального повторения (SM-2).
// Переходы: NEW → LEARNING → REVIEW
//           REVIEW --[Again]--> LAPSE → LEARNING
enum class CardState {
    // Карточка ещё ни разу не показывалась
    NEW,

    // Карточка в фазе обучения: короткие интервалы (1 мин → 10 мин)
    LEARNING,

    // Карточка выучена: длинные интервалы (дни / недели / месяцы)
    REVIEW,

    // Карточка была в REVIEW, но пользователь её забыл — возвращается в LEARNING
    LAPSE,
}
