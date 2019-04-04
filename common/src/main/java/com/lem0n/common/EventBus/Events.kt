package com.lem0n.common.EventBus

sealed class Event
data class onReceivedConnection(val string : String) : Event()
class onConnectionSuccessful : Event()
class onConnectionFailed : Event()
class onRetryConnection : Event()
data class onMessageArrived(val buffer : ByteArray, val bytes : Int) : Event()
class onConnectionLost : Event()