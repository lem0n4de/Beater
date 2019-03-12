package com.lem0n.beater.data.database.dao

import androidx.room.*
import com.lem0n.beater.data.database.entity.User
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsert(user : User) : Completable

    @Query("SELECT id, name, role, deviceName FROM users WHERE id == 1")
    fun getUser() : Single<User>

    @Delete
    fun delete(user : User)
}