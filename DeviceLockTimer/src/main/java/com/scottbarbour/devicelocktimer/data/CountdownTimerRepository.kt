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

class CountdownTimerRepository(
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
            val timeRemaining = currentDeviceTime.until(deviceLockTime, ChronoUnit.SECONDS)
            val countryIsoCode = getCountryIsoCode()

            val timerStatus = getTimerStatus(countryIsoCode, timeRemaining)
            val friendlyTimeRemaining = getFriendlyTimeRemaining(timeRemaining)

            emit(TimerState(friendlyTimeRemaining, timerStatus))
        }
    }

    private fun getTimerStatus(
        countryIsoCode: CountryIsoCode,
        timeRemaining: Long
    ): TimerWarningStatus {

        if (timeRemaining < 0L)
            return TimerWarningStatus.LOCKED

        val hoursUntilLocked = OffsetDateTime.of(
            LocalDateTime.ofEpochSecond(timeRemaining, 0, ZoneOffset.UTC),
            ZoneOffset.UTC
        ).truncatedTo(ChronoUnit.HOURS)

        var status =
            if (timeRemaining == 0L) TimerWarningStatus.LOCKED else TimerWarningStatus.HEALTHY

        // TODO: could tidy this up and also re-write logic so that they are warned on the hour too
        when (countryIsoCode) {
            CountryIsoCode.UG -> {
                if (hoursUntilLocked.hour < 3)
                    status = TimerWarningStatus.WARNING
            }

            CountryIsoCode.KE -> {
                if (hoursUntilLocked.hour < 2)
                    status = TimerWarningStatus.WARNING
            }
        }

        return status
    }

    private fun getFriendlyTimeRemaining(
        timeRemaining: Long
    ): String {
        return if (timeRemaining < 0L)
            "00:00:00"
        else
            OffsetDateTime.of(
                LocalDateTime.ofEpochSecond(timeRemaining, 0, ZoneOffset.UTC),
                ZoneOffset.UTC
            ).format(timerFormatter)
    }

}