package com.lem0n.hotspot

import android.content.Context
import android.net.wifi.WifiConfiguration
import com.lem0n.common.communicators.ClientCommunicator
import com.lem0n.common.communicators.ServerCommunicator
import com.lem0n.hotspot.SignalContract
import com.lem0n.hotspot.core.HotspotClient
import com.lem0n.hotspot.core.HotspotServer
import com.lem0n.hotspot.data.HotspotRepository
import com.lem0n.hotspot.ui.HotspotViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.KoinComponent
import org.koin.core.inject
import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.core.context.loadKoinModules
import timber.log.Timber
import java.util.*

/**
 * Created by lem0n on 12/04/19.
 */
/**
 * HotspotServer and HotspotClient talk with each other through
 * server and client communicators. If server returns UUID back,
 * it means operation was successfull.
 */
object HotspotLib : KoinComponent {
    private val serverCommunicator: ServerCommunicator by inject()
    private val clientCommunicator: ClientCommunicator by inject()

    fun init(appContext: Context) {
        val hotspotModule = module {
            single<HotspotRepository> { HotspotRepository(appContext) }
            viewModel<HotspotViewModel> { HotspotViewModel(get()) }
        }
        loadKoinModules(hotspotModule)

        val hotspotServer = HotspotServer(appContext)
        val hotspotClient = HotspotClient(appContext)
    }
}
