package com.scottbarbour.devicelocktimer.di

import com.scottbarbour.devicelocktimer.data.ActiveUsagePeriodDataSource
import com.scottbarbour.devicelocktimer.data.CountdownTimerRepository
import com.scottbarbour.devicelocktimer.ui.CountdownTimerViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val timerModules = module {
    viewModel { CountdownTimerViewModel(get()) }
    factory { CountdownTimerRepository(get()) }
    factory { ActiveUsagePeriodDataSource() }
}