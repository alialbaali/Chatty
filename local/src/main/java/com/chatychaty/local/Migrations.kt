package com.chatychaty.local.migrations

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

object Migration1To2 : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL(
            """
            ALTER TABLE messages ADD COLUMN is_new INTEGER NOT NULL DEFAULT 0;
        """.trimIndent()
        )
    }
}

object Migration2To3 : Migration(2, 3) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL(
            """
            ALTER TABLE chats ADD COLUMN is_muted INTEGER NOT NULL DEFAULT 0;
        """.trimIndent()
        )
    }
}
