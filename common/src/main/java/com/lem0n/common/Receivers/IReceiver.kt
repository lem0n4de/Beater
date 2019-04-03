package com.lem0n.common.Receivers


interface IReceiver {
    fun receive(byteArray: ByteArray, length : Int) : Boolean
}