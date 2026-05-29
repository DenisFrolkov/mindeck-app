package com.mindeck.domain.models

data class Card(
    val cardId: Int = 0,
    val cardName: String,
    val cardQuestion: String,
    val cardAnswer: String,
    val cardType: CardType,
    val cardTag: String,
    val deckId: Int,
    val cardState: CardState = CardState.NEW,
    val easeFactor: Float = 2.5f,
    val interval: Float = 0f,
    val learningStep: Int = 0,
    val nextReviewDate: Long? = null,
    val repetitionCount: Int = 0,
    val lapseCount: Int = 0,
    val firstReviewDate: Long? = null,
    val lastReviewDate: Long? = null,
) {
    init {
        require(cardName.isNotBlank()) { "Card name must not be blank" }
        require(cardQuestion.isNotBlank()) { "Card question must not be blank" }
        require(cardAnswer.isNotBlank()) { "Card answer must not be blank" }
    }
}
