package com.scottbarbour.devicelocktimer.di

import com.scottbarbour.devicelocktimer.data.*
import com.scottbarbour.devicelocktimer.ui.CountdownTimerViewModel
import com.scottbarbour.devicelocktimer.util.TIMER_FORMATTER_PATTERN
import kotlinx.coroutines.Dispatchers
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import java.time.format.DateTimeFormatter

val timerModules = module {
    viewModel { CountdownTimerViewModel(get()) }
    factory { CountdownTimerRepository(get(), get(), get(), get(), get(), Dispatchers.IO) }
    factory { ActiveUsagePeriodRemoteDataSource() }
    factory { ActiveUsagePeriodLocalDataSource() }
    factory { CountryIsoCodeDataSource() }
    factory { DeviceTimeDataSource() }

    single { DateTimeFormatter.ofPattern(TIMER_FORMATTER_PATTERN) }
}