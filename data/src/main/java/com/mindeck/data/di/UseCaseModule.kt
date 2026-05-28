package com.mindeck.data.di

import com.mindeck.domain.repository.CardRepetitionRepository
import com.mindeck.domain.repository.CardRepository
import com.mindeck.domain.service.ClockRepository
import com.mindeck.domain.repository.DeckRepository
import com.mindeck.domain.usecases.card.command.CreateCardUseCase
import com.mindeck.domain.usecases.card.command.DeleteCardUseCase
import com.mindeck.domain.usecases.card.command.UpdateCardReviewUseCase
import com.mindeck.domain.usecases.card.command.UpdateCardUseCase
import com.mindeck.domain.usecases.card.query.GetAllCardsUseCase
import com.mindeck.domain.usecases.card.query.GetCardByIdUseCase
import com.mindeck.domain.usecases.card.query.GetCardWithDeckByIdUseCase
import com.mindeck.domain.usecases.card.query.GetCardsRepetitionUseCase
import com.mindeck.domain.usecases.deck.command.CreateDeckUseCase
import com.mindeck.domain.usecases.deck.command.DeleteDeckUseCase
import com.mindeck.domain.usecases.deck.command.RenameDeckUseCase
import com.mindeck.domain.usecases.deck.query.GetAllDecksUseCase
import com.mindeck.domain.usecases.deck.query.GetDeckByIdUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    fun provideCreateCardUseCase(repo: CardRepository) = CreateCardUseCase(repo)

    @Provides
    fun provideDeleteCardUseCase(repo: CardRepository) = DeleteCardUseCase(repo)

    @Provides
    fun provideUpdateCardUseCase(repo: CardRepository) = UpdateCardUseCase(repo)

    @Provides
    fun provideUpdateCardReviewUseCase(
        repo: CardRepetitionRepository,
        clock: ClockRepository,
    ) = UpdateCardReviewUseCase(repo, clock)

    @Provides
    fun provideGetAllCardsUseCase(repo: CardRepository) = GetAllCardsUseCase(repo)

    @Provides
    fun provideGetCardByIdUseCase(repo: CardRepository) = GetCardByIdUseCase(repo)

    @Provides
    fun provideGetCardWithDeckByIdUseCase(repo: CardRepository) = GetCardWithDeckByIdUseCase(repo)

    @Provides
    fun provideGetCardsRepetitionUseCase(
        repo: CardRepetitionRepository,
        clock: ClockRepository,
    ) = GetCardsRepetitionUseCase(repo, clock)

    @Provides
    fun provideCreateDeckUseCase(repo: DeckRepository) = CreateDeckUseCase(repo)

    @Provides
    fun provideDeleteDeckUseCase(repo: DeckRepository) = DeleteDeckUseCase(repo)

    @Provides
    fun provideRenameDeckUseCase(repo: DeckRepository) = RenameDeckUseCase(repo)

    @Provides
    fun provideGetAllDecksUseCase(repo: DeckRepository) = GetAllDecksUseCase(repo)

    @Provides
    fun provideGetDeckByIdUseCase(repo: DeckRepository) = GetDeckByIdUseCase(repo)
}
