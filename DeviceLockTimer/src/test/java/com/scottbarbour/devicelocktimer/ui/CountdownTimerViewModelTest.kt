package com.scottbarbour.devicelocktimer.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.scottbarbour.devicelocktimer.data.CountdownTimerRepository
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
class CountdownTimerViewModelTest {

    @get:Rule
    val rule: TestRule = InstantTaskExecutorRule()

    @MockK
    private lateinit var repository: CountdownTimerRepository

    private lateinit var viewModel: CountdownTimerViewModel

    private val expectedTimerValue = "05:00:00"

    @Before
    fun setup() {
        Dispatchers.setMain(StandardTestDispatcher())

        MockKAnnotations.init(this, relaxed = true)
        coEvery { repository.getTimeUntilDeviceLocks() } returns flowOf(expectedTimerValue)

        viewModel = CountdownTimerViewModel(repository)
    }

    @Test
    fun `start timer method calls data layer and posts result to live data property`() = runTest {
        viewModel.startTimer()
        advanceUntilIdle()
        assertEquals(expectedTimerValue, viewModel.timeRemainingUntilDeviceLocks.value)
    }

}