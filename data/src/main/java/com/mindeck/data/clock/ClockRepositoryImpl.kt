package com.mindeck.data.clock

import com.mindeck.domain.service.ClockRepository
import javax.inject.Inject

class ClockRepositoryImpl @Inject constructor() : ClockRepository {
    override fun now(): Long = System.currentTimeMillis()
}
