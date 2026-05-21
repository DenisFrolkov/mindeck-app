package com.mindeck.domain.usecases.card.command

import com.mindeck.domain.repository.AudioRepository
import javax.inject.Inject

class DeleteAudioUseCase @Inject constructor(private val audioRepository: AudioRepository) {
    operator fun invoke(path: String): Boolean {
        return audioRepository.deleteAudio(path = path)
    }
}