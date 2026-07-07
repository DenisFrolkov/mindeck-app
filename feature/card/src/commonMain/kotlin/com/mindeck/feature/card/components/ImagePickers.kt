package com.mindeck.feature.card.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.mindeck.feature.card.model.DraftImage
import com.mindeck.feature.card.model.PhotoSource
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

private const val MAX_FILES_IMAGE_BYTES = 5 * 1024 * 1024
private val FILES_IMAGE_EXTENSIONS = listOf("jpg", "jpeg", "png")

/**
 * Launches the native gallery/files pickers and reports the result back as plain
 * [DraftImage] callbacks, so callers never depend on FileKit types directly.
 */
@Composable
internal fun rememberImagePickers(
    onPickStart: () -> Unit,
    onPicked: (DraftImage) -> Unit,
    onPickFailed: () -> Unit,
    onShowCamera: () -> Unit,
): ImagePickers {
    val scope = rememberCoroutineScope()
    var pickJob by remember { mutableStateOf<Job?>(null) }

    fun pick(
        file: PlatformFile?,
        enforceSizeLimit: Boolean,
    ) {
        if (file == null) return
        pickJob =
            scope.launch {
                onPickStart()
                val image = readPickedImage(file, enforceSizeLimit)
                if (image != null) onPicked(image) else onPickFailed()
            }
    }

    val gallery =
        rememberFilePickerLauncher(type = FileKitType.Image) { file ->
            pick(file, enforceSizeLimit = false)
        }
    val files =
        rememberFilePickerLauncher(type = FileKitType.File(FILES_IMAGE_EXTENSIONS)) { file ->
            pick(file, enforceSizeLimit = true)
        }

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

private suspend fun readPickedImage(
    file: PlatformFile,
    enforceSizeLimit: Boolean,
): DraftImage? {
    // Not using FileKit's withScopedAccess { } here: it's an inline function published
    // with JVM 21 bytecode, which this module's JVM 17 target can't inline. Same
    // start/stop bracketing as withScopedAccess, just as plain (non-inline) calls.
    val accessGranted = file.startAccessingSecurityScopedResource()
    return try {
        val bytes = file.readBytes()
        if (enforceSizeLimit && !bytes.isWithinFilesSizeLimit()) {
            null
        } else {
            DraftImage(url = file.path, bytes = bytes)
        }
    } catch (e: Exception) {
        // readBytes() is FileKit expect/actual — the exact thrown type differs per
        // platform (IOException on JVM/Android, NSError-based on iOS).
        null
    } finally {
        if (accessGranted) file.stopAccessingSecurityScopedResource()
    }
}

private fun ByteArray.isWithinFilesSizeLimit() = size <= MAX_FILES_IMAGE_BYTES

/**
 * Camera captures have no [io.github.vinceglb.filekit.PlatformFile] path to key off of, so we
 * mint a synthetic one — it only needs to be unique per shot for [DraftImage.equals] to tell two
 * captures apart (otherwise a retake would be skipped as "the same" image on recomposition).
 */
internal fun cameraCaptureImage(bytes: ByteArray) = DraftImage(url = "camera:${Random.nextLong()}", bytes = bytes)
