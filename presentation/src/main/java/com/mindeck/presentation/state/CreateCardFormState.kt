package com.mindeck.presentation.state

import com.mindeck.domain.models.CardType

data class CreateCardFormState(
    val title: String = "",
    val tag: String = "",
    val selectedDeckId: Int? = null,
    val selectedType: CardType? = null,
    val cardImagePath: String? = null,
    val cardQuestionAudioPath: String? = null,
    val cardAnswerAudioPath: String? = null,
) {
    fun isValid(): Boolean = title.isNotBlank() &&
        selectedDeckId != null &&
        selectedType != null
}
