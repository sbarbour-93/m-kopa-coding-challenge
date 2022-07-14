package com.scottbarbour.devicelocktimer.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.scottbarbour.devicelocktimer.data.CountdownTimerRepository
import kotlinx.coroutines.launch

class CountdownTimerViewModel(
    private val repository: CountdownTimerRepository
) : ViewModel() {

    private val _timeRemainingUntilDeviceLocks = MutableLiveData<String>() // TODO: give result class
    val timeRemainingUntilDeviceLocks = _timeRemainingUntilDeviceLocks

    fun startTimer() {
        viewModelScope.launch {
            repository.getTimeUntilDeviceLocks().collect { friendlyTime ->
                _timeRemainingUntilDeviceLocks.postValue(friendlyTime)
            }
        }
    }
}