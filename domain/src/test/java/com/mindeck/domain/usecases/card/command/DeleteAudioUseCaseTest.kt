package com.mindeck.domain.usecases.card.command

import com.mindeck.domain.repository.AudioRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class DeleteAudioUseCaseTest {
    private val repository = mockk<AudioRepository>()
    private val useCase = DeleteAudioUseCase(repository)

    @Test
    fun `invoke deletes audio and returns true`() = runTest {
        val path = "/files/audio.m4a"
        every { repository.deleteAudio(path) } returns true

        val result = useCase(path)

        assertEquals(true, result)
        verify { repository.deleteAudio(path = path) }
    }
}
