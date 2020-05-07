package com.alialbaali.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.alialbaali.model.Chat
import com.alialbaali.model.Message

private const val DATABASE_NAME = "ChatyChaty Database"
private const val DATABASE_VERSION = 1

@Database(entities = [Chat::class, Message::class], version = DATABASE_VERSION, exportSchema = false)
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
                .fallbackToDestructiveMigration()
                .build()
    }
}