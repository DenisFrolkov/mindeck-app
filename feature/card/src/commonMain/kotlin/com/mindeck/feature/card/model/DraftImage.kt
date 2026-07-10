package com.mindeck.feature.card.model

import androidx.compose.runtime.Immutable
import com.mindeck.domain.media.MAX_IMAGE_BYTES

@Immutable
class DraftImage(
    val url: String,
    val bytes: ByteArray,
) {
    override fun equals(other: Any?): Boolean = other is DraftImage && other.url == url

    override fun hashCode(): Int = url.hashCode()
}

fun draftImageFromBytes(
    url: String,
    bytes: ByteArray,
): DraftImage? = if (bytes.size <= MAX_IMAGE_BYTES) DraftImage(url, bytes) else null
