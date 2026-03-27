package com.mindeck.domain.models

import org.junit.Assert.assertEquals
import org.junit.Test

class CardTypeTest {

    @Test
    fun `fromStableId returns SIMPLE for id 1`() {
        assertEquals(CardType.SIMPLE, CardType.fromStableId(1))
    }

    @Test
    fun `fromStableId returns COMPLEX for id 2`() {
        assertEquals(CardType.COMPLEX, CardType.fromStableId(2))
    }

    @Test(expected = IllegalArgumentException::class)
    fun `fromStableId throws IllegalArgumentException for unknown id`() {
        CardType.fromStableId(999)
    }
}
