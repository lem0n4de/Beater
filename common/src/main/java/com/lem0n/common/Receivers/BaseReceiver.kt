package com.lem0n.common.Receivers

import com.lem0n.common.internal.LockIsOccupied
import com.lem0n.common.internal.NotAuthorizedToChangeLock
import timber.log.Timber

open class BaseReceiver : IReceiver {
    companion object {
        private val receivers : HashMap<Int, (ByteArray) -> Boolean> = HashMap()
        fun addReceiverFun(signal : Int, block : (ByteArray) -> Boolean ) = receivers.put(signal, block)
        fun removeReceiverFun(signal: Int) = receivers.remove(signal)

        private var lock : Int? = null
        fun setReceiverLock(key : Int) {
            if (lock == null) {
                lock = key
            } else {
                throw LockIsOccupied()
            }
        }
        fun removeReceiverLock(key : Int) {
            if (lock != key) {
                throw NotAuthorizedToChangeLock()
            } else {
                lock = null
            }
        }
    }

    override fun receive(byteArray: ByteArray, length : Int): Boolean {
        Timber.d("Received something.")
        if (lock != null) {
            Timber.d("Lock is in place, directly starting it.")
            return receivers.get(lock!!)!!.invoke(byteArray)
        }
        val actualSignal = String(byteArray, 0, length).toInt()
        if (receivers.containsKey(actualSignal)) {
            Timber.d("No lock but a receiver found.")
            return receivers.get(actualSignal)!!.invoke(byteArray)
        }
        Timber.e("No receiver found. for the signal.")
        return false
    }
}