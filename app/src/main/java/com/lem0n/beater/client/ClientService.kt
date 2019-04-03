package com.lem0n.beater.client

import android.app.Service
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Intent
import android.os.IBinder
import com.lem0n.beater.internal.DeviceNotFoundException
import com.lem0n.common.EventBus.IEventBus
import com.lem0n.common.EventBus.onConnectionLost
import org.koin.android.ext.android.inject
import timber.log.Timber

class ClientService : Service() {
    private val bus : IEventBus by inject()
    private val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
    private var connectThread : ConnectThread? = null
    private var connectedThread : ConnectedThread? = null

    companion object {
        const val STATE_NONE = 0
        const val STATE_CONNECTING = 1
        const val STATE_CONNECTED = 2
    }
    private val clientState = STATE_NONE

    private fun getDevice(): BluetoothDevice {
        val bondedDevices = bluetoothAdapter.bondedDevices
        for (device in bondedDevices) {
            if (device.name == "Galaxy Note5") {
                Timber.d("Galaxy Note 5 found.")
                return device
            }
        }
        throw DeviceNotFoundException()
    }

    private fun connect(device : BluetoothDevice) {
        resetState()
        connectThread = ConnectThread(device)
        connectThread?.start()
        Timber.d("Connect thread started.")
    }

    @Synchronized
    private fun resetState() {
        if (clientState == STATE_CONNECTING) {
            Timber.d("clientState == STATE_CONNECTING, canceling it.")
            connectThread?.cancel()
            connectThread = null
        } else if (clientState == STATE_CONNECTED) {
            Timber.d("clientState == STATE_CONNECTED, canceling it.")
            connectedThread?.cancel()
            connectedThread = null
            bus.publish(onConnectionLost())
        }
    }

    fun tryConnection() {
        val dev = getDevice()
        connect(dev)
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        tryConnection()
    }
}
