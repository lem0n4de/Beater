package com.lem0n.beater.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.lem0n.beater.data.database.dao.UserDao
import com.lem0n.beater.data.database.entity.User

@Database(
    entities = [User::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao() : UserDao

    companion object {
        @Volatile
        private var INSTANCE : AppDatabase? = null

        fun getDatabase(context : Context) : AppDatabase {
            if (INSTANCE == null) {
                val instance = Room.databaseBuilder(
                    context, AppDatabase::class.java, "beater_database"
                ).build()
                INSTANCE = instance
            }
            return INSTANCE!!
        }
    }
}