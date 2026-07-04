package com.mindeck.data.media

import com.mindeck.domain.exception.DomainError
import com.mindeck.domain.media.MediaStorage
import io.github.vinceglb.filekit.FileKit
import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.filesDir
import io.github.vinceglb.filekit.write
import kotlinx.coroutines.CancellationException
import kotlin.random.Random

class MediaStorageImpl : MediaStorage {
    // Writes into app-private files directory. Only the suspend write() touches disk and it
    // dispatches to Dispatchers.IO internally, so this stays main-safe without extra plumbing.
    override suspend fun saveImage(bytes: ByteArray): String =
        try {
            val name = "card_media_${Random.nextLong().toULong().toString(radix = 16)}"
            PlatformFile(FileKit.filesDir, name).write(bytes)
            name
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            throw DomainError.StorageError()
        }
}
