package com.mindeck.app.navigation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.DelicateDecomposeApi
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.lifecycle.doOnDestroy
import com.mindeck.feature.home.home.HomeViewModel
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

class RootComponent(
    componentContext: ComponentContext
) : ComponentContext by componentContext, KoinComponent {
    private val navigation = StackNavigation<Config>()

    val stack: Value<ChildStack<Config, Child>> =
        childStack(
            source = navigation,
            serializer = Config.serializer(),
            initialConfiguration = Config.Main,
            handleBackButton = true,
            childFactory = ::createChild,
        )

    @OptIn(DelicateDecomposeApi::class)
    fun push(config: Config) = navigation.push(config)

    fun pop() = navigation.pop()

    private fun createChild(
        config: Config,
        context: ComponentContext,
    ): Child =
        when (config) {
            is Config.Main -> {
                val viewModel = get<HomeViewModel>()
                context.lifecycle.doOnDestroy(viewModel::clear)
                Child.Main(viewModel)
            }
            is Config.Second -> Child.Second
        }
}
