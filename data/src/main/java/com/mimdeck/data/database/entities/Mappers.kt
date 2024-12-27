package com.mimdeck.data.database.entities

import com.mindeck.domain.models.Card
import com.mindeck.domain.models.Deck
import com.mindeck.domain.models.Folder

object Mappers {
    fun Folder.toData(): FolderEntity {
        return FolderEntity(
            this.folderId,
            this.folderName
        )
    }

    fun FolderEntity.toDomain(): Folder {
        return Folder(
            this.folderId,
            this.folderName ?: "Folder ${this.folderId}"
        )
    }

    fun Deck.toData(): DeckEntity {
        return DeckEntity(
            this.deckId,
            this.deckName,
            this.folderId
        )
    }

    fun DeckEntity.toDomain(): Deck {
        return Deck(
            this.deckId,
            this.deckName ?: "Deck ${this.deckId}",
            this.folderId
        )
    }

    fun Card.toData(): CardEntity {
        return CardEntity(
            this.cardId,
            this.cardName,
            this.cardQuestion,
            this.cardAnswer,
            this.cardPriority,
            this.cardType,
            this.cardTag,
            this.deckId
        )
    }

    fun CardEntity.toDomain(): Card {
        return Card(
            this.cardId,
            this.cardName ?: "Card ${this.cardId}",
            this.cardQuestion ?: "Question №${this.cardId}",
            this.cardAnswer,
            this.cardPriority,
            this.cardType,
            this.cardTag,
            this.deckId
        )
    }
}