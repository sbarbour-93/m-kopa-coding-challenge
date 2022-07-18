package com.scottbarbour.devicelocktimer

import androidx.compose.material.MaterialTheme
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.scottbarbour.devicelocktimer.data.model.TimerState
import com.scottbarbour.devicelocktimer.data.model.TimerWarningStatus
import com.scottbarbour.devicelocktimer.ui.DeviceLockTimer
import org.junit.Rule
import org.junit.Test

class DeviceLockTimerTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun deviceLockTimerWithHealthyStatus() {

        val friendlyTimeRemaining = "08:00:00"

        val mockHealthyStatus = TimerState(friendlyTimeRemaining, TimerWarningStatus.HEALTHY)
        composeTestRule.setContent {
            MaterialTheme {
                DeviceLockTimer(state = mockHealthyStatus)
            }
        }

        composeTestRule.onNodeWithText(friendlyTimeRemaining).assertIsDisplayed()
    }
}