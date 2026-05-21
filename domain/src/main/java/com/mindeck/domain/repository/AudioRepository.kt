package com.mindeck.domain.repository

interface AudioRepository {
    suspend fun saveAudio(uri: String): String
    fun deleteAudio(path: String): Boolean
}