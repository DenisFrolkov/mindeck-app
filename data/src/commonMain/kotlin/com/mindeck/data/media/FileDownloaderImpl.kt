package com.mindeck.data.media

import com.mindeck.domain.exception.DomainError
import com.mindeck.domain.media.FileDownloader
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.coroutines.CancellationException

class FileDownloaderImpl(
    private val client: HttpClient,
) : FileDownloader {
    override suspend fun download(url: String): ByteArray {
        if (!UrlValidator.isSafe(url)) throw DomainError.NetworkError()
        return try {
            client.get(url).body()
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            throw DomainError.NetworkError()
        }
    }
}
