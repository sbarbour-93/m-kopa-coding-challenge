package com.scottbarbour.devicelocktimer.di

import org.junit.Test
import org.koin.test.KoinTest
import org.koin.test.check.checkModules

class CheckKoinModules : KoinTest {
    @Test
    fun checkAllModules() = checkModules {
        timerModules
    }
}