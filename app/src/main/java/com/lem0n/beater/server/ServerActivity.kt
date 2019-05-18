package com.lem0n.beater.server

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.lem0n.beater.R
import com.lem0n.call.initServer
import com.lem0n.common.EventBus.IEventBus
import com.lem0n.common.EventBus.onListeningConnections
import com.lem0n.common.EventBus.onReceivedConnection
import com.lem0n.hotspot.HotspotLib
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_server.*
import org.koin.android.ext.android.inject
import timber.log.Timber

@SuppressLint("CheckResult")
class ServerActivity : AppCompatActivity() {
    private val bus : IEventBus by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        HotspotLib.initServer(this)
        initServer(this)
        setContentView(R.layout.activity_server)
        setTitle(R.string.server_activity_title)

        bus.listen(onReceivedConnection::class.java)
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError {}
            .subscribe {
                connectedDeviceText.text = getString(R.string.on_received_connection)
            }

        bus.listen(onListeningConnections::class.java)
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError {}
            .subscribe {
                connectedDeviceText.text = getString(R.string.on_listening_connections)
            }

        Intent(this, ServerService::class.java).also {
            startService(it)
        }
        Timber.d("Started server service.")
    }
}
