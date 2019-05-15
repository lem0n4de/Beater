package com.lem0n.hotspot

import com.lem0n.common.EventBus.Event

/**
 * Created by lem0n on 16/04/19.
 */
data class onTurnHotspotOn(val ssid : String, val password : String) : Event()
class onHotspotTurnedOn : Event()
class onTurningOnFailed : Event()
class onTurnHotspotOff : Event()
class onHotspotTurnedOff : Event()
class onTurningOffFailed : Event()
class onCheckHotspot : Event()