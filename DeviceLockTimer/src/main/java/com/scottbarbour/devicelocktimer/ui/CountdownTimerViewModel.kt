package com.scottbarbour.devicelocktimer.ui

import androidx.lifecycle.ViewModel
import com.scottbarbour.devicelocktimer.data.CountdownTimerRepository

class CountdownTimerViewModel(private val repository: CountdownTimerRepository) : ViewModel() {}