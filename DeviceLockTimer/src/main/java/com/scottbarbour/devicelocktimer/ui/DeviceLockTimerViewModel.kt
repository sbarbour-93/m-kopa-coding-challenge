package com.scottbarbour.devicelocktimer.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.scottbarbour.devicelocktimer.data.DeviceLockTimerRepository
import com.scottbarbour.devicelocktimer.data.model.TimerState
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.collect

class DeviceLockTimerViewModel(
    private val repository: DeviceLockTimerRepository
) : ViewModel() {

    private val _timerState = MutableLiveData<TimerState>()
    val timerState = _timerState

    fun startTimer() {
        viewModelScope.launch {
            repository.getTimerState().collect { friendlyTime ->
                _timerState.postValue(friendlyTime)
            }
        }
    }
}