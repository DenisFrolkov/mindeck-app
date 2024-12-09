package com.mindeck.app.di

import android.content.Context
import androidx.room.Room
import com.mimdeck.data.dataSource.CardDataSource
import com.mimdeck.data.dataSource.DeckDataSource
import com.mimdeck.data.dataSource.FolderDataSource
import com.mimdeck.data.dataSource.dataSourceImpl.CardLocalDataSourceImpl
import com.mimdeck.data.dataSource.dataSourceImpl.DeckLocalDataSourceImpl
import com.mimdeck.data.dataSource.dataSourceImpl.FolderLocalDataSourceImpl
import com.mimdeck.data.database.AppDatabase
import com.mimdeck.data.database.dao.CardDao
import com.mimdeck.data.database.dao.DeckDao
import com.mimdeck.data.database.dao.FolderDao
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "mindeck_app_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideFolderDao(database: AppDatabase): FolderDao {
        return database.folderDao()
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

    @Binds
    abstract fun bindFolderDataSource(
        impl: FolderLocalDataSourceImpl
    ): FolderDataSource

    @Binds
    abstract fun bindDeckDataSource(
        impl: DeckLocalDataSourceImpl
    ): DeckDataSource

    @Binds
    abstract fun bindCardDataSource(
        impl: CardLocalDataSourceImpl
    ): CardDataSource
}