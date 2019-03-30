package com.lem0n.beater.client

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import com.lem0n.beater.internal.Config
import timber.log.Timber

class ConnectThread(private val dev : BluetoothDevice) : Thread() {
    private val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
    private val socket : BluetoothSocket by lazy(LazyThreadSafetyMode.NONE) {
        dev.createRfcommSocketToServiceRecord(Config.uUID)
    }

    override fun run() {
        bluetoothAdapter.cancelDiscovery()

        try {
            socket.connect()
            Timber.d("Socket has connected.")
        } catch (e : Exception) {
            Timber.e(e, "Error while socket.connect()")
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