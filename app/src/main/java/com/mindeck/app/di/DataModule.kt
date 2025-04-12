package com.mindeck.app.di

import android.content.Context
import androidx.room.Room
import com.mimdeck.data.dataSource.CardDataSource
import com.mimdeck.data.dataSource.DeckDataSource
import com.mimdeck.data.dataSource.dataSourceImpl.CardLocalDataSourceImpl
import com.mimdeck.data.dataSource.dataSourceImpl.DeckLocalDataSourceImpl
import com.mimdeck.data.database.AppDatabase
import com.mimdeck.data.database.dao.CardDao
import com.mimdeck.data.database.dao.DeckDao
import com.mimdeck.data.repository.CardRepositoryImpl
import com.mimdeck.data.repository.DeckRepositoryImpl
import com.mimdeck.data.repository.NotificationRepositoryImpl
import com.mindeck.domain.repository.CardRepository
import com.mindeck.domain.repository.DeckRepository
import com.mindeck.domain.repository.NotificationRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    //Database
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "mindeck_app_database"
        )
            .build()
    }

    //DAO
    @Provides
    @Singleton
    fun provideDeckDao(database: AppDatabase): DeckDao {
        return database.deckDao()
    }

    @Provides
    @Singleton
    fun provideCardDao(database: AppDatabase): CardDao {
        return database.cardDao()
    }

    // DataSources

    @Provides
    fun provideDeckDataSource(impl: DeckLocalDataSourceImpl): DeckDataSource {
        return impl
    }

    @Provides
    fun provideCardDataSource(impl: CardLocalDataSourceImpl): CardDataSource {
        return impl
    }

    // Repositories

    @Provides
    fun provideDeckRepository(impl: DeckRepositoryImpl): DeckRepository {
        return impl
    }

    @Provides
    fun provideCardRepository(impl: CardRepositoryImpl): CardRepository {
        return impl
    }

    @Provides
    fun provideNotificationRepository(impl: NotificationRepositoryImpl): NotificationRepository {
        return impl
    }
}