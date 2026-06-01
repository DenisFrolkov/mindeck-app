package com.mindeck.presentation.ui.navigation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.DelicateDecomposeApi
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.value.Value

class RootComponent(
    componentContext: ComponentContext
) : ComponentContext by componentContext {
    private val navigation = StackNavigation<Config>()

    val stack: Value<ChildStack<Config, Child>> = childStack(
        source = navigation,
        serializer = Config.serializer(),
        initialConfiguration = Config.Main,
        handleBackButton = true,
        childFactory = ::createChild
    )

    @OptIn(DelicateDecomposeApi::class)
    fun push(config: Config) = navigation.push(config)
    fun pop() = navigation.pop()

    private fun createChild(config: Config, context: ComponentContext) =
        when (config) {
            is Config.Main -> Child.Main
            is Config.Second -> Child.Second
        }
}