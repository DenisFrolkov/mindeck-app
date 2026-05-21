package com.mindeck.data.di

import com.mindeck.data.repository.AudioRepositoryImpl
import com.mindeck.data.repository.CardRepetitionRepositoryImpl
import com.mindeck.data.repository.CardRepositoryImpl
import com.mindeck.data.repository.DeckRepositoryImpl
import com.mindeck.data.repository.ImageRepositoryImpl
import com.mindeck.domain.repository.AudioRepository
import com.mindeck.domain.repository.CardRepetitionRepository
import com.mindeck.domain.repository.CardRepository
import com.mindeck.domain.repository.DeckRepository
import com.mindeck.domain.repository.ImageRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {
    @Binds
    fun bindCardRepository(impl: CardRepositoryImpl): CardRepository

    @Binds
    fun bindCardRepetitionRepository(impl: CardRepetitionRepositoryImpl): CardRepetitionRepository

    @Binds
    fun bindDeckRepository(impl: DeckRepositoryImpl): DeckRepository

    @Binds
    fun bindImageRepository(impl: ImageRepositoryImpl): ImageRepository

    @Binds
    fun bindAudioRepository(impl: AudioRepositoryImpl): AudioRepository
}
