package com.example.cxensesdk.kotlin

import com.cxense.cxensesdk.model.EventStatus

internal fun Iterable<EventStatus>.asString() =
    joinToString {
        it.eventId.toString()
    }
