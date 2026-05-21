package com.mindeck.data.repository

import android.content.Context
import android.net.Uri
import androidx.test.platform.app.InstrumentationRegistry
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.File

class ImageRepositoryImplTest {
    val context: Context = InstrumentationRegistry.getInstrumentation().targetContext
    private val httpClient = HttpClient(Android)
    private val repository = ImageRepositoryImpl(context = context, httpClient)

    @Test
    fun saveImageFromUri_returns_valid_file_path_when_uri_is_valid() = runTest {
        val file =
            File(context.filesDir, "test_image.jpg").also {
                it.writeText("test content")
            }
        val uri = Uri.fromFile(file).toString()

        val path = repository.saveImageFromUri(uri = uri)

        assertTrue(File(path).exists())
    }

    @Test
    fun saveImageFromUrl_returns_valid_file_path_when_url_is_reachable() = runTest {
        val url = "https://picsum.photos/200"
        val path = repository.saveImageFromUrl(url)

        assertTrue(File(path).exists())
    }

    @Test
    fun delete_image_removes_file_from_storage() = runTest {
        val file =
            File(context.filesDir, "test_image.jpg").also {
                it.writeText("test content")
            }
        val uri = Uri.fromFile(file).toString()

        val path = repository.saveImageFromUri(uri = uri)
        repository.deleteImage(path = path)

        assertTrue(!File(path).exists())
    }
}
