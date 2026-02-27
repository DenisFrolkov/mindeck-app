package com.mindeck.presentation.ui.navigation

interface Navigator {
    fun push(route: NavigationRoute)
    fun pop()
}

class StackNavigator(
    private val backStack: MutableList<NavigationRoute>,
) : Navigator {
    override fun push(route: NavigationRoute) {
        backStack.add(route)
    }

    override fun pop() {
        if (backStack.size > 1) {
            backStack.removeLastOrNull()
        }
    }
}
