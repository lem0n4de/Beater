package com.lem0n.beater.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.lem0n.beater.data.database.Roles

@Entity(tableName = "users")
data class User (
    var name : String,
    var role : Roles,
    var deviceName : String
) {
    @PrimaryKey(autoGenerate = false)
    var id : Int = 1
}