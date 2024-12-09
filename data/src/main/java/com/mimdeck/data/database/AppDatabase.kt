package com.mimdeck.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mimdeck.data.database.dao.CardDao
import com.mimdeck.data.database.dao.DeckDao
import com.mimdeck.data.database.dao.FolderDao
import com.mimdeck.data.database.entities.CardEntity
import com.mimdeck.data.database.entities.DeckEntity
import com.mimdeck.data.database.entities.FolderEntity

@Database(entities = [FolderEntity::class, DeckEntity::class, CardEntity::class], version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun folderDao(): FolderDao
    abstract fun deckDao(): DeckDao
    abstract fun cardDao(): CardDao
}