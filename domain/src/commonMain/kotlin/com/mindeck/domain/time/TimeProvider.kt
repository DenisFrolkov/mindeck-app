package com.mindeck.domain.time

import kotlinx.datetime.TimeZone

interface TimeProvider {
    fun now(): Long

    fun timeZone(): TimeZone
}
