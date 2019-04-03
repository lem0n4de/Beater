package com.lem0n.common.EventBus

import io.reactivex.Observable

interface IEventBus {
    fun publish(event : Event)
    fun listen(eventType : Class<out Event>) : Observable<out Event>
}