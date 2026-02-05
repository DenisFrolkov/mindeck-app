package com.mindeck.presentation.ui.navigation

import kotlinx.serialization.Serializable

sealed interface NavigationRoute

@Serializable
data object MainRoute : NavigationRoute

@Serializable
data class CreationCardRoute(val deckId: Int? = null) : NavigationRoute

@Serializable
data object DecksRoute : NavigationRoute

@Serializable
data class DeckRoute(val deckId: Int) : NavigationRoute

@Serializable
data class CardRoute(val cardId: Int) : NavigationRoute

@Serializable
data class CardStudyRoute(val cardId: Int) : NavigationRoute

@Serializable
data object RepeatCardsRoute : NavigationRoute
