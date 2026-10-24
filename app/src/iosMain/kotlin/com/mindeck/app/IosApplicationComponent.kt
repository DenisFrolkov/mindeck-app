package com.mindeck.app

import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.backhandler.BackDispatcher
import com.arkivanov.essenty.lifecycle.ApplicationLifecycle
import com.mindeck.app.navigation.RootComponent

class IosApplicationComponent {
    private val lifecycle = ApplicationLifecycle()
    val backDispatcher = BackDispatcher()
    val rootComponent: RootComponent =
        RootComponent(DefaultComponentContext(lifecycle = lifecycle, backHandler = backDispatcher))

    fun onDestroy() = lifecycle.destroy()
}
