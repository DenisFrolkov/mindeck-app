package com.mindeck.feature.card.di

import com.mindeck.feature.card.createCard.CreateCardState
import com.mindeck.feature.card.createCard.CreateCardViewModel
import org.koin.dsl.module

val cardModule =
    module {
        factory { params ->
            CreateCardViewModel(
                get(),
                get(),
                get(),
                get(),
                get(),
                initialState = params.getOrNull<CreateCardState>() ?: CreateCardState(),
            )
        }
    }
