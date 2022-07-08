package com.scottbarbour.devicelocktimer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.scottbarbour.devicelocktimer.ui.CountdownTimerFragment

class TimerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timer)
    }
}