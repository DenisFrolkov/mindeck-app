package com.mindeck.domain.media

/**
 * Persists card media (image bytes) into app-private storage and returns a
 * relative reference (path) that can be stored on a card and re-read later.
 */
fun interface MediaStorage {
    suspend fun saveImage(bytes: ByteArray): String
}
