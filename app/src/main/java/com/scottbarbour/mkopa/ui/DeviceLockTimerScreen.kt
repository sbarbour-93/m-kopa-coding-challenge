package com.scottbarbour.mkopa.ui

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.scottbarbour.devicelocktimer.ui.DeviceLockTimer
import com.scottbarbour.devicelocktimer.ui.DeviceLockTimerViewModel
import org.koin.androidx.compose.getViewModel

@Composable
fun DeviceLockTimerScreen() {

    val viewModel = getViewModel<DeviceLockTimerViewModel>()
    val timerState = viewModel.timerState.observeAsState()
    viewModel.startTimer()

    Column(
        Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        Row(
            modifier =
            Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            timerState.value?.let {
                DeviceLockTimer(it)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DeviceLockTimerScreenPreview() {
    DeviceLockTimerScreen()
}