package com.mindeck.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mindeck.data.database.dao.CardDao
import com.mindeck.data.database.dao.DeckDao
import com.mindeck.data.database.entities.CardEntity
import com.mindeck.data.database.entities.DeckEntity

@Database(
    entities = [DeckEntity::class, CardEntity::class],
    version = 5,
    exportSchema = true,
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun deckDao(): DeckDao
    abstract fun cardDao(): CardDao

    companion object {
        const val DATABASE_NAME = "mindeck_app_database"
    }
}
