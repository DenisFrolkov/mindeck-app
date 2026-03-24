package com.mindeck.domain.models

import junit.framework.TestCase.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test

class CardTypeTest {

    @Test
    fun `fromStableId returns correct type for known id`() {
        assertEquals(CardType.SIMPLE, CardType.fromStableId(1))
        assertEquals(CardType.COMPLEX, CardType.fromStableId(2))
    }

    @Test
    fun `fromStableId throws for unknown id`() {
        val exception = assertThrows(IllegalArgumentException::class.java) {
            CardType.fromStableId(999)
        }
        assertEquals("Unknown CardType stableId: 999", exception.message)
    }
}
