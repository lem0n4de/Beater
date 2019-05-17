package com.lem0n.call

import android.app.Activity
import android.content.Context
import com.lem0n.call.core.CallClient
import com.lem0n.call.core.CallServer
import com.lem0n.call.ui.CallViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module
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
            val callModule = module {
                viewModel<CallViewModel> { CallViewModel() }
            }
            loadKoinModules(callModule)
            CallClient(context)
        } catch (e: Exception) {
            Timber.wtf(e)
        }
    }
}