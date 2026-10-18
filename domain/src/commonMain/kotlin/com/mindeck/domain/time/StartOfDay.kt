package com.mindeck.domain.time

import kotlin.time.Duration.Companion.days

fun startOfDayMillis(timeMillis: Long): Long {
    val millisInDay = 1.days.inWholeMilliseconds
    return (timeMillis / millisInDay) * millisInDay
}
