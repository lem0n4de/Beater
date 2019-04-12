package com.lem0n.common.communicators

/**
 * Created by lem0n on 12/04/19.
 */
interface ICommunicator {
    fun send(byteArray: ByteArray)
    fun receive(byteArray : ByteArray, length : Int)
}

typealias IServerCommunicator = ICommunicator
typealias IClientCommunicator = ICommunicator