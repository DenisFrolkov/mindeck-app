package com.mindeck.data.time

import com.mindeck.domain.time.TimeProvider
import kotlin.time.Clock

class SystemTimeProvider : TimeProvider {
    override fun now(): Long = Clock.System.now().toEpochMilliseconds()
}
