package com.mimdeck.data.di

import android.content.Context
import androidx.room.Room
import com.mimdeck.data.database.AppDatabase
import com.mimdeck.data.database.dao.CardDao
import com.mimdeck.data.database.dao.DeckDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "mindeck_app_database",
        )
            .build()
    }

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
}
