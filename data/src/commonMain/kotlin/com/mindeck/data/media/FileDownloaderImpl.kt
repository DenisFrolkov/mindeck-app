package com.mindeck.data.media

import com.mindeck.domain.exception.DomainError
import com.mindeck.domain.media.FileDownloader
import com.mindeck.domain.media.MAX_IMAGE_BYTES
import io.ktor.client.HttpClient
import io.ktor.client.request.prepareGet
import io.ktor.client.statement.bodyAsChannel
import io.ktor.http.contentLength
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.readAvailable
import kotlinx.coroutines.CancellationException

class FileDownloaderImpl(
    private val client: HttpClient,
) : FileDownloader {
    override suspend fun download(url: String): ByteArray {
        if (!UrlValidator.isSafe(url)) throw DomainError.NetworkError()
        return try {
            client.prepareGet(url).execute { response ->
                val declared = response.contentLength()
                if (declared != null && declared > MAX_IMAGE_BYTES) throw DomainError.NetworkError()
                response.bodyAsChannel().readCapped()
            }
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            throw DomainError.NetworkError()
        }
    }
}

private suspend fun ByteReadChannel.readCapped(): ByteArray {
    val cap = MAX_IMAGE_BYTES + 1
    val buffer = ByteArray(cap)
    var total = 0
    while (total < cap) {
        val read = readAvailable(buffer, total, cap - total)
        if (read == -1) break
        total += read
    }
    if (total > MAX_IMAGE_BYTES) throw DomainError.NetworkError()
    return buffer.copyOf(total)
}
