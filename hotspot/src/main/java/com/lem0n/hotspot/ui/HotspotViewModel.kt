package com.lem0n.hotspot.ui

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import com.lem0n.common.EventBus.IEventBus
import com.lem0n.common.base.BaseViewModel
import com.lem0n.hotspot.*
import com.lem0n.hotspot.data.HotspotRepository
import com.lem0n.hotspot.data.database.entity.HotspotEntry
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.inject
import org.threeten.bp.Instant
import timber.log.Timber


@SuppressLint("CheckResult")
class HotspotViewModel(
    private val repo: HotspotRepository
) : BaseViewModel() {
    private val bus: IEventBus by inject()
    val state = MutableLiveData<HotspotEntry>()
    val lastOperationDone = MutableLiveData<Boolean>()

    private var _ssid: String = ""
    private var _password: String = ""

    init {
        state.value = HotspotEntry(false, "DENEME", "deneme", Instant.now())
        lastOperationDone.value = true
        initializeListeners()
        // TODO Update state here
    }

    private fun initializeListeners() {
        bus.listen(onHotspotTurnedOn::class.java)
            .subscribe {
                saveSettings(_ssid, _password, true)
                updateState(true)
                endOperation()
            }
        bus.listen(onTurningOnFailed::class.java)
            .subscribe {
                endOperation()
            }
        bus.listen(onHotspotTurnedOff::class.java)
            .subscribe {
                saveSettings(_ssid, _password, false)
                updateState(false)
                endOperation()
            }
        bus.listen(onTurningOffFailed::class.java)
            .subscribe {
                endOperation()
            }
    }

    private fun updateState(remoteState : Boolean) = launch(Dispatchers.IO) {
        val _state = HotspotEntry(remoteState, _ssid, _password, Instant.now())
        state.postValue(_state)
    }

    fun checkState() {
        startOperation()
        bus.publish(onCheckHotspot())
    }

    fun turnHotspotOn(ssid: String, password: String) {
        startOperation()
        _ssid = ssid
        _password = password
        bus.publish(onTurnHotspotOn(ssid, password))
    }

    fun turnHotspotOff() {
        startOperation()
        bus.publish(onTurnHotspotOff())
    }

    private fun saveSettings(ssid: String, password: String, state: Boolean) {
        launch(Dispatchers.IO) {
            Timber.i("Adding new entry.")
            repo.newEntry(ssid, password, state)
            Timber.i("Added new entry.")
        }
    }

    private fun startOperation() {
        lastOperationDone.postValue(false)
    }

    private fun endOperation() {
        lastOperationDone.postValue(true)
    }
}
