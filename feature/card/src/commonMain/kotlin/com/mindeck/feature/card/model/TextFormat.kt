package com.mindeck.feature.card.model

import kotlinx.serialization.Serializable

@Serializable
enum class TextFormat {
    BOLD,
    ITALIC,
    UNDERLINE,
    STRIKETHROUGH,
    BULLET_LIST,
    NUMBERED_LIST,
}
