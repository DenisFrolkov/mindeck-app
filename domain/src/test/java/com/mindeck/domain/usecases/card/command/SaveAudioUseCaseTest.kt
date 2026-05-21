package com.mindeck.domain.usecases.card.command

import com.mindeck.domain.repository.AudioRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class SaveAudioUseCaseTest {
    private val repository = mockk<AudioRepository>()
    private val useCase = SaveAudioUseCase(repository)

    private val fakeUri = "content://media/audio.m4a"

    @Test
    fun `invoke saves audio and returns file path`() = runTest {
        coEvery { repository.saveAudio(fakeUri) } returns "/files/audio.m4a"

        val result = useCase(fakeUri)

        assertEquals("/files/audio.m4a", result)
        coVerify(exactly = 1) { repository.saveAudio(fakeUri) }
    }
}
