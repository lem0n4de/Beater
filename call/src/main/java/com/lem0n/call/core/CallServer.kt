package com.lem0n.call.core

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import com.lem0n.call.SignalContract
import com.lem0n.common.EventBus.IEventBus
import com.lem0n.common.communicators.ServerCommunicator
import org.koin.core.KoinComponent
import org.koin.core.inject
import timber.log.Timber

/**
 * Created by lem0n on 12/05/19.
 */
class CallServer(private val context : Activity) : KoinComponent {
    private val serverCommunicator : ServerCommunicator by inject()
    private val bus : IEventBus by inject()

    private val permissions = arrayOf(Manifest.permission.READ_CALL_LOG, Manifest.permission.READ_CONTACTS)

    private fun checkPerms() {
        if (context.checkSelfPermission(permissions[0]) != PackageManager.PERMISSION_GRANTED ||
            context.checkSelfPermission(permissions[1]) != PackageManager.PERMISSION_DENIED) {
            context.requestPermissions(permissions, 222)
        }
    }

    init {
        checkPerms()
        serverCommunicator.registerReceiver(SignalContract.CALL_PHONE, ::callNumber)
    }

    fun callNumber(byteArray: ByteArray, length: Int, lockCount: Int) : Boolean {
        when (lockCount) {
            0 -> {
                Timber.d("Locking receiving 1 more time for number.")
                serverCommunicator.lock(SignalContract.CALL_PHONE, 1)
                return true
            }
            1 -> {
                try {
                    val number = String(byteArray, 0, length)
                    val intent = Intent(Intent.ACTION_CALL)
                    intent.data = Uri.parse("tel:$number")
                    context.startActivity(intent)
                    serverCommunicator.send(SignalContract.SUCCESS)
                    serverCommunicator.unlock(SignalContract.CALL_PHONE)
                    Timber.i("Number called.")
                    return true
                } catch (e : Exception) {
                    Timber.wtf(e)
                    serverCommunicator.send(SignalContract.FAILURE)
                    serverCommunicator.unlock(SignalContract.CALL_PHONE)
                    return false
                }
            }
            else -> return false
        }
    }

    fun getCallLog(byteArray: ByteArray, length: Int, lockCount: Int) {}

    fun getCallLogs(byteArray: ByteArray, length: Int, lockCount: Int) {}
}
