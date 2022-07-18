package com.scottbarbour.mkopa

import android.app.Application
import com.scottbarbour.devicelocktimer.di.timerModules
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MKopaApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@MKopaApp)
            modules(timerModules)
        }
    }
}