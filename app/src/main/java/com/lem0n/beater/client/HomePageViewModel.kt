package com.lem0n.beater.client

import androidx.lifecycle.ViewModel
import com.lem0n.common.EventBus.IEventBus
import com.lem0n.common.EventBus.onConnectionFailed
import com.lem0n.common.EventBus.onConnectionSuccessful
import com.lem0n.common.EventBus.onRetryConnection
import com.lem0n.common.communicators.ClientCommunicator
import io.reactivex.android.schedulers.AndroidSchedulers
import org.koin.core.KoinComponent
import org.koin.core.inject

class HomePageViewModel(
    private val bus : IEventBus
) : ViewModel(), KoinComponent {
    private val clientCommunicator : ClientCommunicator by inject()
    var isConnected = false

    val conFail = bus.listen(onConnectionFailed::class.java)
        .observeOn(AndroidSchedulers.mainThread())
        .doOnError {}

    val conSuccess = bus.listen(onConnectionSuccessful::class.java)
        .observeOn(AndroidSchedulers.mainThread())
        .doOnError {}

    fun send(s : String) {
        if (isConnected == true) {
            clientCommunicator.send(s.toByteArray())
        }
    }

    fun retryConnection() {
        bus.publish(onRetryConnection())
    }
}