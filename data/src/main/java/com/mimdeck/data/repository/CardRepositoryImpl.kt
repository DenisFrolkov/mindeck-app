package com.mimdeck.data.repository

import com.mimdeck.data.dataSource.CardDataSource
import com.mimdeck.data.database.entities.Mappers.toData
import com.mimdeck.data.database.entities.Mappers.toDomain
import com.mimdeck.data.exception.DatabaseException
import com.mindeck.domain.exception.DomainException
import com.mindeck.domain.models.Card
import com.mindeck.domain.models.Deck
import com.mindeck.domain.models.Folder
import com.mindeck.domain.repository.CardRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CardRepositoryImpl @Inject constructor(private val cardDataSource: CardDataSource) : CardRepository {
    override suspend fun insertCard(card: Card) {
        try {
            cardDataSource.insertCard(cardEntity = card.toData())
        } catch (e: DatabaseException) {
            throw DomainException("Failed to create card: ${e.localizedMessage}", e)
        }
    }

    override suspend fun updateCard(card: Card) {
        try {
            cardDataSource.updateCard(cardEntity = card.toData())
        } catch (e: DatabaseException) {
            throw DomainException("Failed to update deck: ${e.localizedMessage}", e)
        }
    }

    override suspend fun deleteCard(card: Card) {
        try {
            cardDataSource.deleteCard(cardEntity = card.toData())
        } catch (e: DatabaseException) {
            throw DomainException("Failed to delete card: ${e.localizedMessage}", e)
        }
    }

    override fun getAllCardsByDeckId(deckId: Int): Flow<List<Card>> {
        return try {
            cardDataSource.getAllCardsByDeckId(deckId = deckId)
                .map { cardsEntityList -> cardsEntityList.map { it.toDomain() } }
        } catch (e: DatabaseException) {
            flow { emit(emptyList<Deck>()) }
            throw DomainException("Failed get all cards by deck id", e)
        }
    }

    override suspend fun getCardById(cardId: Int): Card {
        return try {
            cardDataSource.getCardById(cardId = cardId).toDomain()
        } catch (e: DatabaseException) {
            throw DomainException("Failed get card", e)
        }
    }

    override suspend fun getFolderByCardId(cardId: Int): Folder? {
        return try {
            cardDataSource.getFolderByCardId(cardId = cardId)?.toDomain()
        } catch (e: DatabaseException) {
            throw DomainException("Failed get folder by cardId", e)
        }
    }

    override suspend fun deleteCardsFromDeck(cardsIds: List<Int>, deckId: Int) {
        try {
            cardDataSource.deleteCardsFromDeck(cardsIds = cardsIds, deckId = deckId)
        } catch (e: DatabaseException) {
            throw DomainException("Failed to delete card from deck: ${e.localizedMessage}", e)
        }
    }

    override suspend fun addCardsToDeck(cardIds: List<Int>, deckId: Int) {
        try {
            cardDataSource.addCardsToDeck(cardIds = cardIds, deckId = deckId)
        } catch (e: DatabaseException) {
            throw DomainException("Failed to add card in deck: ${e.localizedMessage}", e)
        }
    }

    override suspend fun moveCardsBetweenDeck(
        cardIds: List<Int>,
        sourceDeckId: Int,
        targetDeckId: Int
    ) {
        try {
            cardDataSource.moveCardsBetweenDeck(
                cardIds = cardIds,
                sourceDeckId = sourceDeckId,
                targetDeckId = targetDeckId
            )
        } catch (e: DatabaseException) {
            throw DomainException("Failed to move cards between deck: ${e.localizedMessage}", e)
        }
    }
}