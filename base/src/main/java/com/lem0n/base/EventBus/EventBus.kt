package com.lem0n.base.EventBus

import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

object EventBus : IEventBus {
    private val publisher = PublishSubject.create<Any>()

    override fun publish(event: Event) {
        publisher.onNext(event)
    }

    override fun listen(eventType: Class<out Event>): Observable<out Event> = publisher.ofType(eventType)
}