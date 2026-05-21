package com.mindeck.data.repository

import android.content.Context
import android.net.Uri
import androidx.test.platform.app.InstrumentationRegistry
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.File

class AudioRepositoryImplTest {
    val context: Context = InstrumentationRegistry.getInstrumentation().targetContext

    private val repository = AudioRepositoryImpl(context = context)

    @Test
    fun saveAudio_returns_valid_file_path_when_uri_is_valid() = runTest {
        val file = File(context.filesDir, "test_audio.m4a").also {
            it.writeText("test content")
        }
        val uri = Uri.fromFile(file).toString()

        val path = repository.saveAudio(uri = uri)

        assertTrue(File(path).exists())
    }

    @Test
    fun deleteAudio_removes_file_from_storage() = runTest {
        val file = File(context.filesDir, "test_audio.m4a").also {
            it.writeText("test content")
        }
        val uri = Uri.fromFile(file).toString()

        val path = repository.saveAudio(uri = uri)
        repository.deleteAudio(path = path)

        assertTrue(!File(path).exists())
    }
}