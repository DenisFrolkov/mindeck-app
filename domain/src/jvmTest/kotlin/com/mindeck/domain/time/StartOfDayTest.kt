package com.mindeck.domain.time

import org.junit.Assert.assertEquals
import org.junit.Test

class StartOfDayTest {
    private val dayMs = 86_400_000L

    @Test
    fun `time in the middle of a day maps to that day start`() {
        assertEquals(dayMs * 10, startOfDayMillis(dayMs * 10 + dayMs / 2))
    }

    @Test
    fun `exact day start maps to itself`() {
        assertEquals(dayMs * 10, startOfDayMillis(dayMs * 10))
    }

    @Test
    fun `last millisecond of a day maps to that day start`() {
        assertEquals(dayMs * 10, startOfDayMillis(dayMs * 11 - 1))
    }
}
