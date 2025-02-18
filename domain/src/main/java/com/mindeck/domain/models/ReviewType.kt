package com.mindeck.domain.models

enum class ReviewType {
    EASY, MEDIUM, HARD, REPEAT;

    companion object {
        fun fromString(value: String): ReviewType =
            values().firstOrNull { it.name == value } ?: REPEAT
    }
}