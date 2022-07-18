package com.scottbarbour.devicelocktimer.data.model

import androidx.compose.ui.graphics.Color

enum class TimerWarningStatus(val timerBackgroundColour: Color) {
    HEALTHY(Color.Green),
    WARNING(Color.Yellow),
    LOCKED(Color.Red)
}
