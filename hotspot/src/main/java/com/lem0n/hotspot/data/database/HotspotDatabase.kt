package com.lem0n.hotspot.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.lem0n.hotspot.data.database.dao.HotspotDao
import com.lem0n.hotspot.data.database.entity.HotspotEntry

/**
 * Created by lem0n on 14/04/19.
 */
@Database(
    entities = [HotspotEntry::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class HotspotDatabase : RoomDatabase() {
    abstract fun hotspotDao() : HotspotDao

    companion object {
        @Volatile
        private var INSTANCE : HotspotDatabase? = null

        fun getDatabase(context : Context) : HotspotDatabase {
            if (INSTANCE == null) {
                val instance = Room.databaseBuilder(
                    context, HotspotDatabase::class.java, "beater_database"
                ).build()
                INSTANCE = instance
            }
            return INSTANCE!!
        }
    }
}