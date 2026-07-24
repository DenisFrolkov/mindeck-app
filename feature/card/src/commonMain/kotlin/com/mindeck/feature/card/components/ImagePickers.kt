package com.mindeck.feature.card.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.mindeck.feature.card.model.DraftImage
import com.mindeck.feature.card.model.PhotoSource
import com.mindeck.feature.card.model.draftImageFromBytes
import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.dialogs.FileKitType
import io.github.vinceglb.filekit.dialogs.compose.PickerResultLauncher
import io.github.vinceglb.filekit.dialogs.compose.rememberFilePickerLauncher
import io.github.vinceglb.filekit.path
import io.github.vinceglb.filekit.readBytes
import io.github.vinceglb.filekit.startAccessingSecurityScopedResource
import io.github.vinceglb.filekit.stopAccessingSecurityScopedResource
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.random.Random

private val FILES_IMAGE_EXTENSIONS = listOf("jpg", "jpeg", "png")

@Composable
internal fun rememberImagePickers(
    onPickStart: () -> Unit,
    onPicked: (DraftImage) -> Unit,
    onPickFailed: () -> Unit,
    onShowCamera: () -> Unit,
): ImagePickers {
    val scope = rememberCoroutineScope()
    var pickJob by remember { mutableStateOf<Job?>(null) }

    fun pick(file: PlatformFile?) {
        if (file == null) return
        pickJob =
            scope.launch {
                onPickStart()
                val image = readPickedImage(file)
                if (image != null) onPicked(image) else onPickFailed()
            }
    }

    val gallery =
        rememberFilePickerLauncher(type = FileKitType.Image) { pick(it) }
    val files =
        rememberFilePickerLauncher(type = FileKitType.File(FILES_IMAGE_EXTENSIONS)) { pick(it) }

    return remember(gallery, files, onShowCamera) {
        ImagePickers(gallery, files, onShowCamera, onCancel = { pickJob?.cancel() })
    }
}

internal class ImagePickers(
    private val gallery: PickerResultLauncher,
    private val files: PickerResultLauncher,
    private val onShowCamera: () -> Unit,
    private val onCancel: () -> Unit,
) {
    fun launch(source: PhotoSource) {
        when (source) {
            PhotoSource.GALLERY -> gallery.launch()
            PhotoSource.FILES -> files.launch()
            PhotoSource.CAMERA -> onShowCamera()
        }
    }

    fun cancel() = onCancel()
}

private suspend fun readPickedImage(file: PlatformFile): DraftImage? {
    val accessGranted = file.startAccessingSecurityScopedResource()
    return try {
        draftImageFromBytes(url = file.path, bytes = file.readBytes())
    } catch (e: Exception) {
        null
    } finally {
        if (accessGranted) file.stopAccessingSecurityScopedResource()
    }
}

internal fun cameraCaptureImage(bytes: ByteArray) = draftImageFromBytes(url = "camera:${Random.nextLong()}", bytes = bytes)
