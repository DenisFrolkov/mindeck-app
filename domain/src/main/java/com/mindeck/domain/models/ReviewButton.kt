package com.mindeck.domain.models

// Кнопки оценки, которые пользователь нажимает при повторении карточки.
// Маппинг на качество SM-2 (q):
//   AGAIN → q=1  (не знаю / забыл)
//   HARD  → q=3  (знаю, но с трудом)
//   GOOD  → q=4  (знаю нормально)
//   EASY  → q=5  (знаю отлично)
enum class ReviewButton {
    AGAIN,
    HARD,
    GOOD,
    EASY,
}
