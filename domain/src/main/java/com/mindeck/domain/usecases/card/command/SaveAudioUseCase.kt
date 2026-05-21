package com.mindeck.domain.usecases.card.command

import com.mindeck.domain.repository.AudioRepository
import javax.inject.Inject

class SaveAudioUseCase
@Inject
constructor(
    private val audioRepository: AudioRepository,
) {
    suspend operator fun invoke(uri: String): String = audioRepository.saveAudio(uri = uri)
}
