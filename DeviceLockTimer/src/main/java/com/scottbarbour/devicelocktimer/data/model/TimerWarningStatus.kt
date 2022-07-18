package com.scottbarbour.devicelocktimer.data.model

import androidx.compose.ui.graphics.Color

enum class TimerWarningStatus(val timerBackgroundColour: Color, val message: String = "") {
    HEALTHY(Color.Green),
    WARNING(Color.Yellow, "Payment Due Soon"),
    LOCKED(Color.Red, "Device Locked");
}
