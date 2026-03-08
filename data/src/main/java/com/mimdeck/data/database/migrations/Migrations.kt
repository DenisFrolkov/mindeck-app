package com.mimdeck.data.database.migrations

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        // Пересоздаём таблицу deck: deck_name TEXT → TEXT NOT NULL, убираем index_deck_deck_id
        db.execSQL(
            """
            CREATE TABLE IF NOT EXISTS `deck_new` (
                `deck_id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                `deck_name` TEXT NOT NULL
            )
            """.trimIndent(),
        )
        db.execSQL(
            """
            INSERT INTO `deck_new` SELECT
                deck_id,
                COALESCE(deck_name, 'Deck ' || deck_id)
            FROM `deck`
            """.trimIndent(),
        )
        db.execSQL("DROP TABLE `deck`")
        db.execSQL("ALTER TABLE `deck_new` RENAME TO `deck`")
        db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_deck_deck_name` ON `deck` (`deck_name`)")

        // Пересоздаём таблицу card: card_name/card_question NOT NULL, card_type TEXT → INTEGER
        db.execSQL(
            """
            CREATE TABLE IF NOT EXISTS `card_new` (
                `card_id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                `card_name` TEXT NOT NULL,
                `card_question` TEXT NOT NULL,
                `card_answer` TEXT NOT NULL,
                `card_type` INTEGER NOT NULL,
                `card_tag` TEXT NOT NULL,
                `deck_id` INTEGER NOT NULL,
                `first_review_date` INTEGER,
                `last_review_date` INTEGER,
                `next_review_date` INTEGER,
                `repetition_count` INTEGER NOT NULL DEFAULT 0,
                `last_review_type` TEXT,
                FOREIGN KEY(`deck_id`) REFERENCES `deck`(`deck_id`) ON UPDATE NO ACTION ON DELETE CASCADE
            )
            """.trimIndent(),
        )
        db.execSQL(
            """
            INSERT INTO `card_new` SELECT
                card_id,
                COALESCE(card_name, 'Card ' || card_id),
                COALESCE(card_question, 'Question ' || card_id),
                card_answer,
                CAST(card_type AS INTEGER),
                card_tag,
                deck_id,
                first_review_date,
                last_review_date,
                next_review_date,
                repetition_count,
                last_review_type
            FROM `card`
            """.trimIndent(),
        )
        db.execSQL("DROP TABLE `card`")
        db.execSQL("ALTER TABLE `card_new` RENAME TO `card`")
        db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_card_card_name_card_question` ON `card` (`card_name`, `card_question`)")
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_card_deck_id` ON `card` (`deck_id`)")
    }
}

val ALL_MIGRATIONS = arrayOf<Migration>(MIGRATION_1_2)
