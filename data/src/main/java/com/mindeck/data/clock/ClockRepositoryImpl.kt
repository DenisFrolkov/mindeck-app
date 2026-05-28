package com.mindeck.data.clock

import com.mindeck.domain.service.ClockRepository

class ClockRepositoryImpl : ClockRepository {
    override fun now(): Long = System.currentTimeMillis()
}
