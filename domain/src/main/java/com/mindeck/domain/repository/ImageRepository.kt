package com.mindeck.domain.repository

interface ImageRepository {
    suspend fun saveImageFromUri(uri: String): String

    suspend fun saveImageFromUrl(url: String): String

    fun deleteImage(path: String): Boolean
}
