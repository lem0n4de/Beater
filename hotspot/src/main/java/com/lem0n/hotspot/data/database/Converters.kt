package com.lem0n.hotspot.data.database

import androidx.room.TypeConverter
import org.threeten.bp.Instant

/**
 * Created by lem0n on 14/04/19.
 */
object Converters {
    @TypeConverter
    @JvmStatic
    fun fromInstant(value : Instant?) = value?.toString()

    @TypeConverter
    @JvmStatic
    fun toInstant(value : String?) = value?.let { Instant.parse(value) }
}
