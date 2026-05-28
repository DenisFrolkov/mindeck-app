package com.mindeck.domain.models

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class CardTypeTest {

    @Test
    fun `fromStableId returns SIMPLE for id 1`() {
        assertEquals(CardType.SIMPLE, CardType.fromStableId(1))
    }

    @Test
    fun `fromStableId returns COMPLEX for id 2`() {
        assertEquals(CardType.COMPLEX, CardType.fromStableId(2))
    }

    @Test
    fun `fromStableId throws IllegalArgumentException for unknown id`() {
        assertFailsWith<IllegalArgumentException> {
            CardType.fromStableId(999)
        }
    }
}
