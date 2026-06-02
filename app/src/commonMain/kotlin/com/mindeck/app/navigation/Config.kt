package com.mindeck.app.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed interface Config {
    @Serializable data object Main : Config

    @Serializable data object Second : Config
}