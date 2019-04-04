package com.lem0n.common.EventBus

import io.reactivex.Observable

interface IEventBus {
    fun publish(event : Event)
    fun <T : Event> listen(eventType : Class<T>) : Observable<T>
}