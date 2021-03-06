package com.lem0n.common.communicators

import com.lem0n.common.internal.UnauthorizedAction
import timber.log.Timber
import java.util.*

typealias ReceiverFunction = (byteArray : ByteArray, length : Int, lockCount : Int) -> Boolean
typealias SenderFunction = (byteArray : ByteArray) -> Unit

class NoSenderFunctionDefinedException() : Exception()

object ServerCommunicator : ICommunicator {
    private val receiverFunctionStore = NonOverridingHashMap<UUID, ReceiverFunction>()
    private var lock : UUID? = null
    private var lockCount : Int = 0
    lateinit var senderFunction : SenderFunction

    /**
     * A general function for sending signals to
     * other device.
     * @param byteArray A byteArray version of message.
     * @throws NoSenderFunctionDefinedException
     */
    @Throws(NoSenderFunctionDefinedException::class)
    override fun send(byteArray: ByteArray) {
        if (::senderFunction.isInitialized) {
            senderFunction.invoke(byteArray)
        } else {
            throw NoSenderFunctionDefinedException()
        }
    }


    /**
     * Helper function to send UUID
     */
    fun send(uuid: UUID) {
        return send(uuid.toString().toByteArray())
    }


    /**
     * A general function to fire receiver functions
     * according to incoming signals.
     * If no value(function) found for the signal,
     * it silently fails.
     */
    override fun receive(byteArray: ByteArray, length: Int) {
        Timber.i("Received signal. $byteArray")
        Timber.i(String(byteArray, 0, length))
        if (lock != null && lockCount > 0) {
            val tmpLock = lockCount
            lockCount -= 1
            Timber.i("Lock is present, sending data to according function. lockCount = $tmpLock")
            receiverFunctionStore[lock!!]!!(byteArray, length, tmpLock)
            return
        }
        try {
            val incomingSignal = UUID.fromString(String(byteArray, 0, length))
            if (receiverFunctionStore.containsKey(incomingSignal)) {
                receiverFunctionStore[incomingSignal]!!(byteArray, length, lockCount)
                Timber.i("A function found for the incoming signal.")
            } else {
                Timber.i("No function found for incoming signal.")
            }
        } catch (e : Exception) {
            Timber.wtf(e)
        }

    }

    /**
     * @param signal Signal of the function that will receive later data.
     * @param lockCount The number of times that the function will receive incoming data.
     * @throws UnauthorizedAction
     */
    @Throws(UnauthorizedAction::class)
    fun lock(signal : UUID, lockCount : Int = 0) {
        if (lock != null) {
            throw UnauthorizedAction("Lock is occupied.")
        }
        lock = signal
        this.lockCount = lockCount
    }

    /**
     * @param signal Signal that was used to lock earlier.
     * @throws UnauthorizedAction
     */
    @Throws(UnauthorizedAction::class)
    fun unlock(signal : UUID) {
        if (lock != signal) {
            throw UnauthorizedAction("Lock and the signal that is sent now is not the same.")
        }
        lock = null
        lockCount = 0
    }

    fun registerReceiver(signal: UUID, block : ReceiverFunction) {
        Timber.d("Registered a function.")
        receiverFunctionStore[signal] = block
    }
}
