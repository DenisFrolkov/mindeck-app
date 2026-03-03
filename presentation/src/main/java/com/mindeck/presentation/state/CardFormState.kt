package com.mindeck.presentation.state

import com.mindeck.domain.models.Card
import com.mindeck.domain.models.Deck

data class CardFormState(
    val card: Card? = null,
    val deck: Deck? = null,
) {
    fun isValid(): Boolean = card != null && deck != null
}
