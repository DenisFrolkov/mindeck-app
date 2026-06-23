package com.mindeck.feature.card.di

import com.mindeck.feature.card.createCard.CreateCardViewModel
import org.koin.dsl.module

val cardModule =
    module {
        factory { CreateCardViewModel(get(), get()) }
    }
