package com.mobiledrivetech.external.sample

import android.app.Application
import com.mobiledrivetech.external.sample.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class BaseApplication : Application() {

    override fun onCreate() {
        initKoin()
        super.onCreate()
    }

    private fun initKoin() {
        startKoin {
            androidContext(this@BaseApplication)
            modules(listOf(appModule))
        }
    }
}
