package com.chatychaty.local

import android.content.Context
import androidx.room.*
import com.chatychaty.domain.model.Chat
import com.chatychaty.domain.model.Message
import com.chatychaty.local.migrations.Migration1To2
import com.chatychaty.local.migrations.Migration2To3
import kotlinx.datetime.Instant
import kotlinx.datetime.toInstant

private const val DATABASE_NAME = "ChatyChaty Database"
private const val DATABASE_VERSION = 3

@Database(entities = [Chat::class, Message::class], version = DATABASE_VERSION, exportSchema = false)
@TypeConverters(InstantConvertor::class)
abstract class AppDatabase : RoomDatabase() {

    abstract val chatDao: ChatDao
    abstract val messageDao: MessageDao

    companion object {

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                DATABASE_NAME
            )
                .addMigrations(Migration1To2, Migration2To3)
                .build()
    }
}

private object InstantConvertor {
    @TypeConverter
    fun toString(value: Instant) = value.toString()

    @TypeConverter
    fun toInstant(value: String) = value.toInstant()
}