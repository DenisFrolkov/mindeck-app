package com.mimdeck.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mimdeck.data.database.dao.CardDao
import com.mimdeck.data.database.dao.DeckDao
import com.mimdeck.data.database.entities.CardEntity
import com.mimdeck.data.database.entities.DeckEntity

@Database(
    entities = [DeckEntity::class, CardEntity::class],
    version = 1,
    exportSchema = true,
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun deckDao(): DeckDao
    abstract fun cardDao(): CardDao
}
