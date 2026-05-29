package com.mindeck.data.database

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.mindeck.data.dao.CardDao
import com.mindeck.data.dao.DeckDao
import com.mindeck.data.entities.CardEntity
import com.mindeck.data.entities.DeckEntity
import kotlinx.coroutines.Dispatchers

@Database(
    entities = [DeckEntity::class, CardEntity::class],
    version = 5,
    exportSchema = true,
)
@ConstructedBy(AppDatabaseConstructor::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun deckDao(): DeckDao

    abstract fun cardDao(): CardDao

    companion object {
        const val DATABASE_NAME = "mindeck_app_database"
    }
}

@Suppress("KotlinNoActualForExpect")
expect object AppDatabaseConstructor : RoomDatabaseConstructor<AppDatabase> {
    override fun initialize(): AppDatabase
}

fun getRoomDatabase(builder: RoomDatabase.Builder<AppDatabase>): AppDatabase =
    builder
        .setDriver(BundledSQLiteDriver())
        .setQueryCoroutineContext(Dispatchers.Default)
        .build()
