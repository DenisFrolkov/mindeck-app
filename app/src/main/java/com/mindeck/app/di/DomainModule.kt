package com.mindeck.app.di

import com.mindeck.domain.repository.CardRepository
import com.mindeck.domain.repository.DeckRepository
import com.mindeck.domain.repository.NotificationRepository
import com.mindeck.domain.usecases.cardUseCase.AddCardsToDeckUseCase
import com.mindeck.domain.usecases.cardUseCase.CreateCardUseCase
import com.mindeck.domain.usecases.cardUseCase.DeleteCardUseCase
import com.mindeck.domain.usecases.cardUseCase.DeleteCardsFromDeckUseCase
import com.mindeck.domain.usecases.cardUseCase.GetAllCardsByDeckIdUseCase
import com.mindeck.domain.usecases.cardUseCase.GetCardByIdUseCase
import com.mindeck.domain.usecases.cardUseCase.GetCardsRepetitionUseCase
import com.mindeck.domain.usecases.cardUseCase.MoveCardsBetweenDeckUseCase
import com.mindeck.domain.usecases.cardUseCase.UpdateCardReviewUseCase
import com.mindeck.domain.usecases.cardUseCase.UpdateCardUseCase
import com.mindeck.domain.usecases.deckUseCases.CreateDeckUseCase
import com.mindeck.domain.usecases.deckUseCases.DeleteDeckUseCase
import com.mindeck.domain.usecases.deckUseCases.GetAllDecksUseCase
import com.mindeck.domain.usecases.deckUseCases.GetDeckByIdUseCase
import com.mindeck.domain.usecases.deckUseCases.RenameDeckUseCase
import com.mindeck.domain.usecases.notificationUseCase.StartNotificationUseCase
import com.mindeck.domain.usecases.notificationUseCase.StopNotificationUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class DomainModule() {

    //DeckUseCase
    @Provides
    fun providesGetAllDecksUseCase(repository: DeckRepository): GetAllDecksUseCase {
        return GetAllDecksUseCase(repository)
    }

    @Provides
    fun providesCreateDeckUseCase(repository: DeckRepository): CreateDeckUseCase {
        return CreateDeckUseCase(repository)
    }

    @Provides
    fun providesRenameDeckUseCase(repository: DeckRepository): RenameDeckUseCase {
        return RenameDeckUseCase(repository)
    }

    @Provides
    fun providesDeleteDeckUseCase(repository: DeckRepository): DeleteDeckUseCase {
        return DeleteDeckUseCase(repository)
    }

    @Provides
    fun providesGetDeckByIdUseCase(repository: DeckRepository): GetDeckByIdUseCase {
        return GetDeckByIdUseCase(repository)
    }

    //CardUseCase
    @Provides
    fun providesCreateCardUseCase(repository: CardRepository): CreateCardUseCase {
        return CreateCardUseCase(repository)
    }

    @Provides
    fun providesUpdateCardUseCase(repository: CardRepository): UpdateCardUseCase {
        return UpdateCardUseCase(repository)
    }

    @Provides
    fun providesDeleteCardUseCase(repository: CardRepository): DeleteCardUseCase {
        return DeleteCardUseCase(repository)
    }

    @Provides
    fun providesGetAllCardsByDeckIdUseCase(repository: CardRepository): GetAllCardsByDeckIdUseCase {
        return GetAllCardsByDeckIdUseCase(repository)
    }

    @Provides
    fun providesGetCardByIdUseCase(repository: CardRepository): GetCardByIdUseCase {
        return GetCardByIdUseCase(repository)
    }

    @Provides
    fun providesAddCardsToDeckUseCase(repository: CardRepository): AddCardsToDeckUseCase {
        return AddCardsToDeckUseCase(repository)
    }

    @Provides
    fun providesDeleteCardsFromDeckUseCase(repository: CardRepository): DeleteCardsFromDeckUseCase {
        return DeleteCardsFromDeckUseCase(repository)
    }

    @Provides
    fun providesMoveCardsBetweenDeckUseCase(repository: CardRepository): MoveCardsBetweenDeckUseCase {
        return MoveCardsBetweenDeckUseCase(repository)
    }

    @Provides
    fun providesGetCardsRepetitionUseCase(repository: CardRepository): GetCardsRepetitionUseCase {
        return GetCardsRepetitionUseCase(repository)
    }

    @Provides
    fun providesUpdateCardReviewUseCase(repository: CardRepository): UpdateCardReviewUseCase {
        return UpdateCardReviewUseCase(repository)
    }

    //NotificationUseCase
    @Provides
    fun providesStartNotificationUseCase(repository: NotificationRepository): StartNotificationUseCase {
        return StartNotificationUseCase(repository)
    }

    @Provides
    fun providesStopNotificationUseCase(repository: NotificationRepository): StopNotificationUseCase {
        return StopNotificationUseCase(repository)
    }
}