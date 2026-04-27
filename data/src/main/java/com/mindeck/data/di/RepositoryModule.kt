package com.mindeck.data.di

import com.mindeck.data.repository.CardRepetitionRepositoryImpl
import com.mindeck.data.repository.CardRepositoryImpl
import com.mindeck.data.repository.DeckRepositoryImpl
import com.mindeck.domain.repository.CardRepetitionRepository
import com.mindeck.domain.repository.CardRepository
import com.mindeck.domain.repository.DeckRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindCardRepository(
        impl: CardRepositoryImpl,
    ): CardRepository

    @Binds
    abstract fun bindCardRepetitionRepository(
        impl: CardRepetitionRepositoryImpl,
    ): CardRepetitionRepository

    @Binds
    abstract fun bindDeckRepository(
        impl: DeckRepositoryImpl,
    ): DeckRepository
}
