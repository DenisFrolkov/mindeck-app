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
    @ColumnInfo(name = "card_state", defaultValue = "NEW") val cardState: String = "NEW",
    @ColumnInfo(name = "ease_factor", defaultValue = "2.5") val easeFactor: Float = 2.5f,
    @ColumnInfo(name = "interval", defaultValue = "0") val interval: Float = 0f,
    @ColumnInfo(name = "learning_step", defaultValue = "0") val learningStep: Int = 0,
    @ColumnInfo(name = "next_review_date") val nextReviewDate: Long? = null,
    @ColumnInfo(name = "repetition_count", defaultValue = "0") val repetitionCount: Int = 0,
    @ColumnInfo(name = "lapse_count", defaultValue = "0") val lapseCount: Int = 0,
    @ColumnInfo(name = "first_review_date") val firstReviewDate: Long? = null,
    @ColumnInfo(name = "last_review_date") val lastReviewDate: Long? = null,
)
