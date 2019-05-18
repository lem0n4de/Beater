package com.lem0n.call.ui

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import com.lem0n.call.onCallPhone
import com.lem0n.call.onPhoneCallEnd
import com.lem0n.call.onPhoneCallSuccess
import com.lem0n.call.onPhoneCallFailure
import com.lem0n.common.EventBus.IEventBus
import com.lem0n.common.base.BaseViewModel
import io.reactivex.disposables.CompositeDisposable
import org.koin.core.inject
import timber.log.Timber


@SuppressLint("CheckResult")
class CallViewModel : BaseViewModel() {
    private val bus : IEventBus by inject()

    private val compositeDisposable = CompositeDisposable()

    var callState = MutableLiveData<CallState>()
    var lastNumber : String = ""

    init {
        val d1 = bus.listen(onPhoneCallSuccess::class.java)
            .subscribe {
                callState.postValue(CallState.CALL_STARTED)
            }

        val d2 = bus.listen(onPhoneCallFailure::class.java)
            .subscribe {
                callState.postValue(CallState.ERROR)
            }

        val d3 = bus.listen(onPhoneCallEnd::class.java)
            .subscribe {
                callState.postValue(CallState.CALL_ENDED)
            }
        compositeDisposable.addAll(d1, d2, d3)
    }

    fun callNumber(number : String) {
        bus.publish(onCallPhone(number))
        lastNumber = number
        Timber.i("onCallPhone event fired with number $number")
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}
