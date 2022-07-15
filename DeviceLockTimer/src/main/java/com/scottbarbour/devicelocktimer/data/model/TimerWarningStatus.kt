package com.scottbarbour.devicelocktimer.data.model

import androidx.annotation.ColorRes

enum class TimerWarningStatus(@ColorRes val timerBackgroundColour: Int) {
    HEALTHY(android.R.color.holo_green_dark),
    WARNING(android.R.color.holo_orange_dark),
    LOCKED(android.R.color.holo_red_dark)
}
