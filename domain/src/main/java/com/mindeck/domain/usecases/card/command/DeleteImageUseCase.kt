package com.mindeck.domain.usecases.card.command

import com.mindeck.domain.repository.ImageRepository
import javax.inject.Inject

class DeleteImageUseCase
@Inject
constructor(
    private val imageRepository: ImageRepository,
) {
    operator fun invoke(path: String): Boolean = imageRepository.deleteImage(path = path)
}
