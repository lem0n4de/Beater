package com.lem0n.common.EventBus

import android.bluetooth.BluetoothSocket

abstract class Event
class onListeningConnections : Event()
data class onReceivedConnection(val string : String) : Event()
class onConnectionSuccessful : Event()
data class onConnectedEvent(val socket : BluetoothSocket) : Event()
class onConnectionFailed : Event()
class onRetryConnection : Event()
data class onMessageArrived(val buffer : ByteArray, val bytes : Int) : Event()