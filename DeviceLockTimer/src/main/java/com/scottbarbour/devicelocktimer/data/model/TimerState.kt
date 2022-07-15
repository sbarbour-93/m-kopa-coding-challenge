package com.scottbarbour.devicelocktimer.data.model

data class TimerState(
    val friendlyTimeRemaining: String,
    val timerWarningStatus: TimerWarningStatus
)
