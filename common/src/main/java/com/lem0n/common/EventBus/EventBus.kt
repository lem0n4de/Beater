package com.lem0n.common.EventBus

import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

object EventBus : IEventBus {
    private val publisher = PublishRelay.create<Event>()

    override fun publish(event: Event) {
        publisher.accept(event)
    }

    override fun <T : Event> listen(eventType: Class<T>): Observable<T> = publisher.ofType(eventType)
}