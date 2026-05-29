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
    }
