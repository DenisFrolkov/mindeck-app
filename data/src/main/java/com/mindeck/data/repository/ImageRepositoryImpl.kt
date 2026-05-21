package com.mindeck.data.repository

import android.content.Context
import androidx.core.net.toUri
import com.mindeck.domain.repository.ImageRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import java.io.File
import java.util.UUID
import javax.inject.Inject

class ImageRepositoryImpl
@Inject
constructor(
    @ApplicationContext private val context: Context,
    private val httpClient: HttpClient,
) : ImageRepository {
    override suspend fun saveImageFromUri(uri: String): String {
        val fileName = "${UUID.randomUUID()}.jpg"
        val savedFile = File(context.filesDir, fileName)
        context.contentResolver.openInputStream(uri.toUri())?.use { input ->
            context.openFileOutput(fileName, Context.MODE_PRIVATE).use { output ->
                input.copyTo(output)
            }
        }
        return savedFile.absolutePath
    }

    override suspend fun saveImageFromUrl(url: String): String {
        val response: HttpResponse = httpClient.get(url)
        val imageByte = response.body<ByteArray>()
        val fileName = "${UUID.randomUUID()}.jpg"

        val savedFile = File(context.filesDir, fileName)
        imageByte.inputStream().use { input ->
            context.openFileOutput(fileName, Context.MODE_PRIVATE).use { output ->
                input.copyTo(output)
            }
        }
        return savedFile.absolutePath
    }

    override fun deleteImage(path: String): Boolean = File(path).delete()
}
