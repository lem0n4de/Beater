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


@SuppressLint("CheckResult")
class HotspotViewModel(
    private val repo : HotspotRepository
) : BaseViewModel() {
    private val bus : IEventBus by inject()
    val state = MutableLiveData<HotspotEntry>()
    val lastOperationDone = MutableLiveData<Boolean>()

    init {
        state.value = HotspotEntry(false, "DENEME", "deneme", Instant.now())
        lastOperationDone.value = true
        initializeListeners()
        // TODO Update state here
    }

    private fun initializeListeners() {
        bus.listen(onHotspotTurnedOn::class.java)
            .subscribe {
                updateState()
                endOperation()
            }
        bus.listen(onTurningOnFailed::class.java)
            .subscribe {
                endOperation()
            }
        bus.listen(onHotspotTurnedOff::class.java)
            .subscribe {
                updateState()
                endOperation()
            }
        bus.listen(onTurningOffFailed::class.java)
            .subscribe {
                endOperation()
            }
    }

    private fun updateState() {
        launch {
            val _state = repo.getLastEntry()
                .observeOn(Schedulers.io())
                .blockingGet()
            state.value = _state
        }
    }

    fun turnHotspotOn(ssid : String, password : String) {
        startOperation()
        saveSettings(ssid, password, true)
        bus.publish(onTurnHotspotOn(ssid, password))
    }

    fun turnHotspotOff() {
        startOperation()
        saveSettings(state.value!!.ssid, state.value!!.password, false)
        bus.publish(onTurnHotspotOff())
    }

    private fun saveSettings(ssid : String, password : String, state : Boolean) {
        launch(Dispatchers.IO) {
            repo.newEntry(ssid, password, state)
        }
    }

    private fun startOperation() { lastOperationDone.value = false }
    private fun endOperation() { lastOperationDone.value = true }
}
