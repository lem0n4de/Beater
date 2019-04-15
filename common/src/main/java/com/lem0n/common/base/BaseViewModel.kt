package com.lem0n.common.base

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

/**
 * Created by lem0n on 14/04/19.
 */
open class BaseViewModel : ViewModel(), CoroutineScope {
    private val job = Job()
    /**
     * Context of this scope.
     */
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}
