package com.mindeck.presentation.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed interface Config {
    @Serializable data object Main : Config

    @Serializable data object Second : Config
}