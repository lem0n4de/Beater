package com.lem0n.beater.data

import com.lem0n.beater.data.database.Roles
import com.lem0n.beater.data.database.entity.User
import io.reactivex.Single

interface UserRepository {
    suspend fun getUser() : Single<User>
    suspend fun insertUser(role: Roles?)
    suspend fun insertUser(name : String?, role: Roles?)
    suspend fun insertUser(name : String?, role: Roles?, deviceName : String?)
    suspend fun updateRole(role : Roles?)
    suspend fun updateName(name : String?)
    suspend fun updateDeviceName(deviceName : String?)
    suspend fun deleteUser()
}