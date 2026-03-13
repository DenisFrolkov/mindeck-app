package com.mindeck.domain.models

data class Card(
    val cardId: Int = 0,
    val cardName: String,
    val cardQuestion: String,
    val cardAnswer: String,
    val cardType: CardType,
    val cardTag: String,
    val deckId: Int,

    // --- Поля системы интервального повторения (SM-2) ---

    // Текущее состояние карточки в алгоритме
    val cardState: CardState = CardState.NEW,

    // Коэффициент лёгкости (Ease Factor). Начальное значение: 2.5, минимум: 1.3
    val easeFactor: Float = 2.5f,

    // Текущий интервал в днях (Float для сохранения дробной части при умножении — избегает застревания)
    val interval: Float = 0f,

    // Текущий шаг в фазе LEARNING/LAPSE: 0 = 1 мин, 1 = 10 мин
    val learningStep: Int = 0,

    // Unix timestamp следующего показа карточки
    val nextReviewDate: Long? = null,

    // Общее количество повторений карточки
    val repetitionCount: Int = 0,

    // Сколько раз карточка падала из REVIEW обратно в LEARNING
    val lapseCount: Int = 0,

    // Дата первого показа (используется для подсчёта дневного лимита новых карточек)
    val firstReviewDate: Long? = null,

    // Дата последнего показа (для статистики)
    val lastReviewDate: Long? = null,
) {
    init {
        require(cardName.isNotBlank()) { "Card name must not be blank" }
        require(cardQuestion.isNotBlank()) { "Card question must not be blank" }
        require(cardAnswer.isNotBlank()) { "Card answer must not be blank" }
    }
}
