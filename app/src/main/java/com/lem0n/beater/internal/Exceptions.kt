package com.lem0n.beater.internal

class InsertUserException(private val msg : String? = null) : Exception() {
    override val message: String?
        get() = msg
}

class UpdateUserException(private val msg : String? = null) : Exception() {
    override val message : String?
        get() = msg
}

class DeleteUserException(private val msg : String? = null) : Exception() {
    override val message : String?
        get() = msg
}