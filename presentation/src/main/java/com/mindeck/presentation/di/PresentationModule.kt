package com.mindeck.presentation.di

import com.mindeck.presentation.viewmodel.card.CardStudyViewModel
import com.mindeck.presentation.viewmodel.card.CardViewModel
import com.mindeck.presentation.viewmodel.card.CreationCardViewModel
import com.mindeck.presentation.viewmodel.deck.DeckViewModel
import com.mindeck.presentation.viewmodel.deck.DecksViewModel
import com.mindeck.presentation.viewmodel.main.MainViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val presentationModule =
    module {
        viewModel { MainViewModel(get(), get()) }
        viewModel { DecksViewModel(get()) }
        viewModel { DeckViewModel(get(), get(), get(), get()) }
        viewModel { CardStudyViewModel(get(), get(), get()) }
        viewModel { CardViewModel(get(), get()) }
        viewModel { CreationCardViewModel(get(), get(), get()) }
    }
