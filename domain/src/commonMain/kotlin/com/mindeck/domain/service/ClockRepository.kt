package com.mindeck.domain.service

fun interface ClockRepository {
    fun now(): Long
}
