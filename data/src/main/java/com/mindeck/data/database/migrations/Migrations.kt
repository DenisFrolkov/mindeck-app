package com.mindeck.data.database.migrations

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        // Recreate deck table: deck_name TEXT → TEXT NOT NULL, remove index_deck_deck_id
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

        // Recreate card table: card_name/card_question NOT NULL, card_type TEXT → INTEGER
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

// Migration v2 → v3: add SM-2 fields, remove last_review_type.
// Recreate card table because SQLite does not support dropping columns directly.
// Existing cards are assigned state NEW — review history is not preserved.
val MIGRATION_2_3 = object : Migration(2, 3) {
    override fun migrate(db: SupportSQLiteDatabase) {
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
                `card_state` TEXT NOT NULL DEFAULT 'NEW',
                `ease_factor` REAL NOT NULL DEFAULT 2.5,
                `interval` INTEGER NOT NULL DEFAULT 0,
                `learning_step` INTEGER NOT NULL DEFAULT 0,
                `next_review_date` INTEGER,
                `repetition_count` INTEGER NOT NULL DEFAULT 0,
                `lapse_count` INTEGER NOT NULL DEFAULT 0,
                `first_review_date` INTEGER,
                `last_review_date` INTEGER,
                FOREIGN KEY(`deck_id`) REFERENCES `deck`(`deck_id`) ON UPDATE NO ACTION ON DELETE CASCADE
            )
            """.trimIndent(),
        )
        db.execSQL(
            """
            INSERT INTO `card_new` (
                card_id, card_name, card_question, card_answer,
                card_type, card_tag, deck_id,
                card_state, ease_factor, interval, learning_step,
                next_review_date, repetition_count, lapse_count,
                first_review_date, last_review_date
            ) SELECT
                card_id, card_name, card_question, card_answer,
                card_type, card_tag, deck_id,
                'NEW', 2.5, 0, 0,
                NULL, 0, 0,
                NULL, NULL
            FROM `card`
            """.trimIndent(),
        )
        db.execSQL("DROP TABLE `card`")
        db.execSQL("ALTER TABLE `card_new` RENAME TO `card`")
        db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_card_card_name_card_question` ON `card` (`card_name`, `card_question`)")
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_card_deck_id` ON `card` (`deck_id`)")
    }
}

// Migration v3 → v4: interval INTEGER → REAL (Float) to preserve fractional part.
// Fractional interval prevents the algorithm from stalling on small values when multiplied.
val MIGRATION_3_4 = object : Migration(3, 4) {
    override fun migrate(db: SupportSQLiteDatabase) {
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
                `card_state` TEXT NOT NULL DEFAULT 'NEW',
                `ease_factor` REAL NOT NULL DEFAULT 2.5,
                `interval` REAL NOT NULL DEFAULT 0,
                `learning_step` INTEGER NOT NULL DEFAULT 0,
                `next_review_date` INTEGER,
                `repetition_count` INTEGER NOT NULL DEFAULT 0,
                `lapse_count` INTEGER NOT NULL DEFAULT 0,
                `first_review_date` INTEGER,
                `last_review_date` INTEGER,
                FOREIGN KEY(`deck_id`) REFERENCES `deck`(`deck_id`) ON UPDATE NO ACTION ON DELETE CASCADE
            )
            """.trimIndent(),
        )
        db.execSQL(
            """
            INSERT INTO `card_new` SELECT
                card_id, card_name, card_question, card_answer,
                card_type, card_tag, deck_id,
                card_state, ease_factor, CAST(interval AS REAL), learning_step,
                next_review_date, repetition_count, lapse_count,
                first_review_date, last_review_date
            FROM `card`
            """.trimIndent(),
        )
        db.execSQL("DROP TABLE `card`")
        db.execSQL("ALTER TABLE `card_new` RENAME TO `card`")
        db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_card_card_name_card_question` ON `card` (`card_name`, `card_question`)")
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_card_deck_id` ON `card` (`deck_id`)")
    }
}

// Migration v4 → v5: add indexes to speed up getCardsRepetition.
val MIGRATION_4_5 = object : Migration(4, 5) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_card_card_state` ON `card` (`card_state`)")
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_card_next_review_date` ON `card` (`next_review_date`)")
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_card_first_review_date` ON `card` (`first_review_date`)")
    }
}

val ALL_MIGRATIONS = arrayOf<Migration>(MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4, MIGRATION_4_5)
