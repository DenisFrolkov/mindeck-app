package com.mindeck.domain.usecases.card.command

import com.mindeck.domain.repository.ImageRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class DeleteImageUseCaseTest {
    private val repository = mockk<ImageRepository>()
    private val useCase = DeleteImageUseCase(repository)

    @Test
    fun `invoke deletes image and returns true when file exists`() = runTest {
        val path = "/files/image.jpg"
        every { repository.deleteImage(path) } returns true

        val result = useCase(path)

        assertEquals(true, result)
        verify { repository.deleteImage(path = path) }
    }
}
