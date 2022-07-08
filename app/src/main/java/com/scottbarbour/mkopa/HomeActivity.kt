package com.scottbarbour.mkopa

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.scottbarbour.devicelocktimer.TimerActivity

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        navigateToTimerActivity()
    }

    private fun navigateToTimerActivity() {
        val timerIntent = Intent(this, TimerActivity::class.java)
        startActivity(timerIntent)
    }

}