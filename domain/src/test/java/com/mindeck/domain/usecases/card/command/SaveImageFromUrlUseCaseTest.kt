package com.mindeck.domain.usecases.card.command

import com.mindeck.domain.repository.ImageRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class SaveImageFromUrlUseCaseTest {
    private val repository = mockk<ImageRepository>()
    private val useCase = SaveImageFromUrlUseCase(repository)

    @Test
    fun `invoke saves image from url and returns file path`() = runTest {
        val url = "test_url"
        coEvery { repository.saveImageFromUrl(url) } returns "/files/image.jpg"

        val result = useCase(url)

        assertEquals("/files/image.jpg", result)
        coVerify(exactly = 1) { repository.saveImageFromUrl(url) }
    }
}
