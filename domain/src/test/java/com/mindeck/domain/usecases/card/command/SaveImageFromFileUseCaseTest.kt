package com.mindeck.domain.usecases.card.command

import com.mindeck.domain.repository.ImageRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class SaveImageFromFileUseCaseTest {
    private val repository = mockk<ImageRepository>()
    private val useCase = SaveImageFromFileUseCase(repository)

    private val fakeUri = "content://media/image.jpg"

    @Test
    fun `invoke saves image and returns file path`() = runTest {
        coEvery { repository.saveImageFromUri(fakeUri) } returns "/files/image.jpg"

        val result = useCase(fakeUri)

        assertEquals("/files/image.jpg", result)
        coVerify(exactly = 1) { repository.saveImageFromUri(fakeUri) }
    }
}
