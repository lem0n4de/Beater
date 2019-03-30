package com.lem0n.beater.server

import android.annotation.SuppressLint
import android.app.Service
import android.content.Intent
import android.os.Bundle
import android.os.Message
import androidx.appcompat.app.AppCompatActivity
import com.lem0n.beater.MessagesContract
import com.lem0n.beater.R
import com.lem0n.beater.internal.BaseServiceActivity
import com.lem0n.beater.internal.EventBus.IEventBus
import com.lem0n.beater.internal.EventBus.onReceivedConnection
import kotlinx.android.synthetic.main.activity_server.*
import org.koin.android.ext.android.inject

@SuppressLint("CheckResult")
class ServerActivity : BaseServiceActivity() {
    private val bus : IEventBus by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_server)

        bindToService(ServerService::class.java)

        bus.listen(onReceivedConnection::class.java).subscribe {
            if (it is onReceivedConnection) {
                connectedDeviceText.text = it.string
            }
        }
    }

    override fun handlerFunction(msg: Message) {
        when (msg.what) {
        }
    }
}
