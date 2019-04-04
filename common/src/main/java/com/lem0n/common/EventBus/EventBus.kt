package com.lem0n.common.EventBus

import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

object EventBus : IEventBus {
    private val publisher = PublishSubject.create<Any>()

    override fun publish(event: Event) {
        publisher.onNext(event)
    }

    override fun <T : Event> listen(eventType: Class<T>): Observable<T> = publisher.ofType(eventType)
}