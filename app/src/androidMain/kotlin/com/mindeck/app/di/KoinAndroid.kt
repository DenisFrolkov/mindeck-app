package com.mindeck.app.di

import android.content.Context
import com.mindeck.data.di.commonDataModule
import com.mindeck.data.di.dataModule
import com.mindeck.feature.card.di.cardModule
import com.mindeck.feature.home.di.homeModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

fun initKoin(context: Context) {
    startKoin {
        androidContext(context)
        modules(dataModule, commonDataModule, homeModule, cardModule)
    }
}
