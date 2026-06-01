package com.mindeck.presentation

import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.destroy
import com.arkivanov.essenty.lifecycle.resume
import com.arkivanov.essenty.lifecycle.stop
import com.mindeck.presentation.ui.navigation.RootComponent

class IosApplicationComponent {
    private val lifecycle = LifecycleRegistry()
    val rootComponent: RootComponent = RootComponent(DefaultComponentContext(lifecycle))

    init {
        lifecycle.resume()
    }

    fun onStop() = lifecycle.stop()
    fun onDestroy() = lifecycle.destroy()
}
