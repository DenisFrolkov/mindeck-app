package com.mindeck.presentation.state

import com.mindeck.domain.models.CardType

data class CreateCardFormState(
    val title: String = "",
    val question: String = "",
    val answer: String = "",
    val tag: String = "",
    val selectedDeckId: Int? = null,
    val selectedType: CardType? = null,
) {
    fun isValid(): Boolean =
        title.isNotBlank() &&
            question.isNotBlank() &&
            answer.isNotBlank() &&
            selectedDeckId != null &&
            selectedType != null
}
