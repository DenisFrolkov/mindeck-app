package com.mindeck.app

import android.app.Application
import com.mindeck.app.di.initKoin

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin(this)
    }
}
