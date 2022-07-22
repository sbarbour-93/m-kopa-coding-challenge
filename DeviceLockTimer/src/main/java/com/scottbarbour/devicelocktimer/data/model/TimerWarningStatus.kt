package com.scottbarbour.devicelocktimer.data.model

import androidx.compose.ui.graphics.Color
import com.scottbarbour.devicelocktimer.ui.theme.Green
import com.scottbarbour.devicelocktimer.ui.theme.Orange
import com.scottbarbour.devicelocktimer.ui.theme.Red

enum class TimerWarningStatus(val timerBackgroundColour: Color, val message: String = "") {
    HEALTHY(Green),
    WARNING(Orange, "Until your device will lock. Payment due soon."),
    LOCKED(Red, "Your device is locked. Payment due.");
}
