package com.lem0n.call

import android.app.Activity
import android.app.Application
import android.content.Context
import timber.log.Timber

/**
 * Created by lem0n on 12/05/19.
 */
object CallLib {
    private var client : CallClient? = null

    fun initServer(context : Activity) {
        try {
            CallServer(context)
        } catch (e: Exception) {
            Timber.wtf(e)
        }
    }
    fun initClient(context : Context) {
        try {
            CallClient(context)
        } catch (e: Exception) {
            Timber.wtf(e)
        }
    }
}