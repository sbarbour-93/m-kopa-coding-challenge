package com.scottbarbour.devicelocktimer

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.scottbarbour.devicelocktimer.di.timerModules
import org.koin.core.context.startKoin

class TimerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startKoin {
            modules(timerModules)
        }
    }
}