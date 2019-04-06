package com.lem0n.beater.server

import android.app.Service
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothServerSocket
import android.bluetooth.BluetoothSocket
import android.content.Intent
import android.os.*
import androidx.annotation.RestrictTo
import com.lem0n.beater.BuildConfig
import com.lem0n.beater.MessagesContract
import com.lem0n.beater.internal.Config
import com.lem0n.common.EventBus.IEventBus
import com.lem0n.common.EventBus.onListeningConnections
import com.lem0n.common.EventBus.onReceivedConnection
import com.lem0n.common.Receivers.ServerReceiver
import org.koin.android.ext.android.inject
import timber.log.Timber

@RestrictTo(RestrictTo.Scope.TESTS)
var service: ServerService? = null

class ServerService : Service() {
    internal var clientList: ArrayList<Messenger> = ArrayList()

    private val bus : IEventBus by inject()
    private val serverReceiver : ServerReceiver by inject()

    companion object {
        private const val STATE_NONE = 0
        private const val STATE_LISTENING = 1
        private const val STATE_CONNECTING = 2
        private const val STATE_CONNECTED = 3
    }

    private var serverState: Int = STATE_NONE

    private val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()

    private var connectedThread: ConnectedThread? = null
    private var listenThread: ListenThread? = null

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            service = this@ServerService
        }
        listen()
    }

    fun resetState() {
        Timber.i("Resetting server serverState.")
        if (listenThread != null) {
            try {
                listenThread?.cancel()
            } catch (e: Exception) {
                Timber.e(e, "Listen thread couldn't be cancelled.")
            }
            listenThread = null
        } else {
            Timber.e("State was STATE_LISTENING but listenThread reference was null.")
        }
        if (connectedThread != null) {
            try {
                connectedThread?.cancel()
            } catch (e: Exception) {
                Timber.e(e, "Connected thread couldn't be cancelled.")
            }
            connectedThread = null
        } else {
            Timber.e("State was STATE_CONNECTED but connectedThread reference was null.")
        }
        serverState = STATE_NONE
    }

    fun listen() {
        listenThread = ListenThread()
        try {
            listenThread?.start()
            serverState = STATE_LISTENING
        } catch (e: Exception) {
            Timber.e(e, "Listen thread couldn't be started.")
        }
    }

    fun resetAndListen() {
        Timber.d("Reset and listen.")
        resetState()
        listen()
    }

    fun connect(socket : BluetoothSocket) {
        connectedThread = ConnectedThread(socket)
        try {
            connectedThread?.start()
            serverState = STATE_CONNECTED
            bus.publish(onReceivedConnection(socket.remoteDevice.name))
        } catch (e: Exception) {
            Timber.e(e, "Connected thread couldn't be started.")
        }
    }

    inner class ListenThread : Thread() {
        private val serverSocket: BluetoothServerSocket? by lazy(LazyThreadSafetyMode.NONE) {
            bluetoothAdapter.listenUsingRfcommWithServiceRecord(Config.NAME, Config.uUID)
        }

        override fun run() {
            val socket: BluetoothSocket? = try {
                Timber.d("Server socket is listening.")
                bus.publish(onListeningConnections())
                serverSocket?.accept()
            } catch (e: Exception) {
                Timber.e(e, "Socket's accept method failed.")
                cancel()
                null
            }

            if (socket != null) {
                Timber.d("Socket connected.")
                connect(socket)
            }
        }

        fun cancel() {
            try {
                serverSocket?.close()
            } catch (e: Exception) {
                Timber.e(e, "Server socket couldn't be closed.")
            }
        }
    }

    inner class ConnectedThread(private val socket: BluetoothSocket) : Thread() {
        private val inputStream = socket.inputStream
        private val outputStream = socket.outputStream
        private var buffer = ByteArray(1024)

        override fun run() {
            var bytes: Int = 0
            Timber.d("Started ConnectedThread.")
            while (true) {
                try {
                    Timber.d("Waiting to read data from bluetooth.")
                    bytes = inputStream.read()
                    serverReceiver.receive(buffer, bytes)
                } catch (e: Exception) {
                    Timber.e(e, "Input stream disconnected.")
                    resetAndListen()
                    break
                }
            }
        }


        fun write(bt: ByteArray) {
            try {
                outputStream.write(bt)
            } catch (e: Exception) {
                Timber.e(e, "Data couldn't be sent to client.")
            }
        }

        fun cancel() {
            try {
                socket.close()
            } catch (e: Exception) {
                Timber.e(e, "Connected socket couldn't be closed.")
            }
        }
    }
}

@RestrictTo(RestrictTo.Scope.TESTS)
fun IBinder.getServerService() = service