package com.lem0n.common.internal

class LockIsOccupied : Exception()
class NotAuthorizedToChangeLock : Exception()
class NoSenderFunctionFound(val msg : String? = null) : Exception(msg)

class UnauthorizedAction(val msg : String? = null) : Exception(msg)