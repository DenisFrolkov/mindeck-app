package com.mindeck.feature.card.model

import androidx.compose.runtime.Immutable

@Immutable
class DraftImage(
    val url: String,
    val bytes: ByteArray,
) {
    override fun equals(other: Any?): Boolean = other is DraftImage && other.url == url

    override fun hashCode(): Int = url.hashCode()
}
