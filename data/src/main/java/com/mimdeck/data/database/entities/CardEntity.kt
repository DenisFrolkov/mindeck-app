package com.mimdeck.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "card",
    indices = [
        Index(
            value = ["card_name", "card_question"],
            unique = true,
        ),
        Index(value = ["deck_id"]),
        Index(value = ["card_state"]),
        Index(value = ["next_review_date"]),
        Index(value = ["first_review_date"]),
    ],
    foreignKeys = [
        ForeignKey(
            entity = DeckEntity::class,
            parentColumns = ["deck_id"],
            childColumns = ["deck_id"],
            onDelete = CASCADE,
        ),
    ],
)
data class CardEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "card_id")
    val cardId: Int = 0,
    @ColumnInfo(name = "card_name") val cardName: String,
    @ColumnInfo(name = "card_question") val cardQuestion: String,
    @ColumnInfo(name = "card_answer") val cardAnswer: String,
    @ColumnInfo(name = "card_type") val cardType: Int,
    @ColumnInfo(name = "card_tag") val cardTag: String,
    @ColumnInfo(name = "deck_id") val deckId: Int,

    // --- Поля системы интервального повторения (SM-2) ---

    // Состояние карточки: NEW / LEARNING / REVIEW / LAPSE (хранится как строка — имя enum)
    @ColumnInfo(name = "card_state", defaultValue = "NEW") val cardState: String = "NEW",

    // Коэффициент лёгкости, начало: 2.5, минимум: 1.3
    @ColumnInfo(name = "ease_factor", defaultValue = "2.5") val easeFactor: Float = 2.5f,

    // Текущий интервал в днях (Float — сохраняет дробную часть для корректного роста при умножении)
    @ColumnInfo(name = "interval", defaultValue = "0") val interval: Float = 0f,

    // Текущий шаг в фазе LEARNING/LAPSE: 0 = 1 мин, 1 = 10 мин
    @ColumnInfo(name = "learning_step", defaultValue = "0") val learningStep: Int = 0,

    // Unix timestamp следующего показа
    @ColumnInfo(name = "next_review_date") val nextReviewDate: Long? = null,

    // Общее количество повторений
    @ColumnInfo(name = "repetition_count", defaultValue = "0") val repetitionCount: Int = 0,

    // Сколько раз карточка падала из REVIEW обратно в LEARNING
    @ColumnInfo(name = "lapse_count", defaultValue = "0") val lapseCount: Int = 0,

    // Дата первого показа (для подсчёта дневного лимита)
    @ColumnInfo(name = "first_review_date") val firstReviewDate: Long? = null,

    // Дата последнего показа (для статистики)
    @ColumnInfo(name = "last_review_date") val lastReviewDate: Long? = null,
)
