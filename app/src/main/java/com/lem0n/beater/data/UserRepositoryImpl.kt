package com.lem0n.beater.data

import android.annotation.SuppressLint
import android.content.Context
import com.lem0n.beater.data.database.AppDatabase
import com.lem0n.beater.data.database.Roles
import com.lem0n.beater.data.database.entity.User
import com.lem0n.beater.internal.DeleteUserException
import com.lem0n.beater.internal.InsertUserException
import com.lem0n.beater.internal.UpdateUserException
import io.reactivex.Single

@SuppressLint("CheckResult")
class UserRepositoryImpl(context : Context) : UserRepository {
    private val database = AppDatabase.getDatabase(context)
    private val userDao = database.userDao()

    companion object {
        private const val DEFAULT_NAME = "DEFAULT_NAME"
        private const val DEFAULT_DEVICE_NAME = "DEFAULT_DEVICE_NAME"
    }

    override suspend fun getUser(): Single<User> {
        return userDao.getUser()
    }

    override suspend fun insertUser(role: Roles?) {
        if (role != null) {
            val user = User(DEFAULT_NAME, role, DEFAULT_DEVICE_NAME)
            userDao.upsert(user).doOnError {
                throw InsertUserException(it.message)
            }
        } else {
            InsertUserException("Null role.")
        }
    }

    override suspend fun insertUser(name: String?, role: Roles?) {
        if (name != null && role != null) {
            val user = User(name, role, DEFAULT_DEVICE_NAME)
            userDao.upsert(user).doOnError {
                throw InsertUserException(it.message)
            }
        } else {
            throw InsertUserException("Null name and/or role.")
        }
    }

    override suspend fun insertUser(name: String?, role: Roles?, deviceName: String?) {
        if (name != null && role != null && deviceName != null) {
            val user = User(name, role, deviceName)
            userDao.upsert(user).doOnError {
                throw InsertUserException(it.message)
            }
        } else {
            throw InsertUserException("Null name and/or role and/or deviceName.")
        }
    }

    override suspend fun updateRole(role: Roles?) {
        if (role != null) {
            try {
                val oldUser = userDao.getUser().blockingGet()
                val updatedUser = User(oldUser.name, role, oldUser.deviceName)
                userDao.upsert(updatedUser).doOnError {
                    throw UpdateUserException(it.message)
                }
            } catch (e : Exception) {
                throw UpdateUserException(e.message)
            }
        } else {
            throw UpdateUserException("Null role.")
        }
    }

    override suspend fun updateName(name: String?) {
        if (name != null) {
            try {
                val oldUser = userDao.getUser().blockingGet()
                val updatedUser = User(name, oldUser.role, oldUser.deviceName)
                userDao.upsert(updatedUser).doOnError {
                    throw UpdateUserException(it.message)
                }
            } catch (e : Exception) {
                throw UpdateUserException(e.message)
            }
        } else {
            throw UpdateUserException("Null name.")
        }
    }

    override suspend fun updateDeviceName(deviceName: String?) {
        if (deviceName != null) {
            try {
                val oldUser = userDao.getUser().blockingGet()
                val updatedUser = User(oldUser.name, oldUser.role, deviceName)
                userDao.upsert(updatedUser).doOnError {
                    throw UpdateUserException(it.message)
                }
            } catch (e : Exception) {
                throw UpdateUserException(e.message)
            }
        } else {
            throw UpdateUserException("Null role.")
        }
    }

    override suspend fun deleteUser() {
        val user = userDao.getUser().blockingGet()
        userDao.delete(user).doOnError {
            throw DeleteUserException()
        }
    }
}