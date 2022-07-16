package com.scottbarbour.devicelocktimer

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.MutableLiveData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.idling.CountingIdlingResource
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.scottbarbour.devicelocktimer.data.model.TimerState
import com.scottbarbour.devicelocktimer.data.model.TimerWarningStatus
import com.scottbarbour.devicelocktimer.ui.CountdownTimerFragment
import com.scottbarbour.devicelocktimer.ui.CountdownTimerViewModel
import com.scottbarbour.devicelocktimer.util.matchers.HasBackgroundColour.Companion.withBackgroundColour
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import org.hamcrest.Matchers.allOf
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module

@RunWith(AndroidJUnit4::class)
class CountdownTimerFragmentTest {

    private var idlingResource: CountingIdlingResource? = null
    private lateinit var fakeViewModel: CountdownTimerViewModel

    @Before
    fun setup() {

        fakeViewModel = mockk(relaxed = true)
        justRun { fakeViewModel.startTimer() }

        startKoin {
            modules(
                module {
                    viewModel { fakeViewModel }
                }
            )
        }
    }

    @After
    fun tearDown() {
        stopKoin()
        idlingResource?.let {
            IdlingRegistry.getInstance().unregister(idlingResource)
        }
    }

    @Test
    fun healthyTimer() {
        every { fakeViewModel.timerState } returns MutableLiveData(
            TimerState(
                "09:00:00",
                TimerWarningStatus.HEALTHY
            )
        )

        launchFragmentInContainer<CountdownTimerFragment>()

        onView(withId(R.id.message))
            .check(
                matches(
                    allOf(
                        isDisplayed(),
                        withText("09:00:00")
                    )
                )
            )

        onView(withId(R.id.countdown_timer_container))
            .check(
                matches(
                    withBackgroundColour(android.R.color.holo_green_dark)
                )
            )
    }

    @Test
    fun warningTimer() {
        every { fakeViewModel.timerState } returns MutableLiveData(
            TimerState(
                "01:00:00",
                TimerWarningStatus.WARNING
            )
        )

        launchFragmentInContainer<CountdownTimerFragment>()

        onView(withId(R.id.message))
            .check(
                matches(
                    allOf(
                        isDisplayed(),
                        withText("01:00:00")
                    )
                )
            )

        onView(withId(R.id.countdown_timer_container))
            .check(
                matches(
                    withBackgroundColour(android.R.color.holo_orange_dark)
                )
            )
    }

    @Test
    fun lockedTimer() {
        every { fakeViewModel.timerState } returns MutableLiveData(
            TimerState(
                "00:00:00",
                TimerWarningStatus.LOCKED
            )
        )

        launchFragmentInContainer<CountdownTimerFragment>()

        onView(withId(R.id.message))
            .check(
                matches(
                    allOf(
                        isDisplayed(),
                        withText("00:00:00")
                    )
                )
            )

        onView(withId(R.id.countdown_timer_container))
            .check(
                matches(
                    withBackgroundColour(android.R.color.holo_red_dark)
                )
            )
    }
}