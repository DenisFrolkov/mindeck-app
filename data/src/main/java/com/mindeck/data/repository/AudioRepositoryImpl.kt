package com.mindeck.data.repository

import android.content.Context
import androidx.core.net.toUri
import com.mindeck.domain.repository.AudioRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.util.UUID
import javax.inject.Inject

class AudioRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
): AudioRepository {
    override suspend fun saveAudio(uri: String): String {
        val fileName = "${UUID.randomUUID()}.mp3"
        val savedFile = File(context.filesDir, fileName)
        context.contentResolver.openInputStream(uri.toUri())?.use { input ->
            context.openFileOutput(fileName, Context.MODE_PRIVATE).use { output ->
                input.copyTo(output)
            }
        }
        return savedFile.absolutePath
    }

    override fun deleteAudio(path: String): Boolean {
        return File(path).delete()
    }
}