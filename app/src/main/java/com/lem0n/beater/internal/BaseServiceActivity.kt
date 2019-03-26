package com.lem0n.beater.internal

import android.annotation.SuppressLint
import android.app.Service
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.*
import androidx.appcompat.app.AppCompatActivity
import com.lem0n.beater.BuildConfig
import com.lem0n.beater.MessagesContract
import timber.log.Timber
import java.io.IOException

@SuppressLint("Registered")
open class BaseServiceActivity : AppCompatActivity() {
    /**
     * This is a class for handling arbitrary messages from service.
     */
    open fun handlerFunction() {}

    private inner class ServiceMessageHandler : Handler() {
        override fun handleMessage(msg: Message?) {
            if (msg != null) {
                when (msg.what) {
                    else -> handlerFunction()
                }
            }
        }
    }

    /**
     * This messenger is actually used in the bound service.
     * If we do not send this to service,
     * then the communication becomes only unidirectional.
     */
    open var messengerForService : Messenger = Messenger(ServiceMessageHandler())
    /**
     * This is the mesenger to send messages to service.
     */
    private var messengerToService : Messenger? = null
    /**
     * Whether activity is bound to a service or not.
     */
    private var bound : Boolean = false

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            messengerToService = Messenger(service)
            bound = true
            Timber.i("Connected to service.")

            try {
                val msg = Message.obtain(null, MessagesContract.REGISTER_CLIENT).apply {
                    replyTo = messengerForService
                }
                messengerToService!!.send(msg)
                Timber.d("Sent messenger to service.")
            } catch (e : IOException) {
                Timber.e(e, "Messenger couldn't be sent to service.")
            }
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            messengerToService = null
            bound = false
            Timber.i("Service disconnected.")
        }
    }

    fun bindToService(serviceClass : Class<Service>) {
        Timber.d("Bind to service with class ${serviceClass.simpleName}")
        Intent(this, serviceClass).also { intent ->
            bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
        }
    }

    fun unbindFromService() {
        Timber.d("Unbinding from service.")
        unbindService(serviceConnection)
        bound = false
    }
}