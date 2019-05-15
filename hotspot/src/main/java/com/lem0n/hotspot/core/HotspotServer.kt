package com.lem0n.hotspot.core

import android.content.Context
import android.content.Intent
import android.net.wifi.WifiConfiguration
import android.provider.Settings
import com.lem0n.common.EventBus.IEventBus
import com.lem0n.common.communicators.ServerCommunicator
import com.lem0n.hotspot.SignalContract
import com.lem0n.hotspot.onHotspotTurnedOff
import com.lem0n.hotspot.onHotspotTurnedOn
import com.lem0n.hotspot.onTurnHotspotOn
import org.koin.core.KoinComponent
import org.koin.core.inject
import timber.log.Timber

/**
 * Created by lem0n on 16/04/19.
 */
class HotspotServer(private val appContext: Context) : KoinComponent {
    private val serverCommunicator: ServerCommunicator by inject()
    private val bus : IEventBus by inject()

    private var ssid: String? = null
    private var password: String? = null

    init {
        if (!Settings.System.canWrite(appContext)) {
            Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS).also { intent ->
                appContext.startActivity(intent)
            }
        }
        serverCommunicator.registerReceiver(SignalContract.ENABLE_HOTSPOT, ::enableHotspot)
        serverCommunicator.registerReceiver(SignalContract.DISABLE_HOTSPOT, ::disableHotspot)
        serverCommunicator.registerReceiver(SignalContract.CHECK_HOTSPOT, ::checkHotspot)
    }

    fun enableHotspot(byteArray: ByteArray, length: Int, lockCount: Int) : Boolean {
        when (lockCount) {
            0 -> {
                Timber.i("Locking receiving for 2 times.")
                serverCommunicator.lock(SignalContract.ENABLE_HOTSPOT, 2)
            }
            2 -> {
                ssid = String(byteArray, 0, length)
                Timber.d("SSID = $ssid")
            }
            1 -> {
                password = String(byteArray, 0, length)
                val wifiManager = appContext.getSystemService(Context.WIFI_SERVICE)
                val wifiConf = configureWifi()
                try {
                    val method = wifiManager::class.java.getDeclaredMethod(
                        "setWifiApEnabled",
                        wifiConf::class.java,
                        Boolean::class.java
                    )
                    method(wifiManager, wifiConf, true)
                    Timber.i("Hotspot enabled.")
                    serverCommunicator.unlock(SignalContract.ENABLE_HOTSPOT)
                    // Hotspot is ON.
                    serverCommunicator.send(SignalContract.ENABLE_HOTSPOT_SUCCESSFUL.toString().toByteArray())
                    Timber.i("Sent message to client..")
                    bus.publish(onHotspotTurnedOn())

                    return true
                } catch (e: Exception) {
                    Timber.e(e)
                    serverCommunicator.unlock(SignalContract.ENABLE_HOTSPOT)
                    serverCommunicator.send(SignalContract.ENABLE_HOTSPOT_FAILED.toString().toByteArray())
                    return false
                }
            }
        }
        return false
    }

    fun disableHotspot(byteArray: ByteArray, length: Int, lockCount: Int) : Boolean {
        val wifiManager = appContext.getSystemService(Context.WIFI_SERVICE)
        val wifiConf = WifiConfiguration()
        try {
            val method = wifiManager::class.java.getDeclaredMethod(
                "setWifiApEnabled",
                wifiConf::class.java,
                Boolean::class.java
            )
            method(wifiManager, wifiConf, false)
            Timber.i("Hotspot disabled.")
            serverCommunicator.send(SignalContract.DISABLE_HOTSPOT_SUCCESSFUL.toString().toByteArray())
            bus.publish(onHotspotTurnedOff())
            return true
        } catch (e: Exception) {
            Timber.e(e)
            serverCommunicator.send(SignalContract.DISABLE_HOTSPOT_FAILED.toString().toByteArray())
            return false
        }
    }

    fun checkHotspot(byteArray: ByteArray, length: Int, lockCount: Int) : Boolean {
        try {
            val wifiManager = appContext.getSystemService(Context.WIFI_SERVICE)
            val apState = wifiManager::class.java.getMethod("getWifiApState").invoke(wifiManager) as Int

            if (apState > 13) {
                Timber.i("Hotspot is already open.")
                serverCommunicator.send(SignalContract.CHECK_HOTSPOT_ENABLED.toString().toByteArray())
                return true
            } else {
                Timber.i("Hotspot is closed.")
                serverCommunicator.send(SignalContract.CHECK_HOTSPOT_DISABLED.toString().toByteArray())
                return true
            }
        } catch (e: Exception) {
            Timber.e(e)
            return false
        }
    }

    fun configureWifi(): WifiConfiguration {
        val wifiConf = WifiConfiguration()
        wifiConf.SSID = ssid
        wifiConf.preSharedKey = password
        wifiConf.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.SHARED)
        wifiConf.allowedProtocols.set(WifiConfiguration.Protocol.RSN)
        wifiConf.allowedProtocols.set(WifiConfiguration.Protocol.WPA)
        wifiConf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK)
        return wifiConf
    }
}