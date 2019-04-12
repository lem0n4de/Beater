package com.lem0n.hotspot

import android.content.Context
import android.net.wifi.WifiConfiguration
import com.lem0n.common.communicators.ClientCommunicator
import com.lem0n.common.communicators.ServerCommunicator
import org.koin.core.KoinComponent
import org.koin.core.inject
import timber.log.Timber
import java.util.*

/**
 * Created by lem0n on 12/04/19.
 */
object HotspotLib : KoinComponent {
    private val serverCommunicator: ServerCommunicator by inject()
    private val clientCommunicator: ClientCommunicator by inject()

    private val ENABLE_HOTSPOT = UUID.randomUUID()
    private val DISABLE_HOTSPOT = UUID.randomUUID()

    fun init(appContext: Context) {
        val hotspotServer = Server(appContext)
        serverCommunicator.registerReceiver(ENABLE_HOTSPOT, hotspotServer::enableHotspot)
        serverCommunicator.registerReceiver(DISABLE_HOTSPOT, hotspotServer::disableHotspot)
    }


    class Server(private val appContext: Context) {
        private var ssid: String? = null
        private var password: String? = null

        fun enableHotspot(byteArray: ByteArray, length: Int, lockCount: Int) : Boolean {
            when (lockCount) {
                0 -> serverCommunicator.lock(ENABLE_HOTSPOT, 3)
                1 -> {
                    ssid = String(byteArray, 0, length)
                }
                2 -> {
                    password = String(byteArray, 0, length)
                }
                3 -> {
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
                        serverCommunicator.unlock(ENABLE_HOTSPOT)
                        return true
                    } catch (e: Exception) {
                        Timber.e(e)
                        serverCommunicator.unlock(ENABLE_HOTSPOT)
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
                return true
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
}