package com.mindeck.app.navigation

import com.mindeck.feature.home.home.HomeViewModel

sealed interface Child {
    data class Main(
        val viewModel: HomeViewModel,
    ) : Child

    data object Second : Child
}
