package com.lem0n.common.EventBus

sealed class Event
class onListeningConnections : Event()
data class onReceivedConnection(val string : String) : Event()
class onConnectionSuccessful : Event()
class onConnectionFailed : Event()
class onRetryConnection : Event()
class onConnectionLost : Event()
data class onMessageArrived(val buffer : ByteArray, val bytes : Int) : Event()
