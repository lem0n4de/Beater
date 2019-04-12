package com.lem0n.beater.client

import android.annotation.SuppressLint
import android.app.Service
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Intent
import android.os.IBinder
import com.lem0n.beater.internal.DeviceNotFoundException
import com.lem0n.common.EventBus.IEventBus
import com.lem0n.common.EventBus.onConnectedEvent
import com.lem0n.common.EventBus.onConnectionSuccessful
import com.lem0n.common.EventBus.onRetryConnection
import org.koin.android.ext.android.inject
import timber.log.Timber

@SuppressLint("CheckResult")
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
        }
    }

    fun tryConnection() {
        resetState()
        val dev = getDevice()
        connect(dev)
    }

    fun stabilizeConnection(socket : BluetoothSocket) {
        resetState()
        connectedThread = ConnectedThread(socket)
        connectedThread?.start()
        Timber.d("Connected thread started.")
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()

        bus.listen(onRetryConnection::class.java).doOnError{}.subscribe { tryConnection() }
        bus.listen(onConnectedEvent::class.java).doOnError{}.subscribe {
            stabilizeConnection(it.socket)
            bus.publish(onConnectionSuccessful())
        }
        tryConnection()
    }
}
