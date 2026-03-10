package com.mindeck.domain.usecases.card.command

import com.mindeck.domain.models.Card
import com.mindeck.domain.models.CardState
import com.mindeck.domain.models.ReviewButton
import com.mindeck.domain.repository.CardRepetitionRepository
import com.mindeck.domain.repository.ClockRepository
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.math.ceil

class UpdateCardReviewUseCase @Inject constructor(
    private val cardRepetitionRepository: CardRepetitionRepository,
    private val clock: ClockRepository,
) {
    // Возвращает обновлённый объект карточки — нужен ViewModel-у для корректного
    // обновления in-memory очереди сессии (иначе очередь хранила бы устаревшее состояние)
    suspend operator fun invoke(card: Card, button: ReviewButton): Card {
        val now = clock.now()
        val firstReview = card.firstReviewDate ?: now
        val updated = applyReview(card, button, now)

        cardRepetitionRepository.updateReview(
            cardId = card.cardId,
            cardState = updated.cardState,
            easeFactor = updated.easeFactor,
            interval = updated.interval,
            learningStep = updated.learningStep,
            nextReviewDate = updated.nextReviewDate!!,
            repetitionCount = updated.repetitionCount,
            lapseCount = updated.lapseCount,
            firstReviewDate = firstReview,
            lastReviewDate = now,
        )

        // Возвращаем итоговый объект с полными датами для переиспользования в очереди
        return updated.copy(firstReviewDate = firstReview, lastReviewDate = now)
    }

    // Основная точка входа: выбирает ветку обработки по текущему состоянию карточки
    private fun applyReview(card: Card, button: ReviewButton, now: Long): Card {
        return when (card.cardState) {
            CardState.NEW,
            CardState.LEARNING,
            CardState.LAPSE -> applyLearning(card, button, now)
            CardState.REVIEW -> applyReviewPhase(card, button, now)
        }
    }

    // --- Фаза LEARNING / LAPSE ---
    // Шаги обучения: шаг 0 = 1 мин, шаг 1 = 10 мин
    // Again → сброс на шаг 0
    // Hard  → остаться на текущем шаге
    // Good  → перейти на следующий шаг; если шагов больше нет → выпустить в REVIEW с interval=1
    // Easy  → сразу выпустить в REVIEW с interval=4
    private fun applyLearning(card: Card, button: ReviewButton, now: Long): Card {
        val learningSteps = listOf(
            TimeUnit.MINUTES.toMillis(1),
            TimeUnit.MINUTES.toMillis(10),
        )

        return when (button) {
            ReviewButton.AGAIN -> card.copy(
                cardState = if (card.cardState == CardState.LAPSE) CardState.LAPSE else CardState.LEARNING,
                easeFactor = (card.easeFactor - 0.54f).coerceAtLeast(MIN_EASE_FACTOR),
                learningStep = 0,
                nextReviewDate = now + learningSteps[0],
                repetitionCount = card.repetitionCount + 1,
            )

            ReviewButton.HARD -> card.copy(
                // NEW → LEARNING при первом показе (HARD не сбрасывает шаг, остаётся на step 0)
                cardState = CardState.LEARNING,
                easeFactor = (card.easeFactor - 0.14f).coerceAtLeast(MIN_EASE_FACTOR),
                nextReviewDate = now + learningSteps[card.learningStep],
                repetitionCount = card.repetitionCount + 1,
            )

            ReviewButton.GOOD -> {
                val nextStep = card.learningStep + 1
                if (nextStep < learningSteps.size) {
                    // NEW → LEARNING, переходим на следующий шаг обучения
                    card.copy(
                        cardState = CardState.LEARNING,
                        learningStep = nextStep,
                        nextReviewDate = now + learningSteps[nextStep],
                        repetitionCount = card.repetitionCount + 1,
                    )
                } else {
                    // Шаги исчерпаны — выпускаем карточку в REVIEW.
                    // После LAPSE применяем мультипликатор к предыдущему интервалу (как Anki),
                    // иначе используем стартовый интервал 1 день.
                    val newInterval = if (card.cardState == CardState.LAPSE) {
                        (card.interval * LAPSE_INTERVAL_MULTIPLIER).coerceAtLeast(1f)
                    } else {
                        1f
                    }
                    card.copy(
                        cardState = CardState.REVIEW,
                        learningStep = 0,
                        interval = newInterval,
                        nextReviewDate = startOfUtcDay(now) + ceil(newInterval).toLong() * TimeUnit.DAYS.toMillis(1),
                        repetitionCount = card.repetitionCount + 1,
                    )
                }
            }

            ReviewButton.EASY -> {
                val newInterval = if (card.cardState == CardState.LAPSE) {
                    (card.interval * LAPSE_INTERVAL_MULTIPLIER).coerceAtLeast(4f)
                } else {
                    4f
                }
                card.copy(
                    cardState = CardState.REVIEW,
                    easeFactor = (card.easeFactor + 0.10f).coerceAtMost(MAX_EASE_FACTOR),
                    learningStep = 0,
                    interval = newInterval,
                    nextReviewDate = startOfUtcDay(now) + ceil(newInterval).toLong() * TimeUnit.DAYS.toMillis(1),
                    repetitionCount = card.repetitionCount + 1,
                )
            }
        }
    }

    // --- Фаза REVIEW ---
    // Again → сброс в LAPSE (возврат в LEARNING шаг 0), EF снижается на 0.54
    // Hard  → interval × 1.2,      EF снижается на 0.14  (адаптация Anki)
    // Good  → interval × EF,       EF не меняется         (оригинальный SM-2)
    // Easy  → interval × EF × 1.3, EF растёт на 0.10     (адаптация Anki)
    private fun applyReviewPhase(card: Card, button: ReviewButton, now: Long): Card {
        return when (button) {
            ReviewButton.AGAIN -> card.copy(
                cardState = CardState.LAPSE,
                easeFactor = (card.easeFactor - 0.54f).coerceAtLeast(MIN_EASE_FACTOR),
                learningStep = 0,
                lapseCount = card.lapseCount + 1,
                nextReviewDate = now + TimeUnit.MINUTES.toMillis(1),
                repetitionCount = card.repetitionCount + 1,
            )

            ReviewButton.HARD -> {
                // Дробная часть сохраняется — исключает застревание на малых интервалах.
                // nextReviewDate снаплен к началу UTC-дня: пользователь видит все карточки сразу,
                // а не в разное время суток.
                val newInterval = (card.interval * 1.2f).coerceAtLeast(card.interval + 1f)
                card.copy(
                    easeFactor = (card.easeFactor - 0.14f).coerceAtLeast(MIN_EASE_FACTOR),
                    interval = newInterval,
                    nextReviewDate = startOfUtcDay(now) + ceil(newInterval).toLong() * TimeUnit.DAYS.toMillis(1),
                    repetitionCount = card.repetitionCount + 1,
                )
            }

            ReviewButton.GOOD -> {
                val newInterval = (card.interval * card.easeFactor).coerceAtLeast(card.interval + 1f)
                card.copy(
                    interval = newInterval,
                    nextReviewDate = startOfUtcDay(now) + ceil(newInterval).toLong() * TimeUnit.DAYS.toMillis(1),
                    repetitionCount = card.repetitionCount + 1,
                )
            }

            ReviewButton.EASY -> {
                val newInterval = (card.interval * card.easeFactor * 1.3f).coerceAtLeast(card.interval + 1f)
                card.copy(
                    easeFactor = (card.easeFactor + 0.10f).coerceAtMost(MAX_EASE_FACTOR),
                    interval = newInterval,
                    nextReviewDate = startOfUtcDay(now) + ceil(newInterval).toLong() * TimeUnit.DAYS.toMillis(1),
                    repetitionCount = card.repetitionCount + 1,
                )
            }
        }
    }

    // Вычисляет интервал до следующего показа карточки для предпросмотра на кнопках оценки.
    // Не сохраняет в БД — только возвращает количество миллисекунд.
    fun previewNextInterval(card: Card, button: ReviewButton): Long {
        val now = clock.now()
        val updated = applyReview(card, button, now)
        return (updated.nextReviewDate ?: now) - now
    }

    // Возвращает Unix timestamp начала текущего дня по UTC (midnight 00:00:00 UTC).
    // Используется для привязки nextReviewDate к дневной границе — пользователь
    // видит все карточки сразу при открытии приложения, а не в разное время суток.
    private fun startOfUtcDay(timestamp: Long): Long =
        (timestamp / TimeUnit.DAYS.toMillis(1)) * TimeUnit.DAYS.toMillis(1)

    companion object {
        // Минимально допустимое значение Ease Factor по спецификации SM-2
        private const val MIN_EASE_FACTOR = 1.3f
        private const val MAX_EASE_FACTOR = 3.5f
        // Доля предыдущего интервала, применяемая при выпуске из LAPSE (аналог Anki)
        private const val LAPSE_INTERVAL_MULTIPLIER = 0.7f
    }
}
