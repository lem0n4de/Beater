package com.lem0n.beater.client

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.lem0n.beater.R
import com.lem0n.common.EventBus.IEventBus
import com.lem0n.common.EventBus.onConnectionSuccessful
import org.koin.android.ext.android.inject

@SuppressLint("CheckResult")
class ClientActivity : AppCompatActivity() {
    private val bus : IEventBus by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client)

        bus.listen(onConnectionSuccessful::class.java).subscribe {
            // TODO
        }

        Intent(this, ClientService::class.java).also {
            startActivity(it)
        }
    }
}
