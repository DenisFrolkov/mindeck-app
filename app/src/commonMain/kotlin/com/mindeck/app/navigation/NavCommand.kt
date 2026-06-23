package com.mindeck.app.navigation

import com.mindeck.feature.card.createCard.CreateCardNavigationEvent
import com.mindeck.feature.home.HomeNavigationEvent

internal sealed interface NavCommand {
    data class Push(
        val config: Config,
    ) : NavCommand

    data object Pop : NavCommand
}

internal fun RootComponent.navigate(command: NavCommand?) =
    when (command) {
        is NavCommand.Push -> push(command.config)
        NavCommand.Pop -> pop()
        null -> Unit
    }

internal fun homeDestinationFor(event: HomeNavigationEvent): NavCommand? =
    when (event) {
        HomeNavigationEvent.CreateCard -> NavCommand.Push(Config.CreateCard)
        HomeNavigationEvent.Search,
        HomeNavigationEvent.Settings,
        HomeNavigationEvent.ImportDeck,
        HomeNavigationEvent.Review,
        HomeNavigationEvent.Statistics,
        HomeNavigationEvent.AllDecks,
        is HomeNavigationEvent.OpenDeck,
        -> null
    }

internal fun createCardDestinationFor(event: CreateCardNavigationEvent): NavCommand =
    when (event) {
        CreateCardNavigationEvent.Back -> NavCommand.Pop
    }
