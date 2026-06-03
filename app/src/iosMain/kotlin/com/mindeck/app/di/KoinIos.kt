package com.mindeck.app.di

import com.mindeck.data.di.commonDataModule
import com.mindeck.data.di.dataModule
import org.koin.core.context.startKoin

fun initKoin() {
    startKoin { modules(dataModule, commonDataModule) }
}
