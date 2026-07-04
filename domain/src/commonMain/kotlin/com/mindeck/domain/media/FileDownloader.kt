package com.mindeck.domain.media

fun interface FileDownloader {
    suspend fun download(url: String): ByteArray
}
