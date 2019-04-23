package com.lem0n.beater.client

import android.bluetooth.BluetoothSocket
import com.lem0n.common.EventBus.IEventBus
import com.lem0n.common.communicators.ClientCommunicator
import org.koin.core.KoinComponent
import org.koin.core.inject
import timber.log.Timber

class ConnectedThread(private val socket : BluetoothSocket) : Thread(), KoinComponent {
    private val outputStream = socket.outputStream
    private val inputStream = socket.inputStream
    private val buffer = ByteArray(1024)
    private val bus : IEventBus by inject()
    private val clientCommunicator : ClientCommunicator by inject()

    override fun run() {
        clientCommunicator.senderFunction = ::write
        var bytes : Int = 0
        while (true) {
            try {
                bytes = inputStream.read(buffer)
                clientCommunicator.receive(buffer, bytes)
            } catch (e : Exception) {
                Timber.e(e, "Input stream was disconnected.")
                cancel()
                break
            }
        }
    }

    fun write(buffer : ByteArray) {
        try {
            outputStream.write(buffer)
            Timber.d("Message sent. ")
        } catch (e : Exception) {
            Timber.e(e, "Failed attempt to send message.")
        }
    }

    fun cancel() {
        try {
            socket.close()
        } catch (e : Exception) {
            Timber.e(e, "Canceling socket has failed.")
        }
    }
}