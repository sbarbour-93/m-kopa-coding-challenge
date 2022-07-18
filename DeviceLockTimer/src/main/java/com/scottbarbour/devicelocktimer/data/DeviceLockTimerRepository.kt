package com.scottbarbour.devicelocktimer.data

import com.scottbarbour.devicelocktimer.data.model.CountryIsoCode
import com.scottbarbour.devicelocktimer.data.model.TimerState
import com.scottbarbour.devicelocktimer.data.model.TimerWarningStatus
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

class DeviceLockTimerRepository(
    private val activeUsagePeriodRemoteDataSource: ActiveUsagePeriodRemoteDataSource,
    private val activeUsagePeriodLocalDataSource: ActiveUsagePeriodLocalDataSource,
    private val countryIsoCodeDataSource: CountryIsoCodeDataSource,
    private val deviceTimeDataSource: DeviceTimeDataSource,
    private val timerFormatter: DateTimeFormatter,
    private val dispatcher: CoroutineDispatcher
) {

    private suspend fun getTimeUntilDeviceLock(): OffsetDateTime = withContext(dispatcher) {
        return@withContext if (activeUsagePeriodLocalDataSource.getLockingInfo() != null) {
            println("Fetching lock time from local data source")
            activeUsagePeriodLocalDataSource.getLockingInfo()!!.lockTime
        } else {
            println("Fetching lock time from remote data source")
            val lockingInfo = activeUsagePeriodRemoteDataSource.getLockingInfo()
            activeUsagePeriodLocalDataSource.storeLockingInfoLocally(lockingInfo)
            lockingInfo.lockTime
        }
    }

    private suspend fun getCountryIsoCode(): CountryIsoCode {
        return countryIsoCodeDataSource.getCountryIsoCode()
    }

    suspend fun getTimerState(): Flow<TimerState> = flow {
        while (true) {
            val deviceLockTime = getTimeUntilDeviceLock().truncatedTo(ChronoUnit.SECONDS)
            val currentDeviceTime =
                deviceTimeDataSource.getCurrentDeviceTime().truncatedTo(ChronoUnit.SECONDS)

            val countryIsoCode = getCountryIsoCode()
            val warningTimeForCountry =
                determineWarningTimeForCountry(countryIsoCode, deviceLockTime)

            val timerWarningStatus =
                getTimerWarningStatus(deviceLockTime, currentDeviceTime, warningTimeForCountry)

            val friendlyTimeRemaining = getFriendlyTimeRemaining(deviceLockTime, currentDeviceTime)

            emit(TimerState(friendlyTimeRemaining, timerWarningStatus))
        }
    }

    private fun determineWarningTimeForCountry(
        countryIsoCode: CountryIsoCode,
        deviceLockTime: OffsetDateTime
    ): OffsetDateTime {
        return when (countryIsoCode) {
            CountryIsoCode.UG -> deviceLockTime.minusHours(3L)
            CountryIsoCode.KE -> deviceLockTime.minusHours(2L)
        }
    }

    private fun getTimerWarningStatus(
        deviceLockTime: OffsetDateTime,
        currentDeviceTime: OffsetDateTime,
        warningTimeForCountry: OffsetDateTime
    ): TimerWarningStatus {
        return if (currentDeviceTime.isBefore(warningTimeForCountry)) {
            TimerWarningStatus.HEALTHY
        } else if (currentDeviceTime.isAfter(deviceLockTime)) {
            TimerWarningStatus.LOCKED
        } else {
            TimerWarningStatus.WARNING
        }
    }

    private fun getFriendlyTimeRemaining(
        deviceLockTime: OffsetDateTime,
        currentDeviceTime: OffsetDateTime
    ): String {
        val timeRemaining = currentDeviceTime.until(deviceLockTime, ChronoUnit.SECONDS)

        return if (timeRemaining < 0L)
            "00:00:00"
        else
            OffsetDateTime.of(
                LocalDateTime.ofEpochSecond(timeRemaining, 0, ZoneOffset.UTC),
                ZoneOffset.UTC
            ).format(timerFormatter)
    }

}