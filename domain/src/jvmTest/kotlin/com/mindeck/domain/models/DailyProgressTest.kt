package com.mindeck.domain.models

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class DailyProgressTest {
    @Test
    fun `progress is completed when cards were reviewed and nothing is due`() {
        assertTrue(DailyProgress(reviewedCount = 3, dueCount = 0).isCompleted)
    }

    @Test
    fun `progress is not completed while cards are still due`() {
        assertFalse(DailyProgress(reviewedCount = 3, dueCount = 1).isCompleted)
    }

    @Test
    fun `progress is not completed when nothing was reviewed and nothing is due`() {
        assertFalse(DailyProgress(reviewedCount = 0, dueCount = 0).isCompleted)
    }

    @Test
    fun `progress is not completed before the first review`() {
        assertFalse(DailyProgress(reviewedCount = 0, dueCount = 5).isCompleted)
    }
}
