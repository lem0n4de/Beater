package com.lem0n.beater.internal.EventBus

sealed class Event
data class onReceivedConnection(val string : String) : Event()