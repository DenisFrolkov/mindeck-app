package com.mindeck.domain.usecases.media

import com.mindeck.domain.media.MediaStorage

class SaveImageUseCase(
    private val mediaStorage: MediaStorage,
) {
    suspend operator fun invoke(bytes: ByteArray): String = mediaStorage.saveImage(bytes)
}
