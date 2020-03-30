package com.apps.chatychaty.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.apps.chatychaty.model.Chat
import com.apps.chatychaty.model.Message

@Database(
    entities = [Chat::class, Message::class],
    version = 1,
    exportSchema = false
)
internal abstract class AppDatabase : RoomDatabase() {

    internal abstract val chatDao: ChatDao
    internal abstract val messageDao: MessageDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null


        internal fun getInstance(context: Context): AppDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context,
                        AppDatabase::class.java,
                        "AppDatabase"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}