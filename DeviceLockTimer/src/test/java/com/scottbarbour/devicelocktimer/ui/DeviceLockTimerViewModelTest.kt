package com.scottbarbour.devicelocktimer.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.scottbarbour.devicelocktimer.data.DeviceLockTimerRepository
import com.scottbarbour.devicelocktimer.data.model.TimerState
import com.scottbarbour.devicelocktimer.data.model.TimerWarningStatus
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import kotlin.test.assertEquals


@ExperimentalCoroutinesApi
class DeviceLockTimerViewModelTest {

    @get:Rule
    val rule: TestRule = InstantTaskExecutorRule()

    @MockK
    private lateinit var repository: DeviceLockTimerRepository

    private lateinit var viewModel: DeviceLockTimerViewModel

    private val expectedTimerState = TimerState(
        "05:00:00",
        TimerWarningStatus.HEALTHY
    )

    @Before
    fun setup() {
        Dispatchers.setMain(StandardTestDispatcher())

        MockKAnnotations.init(this, relaxed = true)
        coEvery { repository.getTimerState() } returns flowOf(expectedTimerState)

        viewModel = DeviceLockTimerViewModel(repository)
    }

    @Test
    fun `start timer method calls data layer and posts result to live data property`() = runTest {
        viewModel.startTimer()
        advanceUntilIdle()
        assertEquals(expectedTimerState, viewModel.timerState.value)
    }

}