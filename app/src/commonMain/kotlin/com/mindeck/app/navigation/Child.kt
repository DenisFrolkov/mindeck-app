package com.mindeck.app.navigation

import com.mindeck.feature.card.CreateCardViewModel
import com.mindeck.feature.home.HomeViewModel

sealed interface Child {
    data class Home(
        val viewModel: HomeViewModel,
    ) : Child

    data class CreateCard(
        val viewModel: CreateCardViewModel,
    ) : Child
}
