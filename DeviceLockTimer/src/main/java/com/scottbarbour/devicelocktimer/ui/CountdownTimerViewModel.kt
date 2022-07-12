package com.scottbarbour.devicelocktimer.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.scottbarbour.devicelocktimer.data.CountdownTimerRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch

class CountdownTimerViewModel(
    private val repository: CountdownTimerRepository,
    private val dispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _timeRemainingUntilDeviceLocks = MutableLiveData<String>() // TODO: give result class
    val timeRemainingUntilDeviceLocks = _timeRemainingUntilDeviceLocks

    fun setupTimer() {
        viewModelScope.launch(dispatcher) {
            val result = repository.getTimeUntilDeviceLocks()
            _timeRemainingUntilDeviceLocks.postValue(result)
        }
    }
}