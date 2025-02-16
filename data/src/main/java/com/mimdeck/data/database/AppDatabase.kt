package com.mimdeck.data.database

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import com.mimdeck.data.database.dao.CardDao
import com.mimdeck.data.database.dao.DeckDao
import com.mimdeck.data.database.dao.FolderDao
import com.mimdeck.data.database.entities.CardEntity
import com.mimdeck.data.database.entities.DeckEntity
import com.mimdeck.data.database.entities.FolderEntity

@Database(
    entities = [FolderEntity::class, DeckEntity::class, CardEntity::class],
    version = 2,
    exportSchema = true,
    autoMigrations = [AutoMigration(from = 1, to = 2)]
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun folderDao(): FolderDao
    abstract fun deckDao(): DeckDao
    abstract fun cardDao(): CardDao
}