package com.lem0n.common

import com.lem0n.common.Receivers.ServerReceiver
import org.koin.dsl.module


val commonModule = module {
    single<ServerReceiver> { ServerReceiver() }
}