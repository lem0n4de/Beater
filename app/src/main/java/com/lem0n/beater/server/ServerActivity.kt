package com.lem0n.beater.server

import android.app.Service
import android.content.Intent
import android.os.Bundle
import android.os.Message
import androidx.appcompat.app.AppCompatActivity
import com.lem0n.beater.MessagesContract
import com.lem0n.beater.R
import com.lem0n.beater.internal.BaseServiceActivity

class ServerActivity : BaseServiceActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_server)

        bindToService(ServerService::class.java)
    }

    override fun handlerFunction(msg: Message) {
        when (msg.what) {
        }
    }
}
