package com.lem0n.common

import com.lem0n.common.EventBus.EventBus
import com.lem0n.common.EventBus.IEventBus
import com.lem0n.common.Receivers.ClientReceiver
import com.lem0n.common.communicators.IServerCommunicator
import com.lem0n.common.communicators.ServerCommunicator
import com.lem0n.common.senders.ClientSender
import org.koin.dsl.module


val commonModule = module {
    single<ClientReceiver> { ClientReceiver() }

    single<IServerCommunicator> { ServerCommunicator }


    single<ClientSender> { ClientSender() }
    single<IEventBus> { EventBus }
}