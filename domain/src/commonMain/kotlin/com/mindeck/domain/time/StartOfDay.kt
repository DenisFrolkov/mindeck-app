package com.mindeck.domain.time

import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Instant

fun startOfDayMillis(
    timeMillis: Long,
    zone: TimeZone,
    plusDays: Int = 0,
): Long =
    Instant
        .fromEpochMilliseconds(timeMillis)
        .toLocalDateTime(zone)
        .date
        .plus(plusDays, DateTimeUnit.DAY)
        .atStartOfDayIn(zone)
        .toEpochMilliseconds()
