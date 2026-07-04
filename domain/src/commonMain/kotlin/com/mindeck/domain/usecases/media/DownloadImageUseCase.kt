package com.mindeck.domain.usecases.media

import com.mindeck.domain.media.FileDownloader

class DownloadImageUseCase(
    private val fileDownloader: FileDownloader,
) {
    suspend operator fun invoke(url: String): ByteArray = fileDownloader.download(url = url)
}
