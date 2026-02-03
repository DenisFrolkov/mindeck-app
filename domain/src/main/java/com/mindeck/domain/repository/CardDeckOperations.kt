package com.mindeck.domain.repository

interface CardDeckOperations {
    suspend fun deleteCardsFromDeck(cardsIds: List<Int>, deckId: Int)
    suspend fun addCardsToDeck(cardIds: List<Int>, deckId: Int)
    suspend fun moveCardsBetweenDeck(
        cardIds: List<Int>,
        sourceDeckId: Int,
        targetDeckId: Int,
    ) {
        addCardsToDeck(cardIds, targetDeckId)
        deleteCardsFromDeck(cardIds, sourceDeckId)
    }
}
