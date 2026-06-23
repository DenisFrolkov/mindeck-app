package com.mindeck.app.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed interface Config {
    @Serializable data object Home : Config

    @Serializable data object CreateCard : Config
}
