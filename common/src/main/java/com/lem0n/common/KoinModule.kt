package com.lem0n.common

import com.lem0n.common.EventBus.EventBus
import com.lem0n.common.EventBus.IEventBus
import com.lem0n.common.communicators.ClientCommunicator
import com.lem0n.common.communicators.ServerCommunicator
import org.koin.dsl.module


val commonModule = module {
    single<ServerCommunicator> { ServerCommunicator }
    single<ClientCommunicator> { ClientCommunicator }

    single<IEventBus> { EventBus }
}