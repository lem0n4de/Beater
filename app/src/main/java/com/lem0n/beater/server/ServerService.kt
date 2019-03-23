package com.lem0n.beater.server

import android.app.Service
import android.content.Intent
import android.os.*
import androidx.annotation.RestrictTo
import com.lem0n.beater.Messages

@RestrictTo(RestrictTo.Scope.TESTS)
var service : ServerService? = null

class ServerService : Service() {
    internal var clientList : ArrayList<Messenger> = ArrayList()
    internal val messenger by lazy { Messenger(ActivityHandler()) }

    inner class ActivityHandler() : Handler() {
        override fun handleMessage(msg: Message?) {
            if (msg != null) {
                when (msg.what) {
                    Messages.REGISTER_CLIENT -> {
                        registerClient(msg.replyTo)
                    }
                }
            }
        }
    }

    fun registerClient(messenger: Messenger) {
        clientList.add(messenger)
    }

    override fun onBind(intent: Intent): IBinder {
        service = this@ServerService
        return messenger.binder
    }
}

@RestrictTo(RestrictTo.Scope.TESTS)
fun IBinder.getServerService() = service