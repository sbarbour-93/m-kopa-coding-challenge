package com.scottbarbour.devicelocktimer.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.scottbarbour.devicelocktimer.data.model.TimerState
import com.scottbarbour.devicelocktimer.data.model.TimerWarningStatus

@Composable
fun DeviceLockTimer(state: TimerState) {
    Column(
        modifier = Modifier
            .background(color = state.timerWarningStatus.timerBackgroundColour)
            .padding(15.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = state.friendlyTimeRemaining,
            style = MaterialTheme.typography.body1,
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp
        )
        if (state.timerWarningStatus.message.isNotBlank()) {
            Text(
                text = state.timerWarningStatus.message,
                style = MaterialTheme.typography.body2,
                fontStyle = FontStyle.Italic,
                fontSize = 12.sp
            )
        }
    }
}

@Preview
@Composable
fun DeviceLockTimerPreview() {
    DeviceLockTimer(state = TimerState("00:00:00", TimerWarningStatus.LOCKED))
}