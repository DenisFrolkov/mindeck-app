package com.mindeck.feature.card.createCard

import com.mindeck.core.ui.theme.DeckSwatchColors
import com.mindeck.core.ui.theme.deck_blue_container
import com.mindeck.core.ui.theme.deck_blue_on
import com.mindeck.core.ui.theme.deck_blue_solid
import com.mindeck.core.ui.theme.deck_green_container
import com.mindeck.core.ui.theme.deck_green_on
import com.mindeck.core.ui.theme.deck_green_solid
import com.mindeck.core.ui.theme.deck_orange_container
import com.mindeck.core.ui.theme.deck_orange_on
import com.mindeck.core.ui.theme.deck_orange_solid
import com.mindeck.core.ui.theme.deck_pink_container
import com.mindeck.core.ui.theme.deck_pink_on
import com.mindeck.core.ui.theme.deck_pink_solid
import com.mindeck.core.ui.theme.deck_purple_container
import com.mindeck.core.ui.theme.deck_purple_on
import com.mindeck.core.ui.theme.deck_purple_solid
import com.mindeck.core.ui.theme.deck_yellow_container
import com.mindeck.core.ui.theme.deck_yellow_on
import com.mindeck.core.ui.theme.deck_yellow_solid
import com.mindeck.domain.models.DeckColor

internal fun DeckColor.toSwatch(): DeckSwatchColors =
    when (this) {
        DeckColor.BLUE -> DeckSwatchColors(deck_blue_container, deck_blue_on, deck_blue_solid)
        DeckColor.PINK -> DeckSwatchColors(deck_pink_container, deck_pink_on, deck_pink_solid)
        DeckColor.GREEN -> DeckSwatchColors(deck_green_container, deck_green_on, deck_green_solid)
        DeckColor.YELLOW -> DeckSwatchColors(deck_yellow_container, deck_yellow_on, deck_yellow_solid)
        DeckColor.PURPLE -> DeckSwatchColors(deck_purple_container, deck_purple_on, deck_purple_solid)
        DeckColor.ORANGE -> DeckSwatchColors(deck_orange_container, deck_orange_on, deck_orange_solid)
    }

internal fun deckSwatchColors(): List<DeckSwatchColors> = DeckColor.entries.map { it.toSwatch() }
