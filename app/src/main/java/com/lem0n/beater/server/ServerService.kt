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
import timber.log.Timber

@RestrictTo(RestrictTo.Scope.TESTS)
var service: ServerService? = null

class ServerService : Service() {
    internal var clientList: ArrayList<Messenger> = ArrayList()
    internal val messenger by lazy { Messenger(ActivityHandler()) }

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

    inner class ActivityHandler() : Handler() {
        override fun handleMessage(msg: Message?) {
            if (msg != null) {
                when (msg.what) {
                    MessagesContract.REGISTER_CLIENT -> {
                        registerClient(msg.replyTo)
                    }
                    MessagesContract.UNREGISTER_CLIENT -> {
                        unregisterClient(msg.replyTo)
                    }
                }
            }
        }
    }

    internal fun registerClient(messenger: Messenger) {
        clientList.add(messenger)
    }

    internal fun unregisterClient(messenger: Messenger) {
        clientList.remove(messenger)
    }

    override fun onBind(intent: Intent): IBinder {
        if (BuildConfig.DEBUG) {
            service = this@ServerService
        }
        return messenger.binder
    }

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    fun resetState() {
        Timber.i("Resetting server serverState.")
        if (serverState == STATE_LISTENING) {
            Timber.d("State == STATE_LISTENING")
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
        }

        if (serverState == STATE_CONNECTED) {
            Timber.d("State == STATE_CONNECTED")
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
        resetState()
        listen()
    }

    fun connect(socket : BluetoothSocket) {
        connectedThread = ConnectedThread(socket)
        try {
            connectedThread?.start()
            serverState = STATE_CONNECTED
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
                serverSocket?.accept()
            } catch (e: Exception) {
                Timber.e(e, "Socket's accept method failed.")
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
            var bytes: Int
            Timber.d("Started ConnectedThread.")
            while (true) {
                try {
                    Timber.d("Waiting to read data from bluetooth.")
                    bytes = inputStream.read()
                } catch (e: Exception) {
                    Timber.e(e, "Input stream disconnected.")
                    resetAndListen()
                }
            }

            // TODO Receive message and return feedback
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