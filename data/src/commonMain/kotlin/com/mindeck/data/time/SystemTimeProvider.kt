package com.mindeck.data.time

import com.mindeck.domain.time.TimeProvider
import kotlinx.datetime.TimeZone
import kotlin.time.Clock

class SystemTimeProvider : TimeProvider {
    override fun now(): Long = Clock.System.now().toEpochMilliseconds()

    override fun timeZone(): TimeZone = TimeZone.currentSystemDefault()
}
