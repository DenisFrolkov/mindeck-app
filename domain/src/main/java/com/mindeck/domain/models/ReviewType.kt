package com.mindeck.domain.models

enum class ReviewType {
    EASY, MEDIUM, HARD, REPEAT;

    companion object {
        fun fromString(value: String): ReviewType =
            entries.firstOrNull { it.name == value } ?: throw IllegalArgumentException("Unknown ReviewType value: $value")
    }
}
