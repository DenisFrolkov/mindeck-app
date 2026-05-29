package com.mindeck.data.clock

import com.mindeck.domain.service.ClockRepository
import kotlin.time.Clock

class ClockRepositoryImpl : ClockRepository {
    override fun now(): Long = Clock.System.now().toEpochMilliseconds()
}
