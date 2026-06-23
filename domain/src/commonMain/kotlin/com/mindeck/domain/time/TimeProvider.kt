package com.mindeck.domain.time

fun interface TimeProvider {
    fun now(): Long
}
