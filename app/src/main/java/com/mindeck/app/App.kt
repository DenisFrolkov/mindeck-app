package com.mindeck.app

import android.app.Application
import com.mindeck.data.di.commonDataModule
import com.mindeck.data.di.dataModule
import com.mindeck.presentation.di.presentationModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(dataModule, commonDataModule, presentationModule)
        }
    }
}
