package com.scottbarbour.devicelocktimer.data

import com.scottbarbour.devicelocktimer.data.model.CountryIsoCode
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

class CountdownTimerRepository(
    private val activeUsagePeriodDataSource: ActiveUsagePeriodDataSource,
    private val countryIsoCodeDataSource: CountryIsoCodeDataSource,
    private val deviceTimeDataSource: DeviceTimeDataSource,
    private val timerFormatter: DateTimeFormatter
) {

    private suspend fun getTimeUntilDeviceLock(): OffsetDateTime {
        return activeUsagePeriodDataSource.getLockingInfo().lockTime
    }

    private suspend fun getCountryIsoCode(): CountryIsoCode {
        return countryIsoCodeDataSource.getCountryIsoCode()
    }

    suspend fun getTimeUntilDeviceLocks(): String {
        val deviceLockTime = getTimeUntilDeviceLock()
        val currentDeviceTime = deviceTimeDataSource.getCurrentDeviceTime()
        val timeRemaining = currentDeviceTime.until(deviceLockTime, ChronoUnit.SECONDS)


        return getFriendlyTimeRemaining(timeRemaining)
    }

    private fun getFriendlyTimeRemaining(timeRemaining: Long) = OffsetDateTime.of(
        LocalDateTime.ofEpochSecond(timeRemaining, 0, ZoneOffset.UTC),
        ZoneOffset.UTC
    ).format(timerFormatter)
}