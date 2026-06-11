package com.mindeck.feature.card.di

import com.mindeck.feature.card.CreateCardViewModel
import org.koin.dsl.module

val cardModule =
    module {
        factory { CreateCardViewModel() }
    }
