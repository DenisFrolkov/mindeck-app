package com.mindeck.app.navigation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.pushNew
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.instancekeeper.getOrCreate
import com.mindeck.feature.card.createCard.CreateCardViewModel
import com.mindeck.feature.home.HomeViewModel
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

class RootComponent(
    componentContext: ComponentContext,
) : ComponentContext by componentContext,
    KoinComponent {
    private val navigation = StackNavigation<Config>()

    val stack: Value<ChildStack<Config, Child>> =
        childStack(
            source = navigation,
            serializer = Config.serializer(),
            initialConfiguration = Config.Home,
            handleBackButton = true,
            childFactory = ::createChild,
        )

    fun push(config: Config) = navigation.pushNew(config)

    fun pop() = navigation.pop()

    private fun createChild(
        config: Config,
        context: ComponentContext,
    ): Child =
        when (config) {
            is Config.Home -> {
                val viewModel = context.instanceKeeper.getOrCreate { get<HomeViewModel>() }
                Child.Home(viewModel)
            }
            is Config.CreateCard -> {
                val viewModel = context.instanceKeeper.getOrCreate { get<CreateCardViewModel>() }
                Child.CreateCard(viewModel)
            }
        }
}
