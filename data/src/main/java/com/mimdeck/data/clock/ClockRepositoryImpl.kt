package com.mimdeck.data.clock

import com.mindeck.domain.repository.ClockRepository

class ClockRepositoryImpl : ClockRepository {
    override fun now(): Long = System.currentTimeMillis()
}
