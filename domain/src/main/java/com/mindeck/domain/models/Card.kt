package com.mindeck.domain.models

data class Card(
    val cardId: Int = 0,
    val cardName: String,
    val cardQuestion: String,
    val cardAnswer: String,
    val cardType: String,
    val cardTag: String,
    val deckId: Int,
    val firstReviewDate: Long?,
    val lastReviewDate: Long?,
    val nextReviewDate: Long?,
    val repetitionCount: Int = 0,
    val lastReviewType: ReviewType?
)
