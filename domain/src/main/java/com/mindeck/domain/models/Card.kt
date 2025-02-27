package com.mindeck.domain.models

data class Card(
    val cardId: Int = 0,
    val cardName: String,
    val cardQuestion: String,
    val cardAnswer: String,
    val cardType: String,
    val cardTag: String,
    val deckId: Int,
    val firstReviewDate: Long? = null,
    val lastReviewDate: Long? = null,
    val nextReviewDate: Long? = null,
    val repetitionCount: Int = 0,
    val lastReviewType: ReviewType? = null
)
