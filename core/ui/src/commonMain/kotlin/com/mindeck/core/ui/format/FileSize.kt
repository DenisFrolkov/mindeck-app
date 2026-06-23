package com.mindeck.core.ui.format

import androidx.compose.runtime.Composable
import mindeck_app.core.ui.generated.resources.Res
import mindeck_app.core.ui.generated.resources.file_size_unit_bytes
import mindeck_app.core.ui.generated.resources.file_size_unit_kb
import mindeck_app.core.ui.generated.resources.file_size_unit_mb
import org.jetbrains.compose.resources.stringResource
import kotlin.math.round

private const val BYTES_PER_KB = 1024.0
private const val BYTES_PER_MB = BYTES_PER_KB * 1024.0

enum class FileSizeUnit {
    BYTES,
    KILOBYTES,
    MEGABYTES,
}

data class FormattedFileSize(
    val value: Double,
    val unit: FileSizeUnit,
)

fun formatFileSize(bytes: Int): FormattedFileSize =
    when {
        bytes >= BYTES_PER_MB -> FormattedFileSize(bytes / BYTES_PER_MB, FileSizeUnit.MEGABYTES)
        bytes >= BYTES_PER_KB -> FormattedFileSize(bytes / BYTES_PER_KB, FileSizeUnit.KILOBYTES)
        else -> FormattedFileSize(bytes.toDouble(), FileSizeUnit.BYTES)
    }

@Composable
fun FormattedFileSize.toLabel(): String {
    val unitLabel =
        stringResource(
            when (unit) {
                FileSizeUnit.BYTES -> Res.string.file_size_unit_bytes
                FileSizeUnit.KILOBYTES -> Res.string.file_size_unit_kb
                FileSizeUnit.MEGABYTES -> Res.string.file_size_unit_mb
            },
        )
    return "${value.toOneDecimalString()} $unitLabel"
}

private fun Double.toOneDecimalString(): String {
    val rounded = round(this * 10) / 10
    val whole = rounded.toLong()
    return if (rounded == whole.toDouble()) whole.toString() else rounded.toString()
}
