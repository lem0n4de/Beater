package com.lem0n.call

import com.lem0n.common.EventBus.Event

/**
 * Created by lem0n on 16/05/19.
 */
data class onCallPhone(val number: Int) : Event()
class onPhoneCallSuccess : Event()
class onPhoneCallFailure : Event()