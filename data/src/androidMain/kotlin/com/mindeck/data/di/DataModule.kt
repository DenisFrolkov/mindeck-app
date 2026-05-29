package com.mindeck.data.di

import com.mindeck.data.clock.ClockRepositoryImpl
import com.mindeck.data.database.AppDatabase
import com.mindeck.data.database.getDatabaseBuilder
import com.mindeck.data.database.getRoomDatabase
import com.mindeck.data.migrations.ALL_MIGRATIONS
import com.mindeck.data.repository.CardRepetitionRepositoryImpl
import com.mindeck.data.repository.CardRepositoryImpl
import com.mindeck.data.repository.DeckRepositoryImpl
import com.mindeck.domain.repository.CardRepetitionRepository
import com.mindeck.domain.repository.CardRepository
import com.mindeck.domain.repository.DeckRepository
import com.mindeck.domain.service.ClockRepository
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
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val dataModule =
    module {

        single<AppDatabase> {
            getRoomDatabase(
                getDatabaseBuilder(androidContext()).addMigrations(*ALL_MIGRATIONS),
            )
        }

        single { get<AppDatabase>().deckDao() }
        single { get<AppDatabase>().cardDao() }

        single<ClockRepository> { ClockRepositoryImpl() }

        single<CardRepository> { CardRepositoryImpl(get()) }
        single<CardRepetitionRepository> { CardRepetitionRepositoryImpl(get()) }
        single<DeckRepository> { DeckRepositoryImpl(get()) }

        factory { CreateCardUseCase(get()) }
        factory { DeleteCardUseCase(get()) }
        factory { UpdateCardUseCase(get()) }
        factory { UpdateCardReviewUseCase(get(), get()) }
        factory { GetAllCardsUseCase(get()) }
        factory { GetCardByIdUseCase(get()) }
        factory { GetCardWithDeckByIdUseCase(get()) }
        factory { GetCardsRepetitionUseCase(get(), get()) }
        factory { CreateDeckUseCase(get()) }
        factory { DeleteDeckUseCase(get()) }
        factory { RenameDeckUseCase(get()) }
        factory { GetAllDecksUseCase(get()) }
        factory { GetDeckByIdUseCase(get()) }
    }
