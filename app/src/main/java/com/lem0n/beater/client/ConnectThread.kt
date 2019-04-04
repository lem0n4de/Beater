package com.lem0n.beater.client

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import com.lem0n.beater.internal.Config
import com.lem0n.common.EventBus.IEventBus
import com.lem0n.common.EventBus.onConnectionFailed
import com.lem0n.common.EventBus.onConnectionSuccessful
import org.koin.core.KoinComponent
import org.koin.core.inject
import timber.log.Timber

class ConnectThread(private val dev : BluetoothDevice) : Thread(), KoinComponent {
    private val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
    private val bus : IEventBus by inject()
    private val socket : BluetoothSocket by lazy(LazyThreadSafetyMode.NONE) {
        dev.createRfcommSocketToServiceRecord(Config.uUID)
    }

    override fun run() {
        bluetoothAdapter.cancelDiscovery()

        try {
            socket.connect()
            Timber.d("Socket has connected.")
            bus.publish(onConnectionSuccessful())
        } catch (e : Exception) {
            Timber.e(e, "Error while socket.connect()")
            bus.publish(onConnectionFailed())
            cancel()
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