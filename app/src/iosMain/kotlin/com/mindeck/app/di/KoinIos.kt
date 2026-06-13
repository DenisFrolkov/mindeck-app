package com.mindeck.app.di

import com.mindeck.data.di.commonDataModule
import com.mindeck.data.di.dataModule
import com.mindeck.feature.card.di.cardModule
import com.mindeck.feature.home.di.homeModule
import org.koin.core.context.startKoin

fun initKoin() {
    startKoin {
        modules(dataModule, commonDataModule, homeModule, cardModule)
    }
}
