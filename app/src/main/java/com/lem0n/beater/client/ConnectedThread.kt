package com.lem0n.beater.client

import android.bluetooth.BluetoothSocket
import com.lem0n.common.EventBus.IEventBus
import com.lem0n.common.Receivers.ClientReceiver
import com.lem0n.common.senders.ClientSender
import org.koin.core.KoinComponent
import org.koin.core.inject
import org.koin.core.parameter.parametersOf
import timber.log.Timber

class ConnectedThread(private val socket : BluetoothSocket) : Thread(), KoinComponent {
    private val outputStream = socket.outputStream
    private val inputStream = socket.inputStream
    private val buffer = ByteArray(1024)
    private val bus : IEventBus by inject()
    private val clientReceiver : ClientReceiver by inject()
    private val clientSender : ClientSender by inject()

    override fun run() {
        clientSender.setSenderFunction(::write)
        var bytes : Int = 0
        while (true) {
            try {
                bytes = inputStream.read(buffer)
            } catch (e : Exception) {
                Timber.e(e, "Input stream was disconnected.")
                cancel()
                break
            }
        }
        clientReceiver.receive(buffer, bytes)
    }

    fun write(buffer : ByteArray) : Boolean {
        try {
            outputStream.write(buffer)
            Timber.d("Message sent.")
            return true
        } catch (e : Exception) {
            Timber.e(e, "Failed attempt to send message.")
            return false
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