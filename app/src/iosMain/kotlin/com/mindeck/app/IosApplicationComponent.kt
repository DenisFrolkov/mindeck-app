package com.mindeck.app

import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.backhandler.BackDispatcher
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.destroy
import com.arkivanov.essenty.lifecycle.resume
import com.arkivanov.essenty.lifecycle.stop
import com.mindeck.app.navigation.RootComponent

class IosApplicationComponent {
    private val lifecycle = LifecycleRegistry()
    val backDispatcher = BackDispatcher()
    val rootComponent: RootComponent =
        RootComponent(DefaultComponentContext(lifecycle = lifecycle, backHandler = backDispatcher))

    init {
        lifecycle.resume()
    }

    fun onStop() = lifecycle.stop()

    fun onDestroy() = lifecycle.destroy()
}
