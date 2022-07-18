package com.scottbarbour.devicelocktimer.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.scottbarbour.devicelocktimer.data.model.TimerState
import com.scottbarbour.devicelocktimer.data.model.TimerWarningStatus

@Composable
fun DeviceLockTimer(state: TimerState) {
    Column(modifier = Modifier
        .background(color = state.timerWarningStatus.timerBackgroundColour)
        .padding(15.dp)
    ) {
        Text(
            text = state.friendlyTimeRemaining,
            style = MaterialTheme.typography.body1
        )
    }
}

@Preview
@Composable
fun DeviceLockTimerPreview() {
    DeviceLockTimer(state = TimerState("00:00:00", TimerWarningStatus.LOCKED))
}