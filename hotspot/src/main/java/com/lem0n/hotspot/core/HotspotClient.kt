package com.lem0n.hotspot.core

import android.annotation.SuppressLint
import android.content.Context
import com.lem0n.common.EventBus.IEventBus
import com.lem0n.common.communicators.ClientCommunicator
import com.lem0n.hotspot.*
import com.lem0n.hotspot.data.IHotspotRepository
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.context.GlobalContext
import org.koin.core.inject
import timber.log.Timber

/**
 * Created by lem0n on 16/04/19.
 */
@SuppressLint("CheckResult")
class HotspotClient(private val appContext: Context) : KoinComponent {
    private val compositeDisposable = CompositeDisposable()
    private val bus: IEventBus by inject()
    private val clientCommunicator: ClientCommunicator by inject()

    init {
        val hotspotOn = bus.listen(onTurnHotspotOn::class.java)
            .subscribe {
                enableHotspot(it.ssid, it.password)
            }
        val hotspotOff = bus.listen(onTurnHotspotOff::class.java)
            .subscribe {
                disableHotspot()
            }
        val checkHotspotState = bus.listen(onCheckHotspot::class.java)
            .subscribe {
                checkHotspot()
            }
        compositeDisposable.addAll(hotspotOn, hotspotOff, checkHotspotState)

        clientCommunicator.registerReceiver(SignalContract.ENABLE_HOTSPOT_SUCCESSFUL) { _, _, _ ->
            bus.publish(onHotspotTurnedOn())
            return@registerReceiver true
        }
        clientCommunicator.registerReceiver(SignalContract.ENABLE_HOTSPOT_FAILED) { _, _, _ ->
            bus.publish(onTurningOnFailed())
            return@registerReceiver true
        }
        clientCommunicator.registerReceiver(SignalContract.DISABLE_HOTSPOT_SUCCESSFUL) { _, _, _ ->
            bus.publish(onHotspotTurnedOff())
            return@registerReceiver true
        }
        clientCommunicator.registerReceiver(SignalContract.DISABLE_HOTSPOT_FAILED) { _, _, _ ->
            bus.publish(onTurningOffFailed())
            return@registerReceiver true
        }
        clientCommunicator.registerReceiver(SignalContract.CHECK_HOTSPOT_ENABLED) { _, _, _ ->
            bus.publish(onHotspotTurnedOn())
            return@registerReceiver true
        }
        clientCommunicator.registerReceiver(SignalContract.CHECK_HOTSPOT_DISABLED) { _, _, _ ->
            bus.publish(onHotspotTurnedOff())
            return@registerReceiver true
        }
    }

    private fun disableHotspot() {
        Timber.i("Sending disable hotspot message.")
        clientCommunicator.send(SignalContract.DISABLE_HOTSPOT.toString().toByteArray())
    }

    private fun enableHotspot(ssid: String, password: String) {
        val uuidBytearray = SignalContract.ENABLE_HOTSPOT.toString().toByteArray(Charsets.UTF_8)
        val ssidBytearray = ssid.toByteArray()
        val passwordBytearray = password.toByteArray()

        clientCommunicator.send(uuidBytearray)
        Thread.sleep(500L)
        clientCommunicator.send(ssidBytearray)
        Thread.sleep(500L)
        clientCommunicator.send(passwordBytearray)
    }

    private fun checkHotspot() {
        clientCommunicator.send(SignalContract.CHECK_HOTSPOT.toString().toByteArray())
        Timber.i("Sent check hotspot message to server.")
    }

    fun onDestroy() {
        compositeDisposable.dispose()
    }
}
