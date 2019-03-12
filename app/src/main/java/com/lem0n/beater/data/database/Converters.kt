package com.lem0n.beater.data.database

import androidx.room.TypeConverter

object Converters {
    @TypeConverter
    @JvmStatic
    fun fromRoles(value : Roles?) = value?.name

    @TypeConverter
    @JvmStatic
    fun toRoles(value : String?) = value?.let(Roles::valueOf)
}