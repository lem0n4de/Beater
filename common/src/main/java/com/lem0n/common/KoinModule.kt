package com.lem0n.common

import com.lem0n.common.EventBus.EventBus
import com.lem0n.common.EventBus.IEventBus
import com.lem0n.common.Receivers.ClientReceiver
import com.lem0n.common.Receivers.ServerReceiver
import com.lem0n.common.senders.ClientSender
import com.lem0n.common.senders.SenderFunction
import org.koin.dsl.module


val commonModule = module {
    single<ServerReceiver> { ServerReceiver() }
    single<ClientReceiver> { ClientReceiver() }


    single<ClientSender> { ClientSender() }
    single<IEventBus> { EventBus }
}