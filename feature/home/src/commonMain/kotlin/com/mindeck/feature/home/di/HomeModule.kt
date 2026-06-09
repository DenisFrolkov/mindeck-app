package com.mindeck.feature.home.di

import com.mindeck.feature.home.home.HomeViewModel
import org.koin.dsl.module

val homeModule = module {
    factory { HomeViewModel(get()) }
}