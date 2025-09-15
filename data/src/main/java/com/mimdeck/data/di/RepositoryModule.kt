package com.mimdeck.data.di

import com.mimdeck.data.repository.CardDeckOperationsImpl
import com.mimdeck.data.repository.CardRepetitionRepositoryImpl
import com.mimdeck.data.repository.CardRepositoryImpl
import com.mimdeck.data.repository.DeckRepositoryImpl
import com.mimdeck.data.repository.NotificationRepositoryImpl
import com.mindeck.domain.repository.CardDeckOperations
import com.mindeck.domain.repository.CardRepetitionRepository
import com.mindeck.domain.repository.CardRepository
import com.mindeck.domain.repository.DeckRepository
import com.mindeck.domain.repository.NotificationRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindCardRepository(
        impl: CardRepositoryImpl
    ): CardRepository

    @Binds
    abstract fun bindCardDeckOperations(
        impl: CardDeckOperationsImpl
    ): CardDeckOperations

    @Binds
    abstract fun bindCardRepetitionRepository(
        impl: CardRepetitionRepositoryImpl
    ): CardRepetitionRepository

    @Binds
    abstract fun bindDeckRepository(
        impl: DeckRepositoryImpl
    ): DeckRepository

    @Binds
    abstract fun bindNotificationRepository(
        impl: NotificationRepositoryImpl
    ): NotificationRepository
}