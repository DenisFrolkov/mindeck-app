package com.mindeck.data.converter

import androidx.room.TypeConverter
import com.mindeck.domain.models.DeckColor

class TypeConverter {
    @TypeConverter
    fun fromString(value: String?): DeckColor {
        return enumValueOf<DeckColor>(value ?: "BLUE")
    }

    @TypeConverter
    fun stringToDeckColor(deckColor: DeckColor): String {
        return deckColor.name
    }
}