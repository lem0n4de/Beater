package com.lem0n.call

import android.annotation.SuppressLint
import android.content.Context
import com.lem0n.common.EventBus.IEventBus
import com.lem0n.common.communicators.ClientCommunicator
import io.reactivex.disposables.CompositeDisposable
import org.koin.core.KoinComponent
import org.koin.core.inject
import timber.log.Timber

/**
 * Created by lem0n on 12/05/19.
 */
@SuppressLint("CheckResult")
class CallClient(context : Context) : KoinComponent {
    private val compositeDisposable = CompositeDisposable()
    private val clientCommunicator : ClientCommunicator by inject()
    private val bus : IEventBus by inject()

    init {
        val onCallPhone = bus.listen(onCallPhone::class.java)
            .subscribe {
                callNumber(it.number)
            }

        compositeDisposable.addAll(onCallPhone)

        clientCommunicator.registerReceiver(SignalContract.SUCCESS) { _,_,_ ->
            bus.publish(onPhoneCallSuccess())
            return@registerReceiver true
        }

        clientCommunicator.registerReceiver(SignalContract.FAILURE) { _,_,_ ->
            bus.publish(onPhoneCallFailure())
            return@registerReceiver true
        }
    }

    private fun callNumber(number : Number) {
        clientCommunicator.send(SignalContract.CALL_PHONE)
        Timber.i("Sent $number to other device.")
    }

    fun onDestroy() {
        compositeDisposable.dispose()
    }
}