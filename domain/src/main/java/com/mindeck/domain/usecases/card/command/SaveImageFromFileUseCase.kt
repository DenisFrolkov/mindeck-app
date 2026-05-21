package com.mindeck.domain.usecases.card.command

import com.mindeck.domain.repository.ImageRepository
import javax.inject.Inject

class SaveImageFromFileUseCase
@Inject
constructor(
    private val imageRepository: ImageRepository,
) {
    suspend operator fun invoke(uri: String): String = imageRepository.saveImageFromUri(uri = uri)
}
