package com.mindeck.domain.usecases.card.command

import com.mindeck.domain.repository.ImageRepository
import javax.inject.Inject

class SaveImageFromUrlUseCase
@Inject
constructor(
    private val imageRepository: ImageRepository,
) {
    suspend operator fun invoke(url: String): String = imageRepository.saveImageFromUrl(url = url)
}
