package com.lem0n.common.senders

import com.lem0n.common.internal.NoSenderFunctionFound


typealias SenderFunction = (ByteArray) -> Boolean

open class BaseSender : ISender {
    private var senderFunction: SenderFunction? = null

    fun setSenderFunction(sender : SenderFunction) {
        senderFunction = sender
    }

    override fun send(byteArray: ByteArray): Boolean {
        if (senderFunction != null) {
            return senderFunction!!.invoke(byteArray)
        }
        throw NoSenderFunctionFound()
    }
}