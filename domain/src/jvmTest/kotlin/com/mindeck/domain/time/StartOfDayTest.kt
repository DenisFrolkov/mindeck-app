package com.mindeck.domain.time

import kotlinx.datetime.TimeZone
import org.junit.Assert.assertEquals
import org.junit.Test
import kotlin.time.Instant

class StartOfDayTest {
    private val utc = TimeZone.UTC
    private val moscow = TimeZone.of("Europe/Moscow")
    private val newYork = TimeZone.of("America/New_York")

    private fun millis(iso: String) = Instant.parse(iso).toEpochMilliseconds()

    @Test
    fun `utc zone maps a time to midnight utc`() {
        assertEquals(
            millis("2026-03-10T00:00:00Z"),
            startOfDayMillis(millis("2026-03-10T12:30:00Z"), utc),
        )
    }

    @Test
    fun `utc plus 3 treats 01 00 local as the new local day`() {
        assertEquals(
            millis("2026-03-09T21:00:00Z"),
            startOfDayMillis(millis("2026-03-09T22:00:00Z"), moscow),
        )
    }

    @Test
    fun `utc plus 3 keeps 23 59 local in the same local day`() {
        assertEquals(
            millis("2026-03-09T21:00:00Z"),
            startOfDayMillis(millis("2026-03-10T20:59:00Z"), moscow),
        )
    }

    @Test
    fun `utc minus 5 maps late evening utc to the previous local day`() {
        assertEquals(
            millis("2026-01-09T05:00:00Z"),
            startOfDayMillis(millis("2026-01-10T03:00:00Z"), newYork),
        )
    }

    @Test
    fun `plusDays moves by calendar days across a dst change`() {
        assertEquals(
            millis("2026-03-11T04:00:00Z"),
            startOfDayMillis(millis("2026-03-07T15:00:00Z"), newYork, plusDays = 4),
        )
    }

    @Test
    fun `dst change day is shorter than 24 hours but still starts at local midnight`() {
        assertEquals(
            millis("2026-03-08T05:00:00Z"),
            startOfDayMillis(millis("2026-03-08T20:00:00Z"), newYork),
        )
        assertEquals(
            millis("2026-03-09T04:00:00Z"),
            startOfDayMillis(millis("2026-03-09T12:00:00Z"), newYork),
        )
    }
}
