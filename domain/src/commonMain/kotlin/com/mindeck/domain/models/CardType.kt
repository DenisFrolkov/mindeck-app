package com.mindeck.domain.models

enum class CardType(val stableId: Int) {
    SIMPLE(1),
    COMPLEX(2),
    ;

    companion object {
        fun fromStableId(id: Int): CardType =
            entries.find { it.stableId == id } ?: throw IllegalArgumentException("Unknown CardType stableId: $id")
    }
}
