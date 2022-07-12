package com.scottbarbour.devicelocktimer.di

import com.scottbarbour.devicelocktimer.data.ActiveUsagePeriodDataSource
import com.scottbarbour.devicelocktimer.data.CountdownTimerRepository
import com.scottbarbour.devicelocktimer.data.CountryIsoCodeDataSource
import com.scottbarbour.devicelocktimer.data.DeviceTimeDataSource
import com.scottbarbour.devicelocktimer.ui.CountdownTimerViewModel
import com.scottbarbour.devicelocktimer.util.TIMER_FORMATTER_PATTERN
import kotlinx.coroutines.Dispatchers
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import java.time.format.DateTimeFormatter

val timerModules = module {
    viewModel { CountdownTimerViewModel(get(), Dispatchers.IO) }
    factory { CountdownTimerRepository(get(), get(), get(), get()) }
    factory { ActiveUsagePeriodDataSource() }
    factory { CountryIsoCodeDataSource() }
    factory { DeviceTimeDataSource() }

    single { DateTimeFormatter.ofPattern(TIMER_FORMATTER_PATTERN) }
}