package com.lem0n.common

import com.lem0n.common.EventBus.EventBus
import com.lem0n.common.EventBus.IEventBus
import com.lem0n.common.Receivers.ServerReceiver
import org.koin.dsl.module


val commonModule = module {
    single<ServerReceiver> { ServerReceiver() }
    single<IEventBus> { EventBus }
}