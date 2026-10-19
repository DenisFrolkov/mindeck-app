package com.mindeck.domain.time

import kotlinx.datetime.TimeZone

class FixedTimeProvider(
    private val now: Long,
    private val zone: TimeZone = TimeZone.UTC,
) : TimeProvider {
    override fun now(): Long = now

    override fun timeZone(): TimeZone = zone
}
