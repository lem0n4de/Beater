package com.lem0n.common.senders


interface ISender {
    fun send(byteArray: ByteArray) : Boolean
}