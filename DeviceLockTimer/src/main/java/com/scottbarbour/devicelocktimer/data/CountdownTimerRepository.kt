package com.scottbarbour.devicelocktimer.data

import com.scottbarbour.devicelocktimer.data.model.CountryIsoCode
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

    suspend fun getTimeUntilDeviceLocks(): Flow<String> = flow {
        while (true) {
            val deviceLockTime = getTimeUntilDeviceLock().truncatedTo(ChronoUnit.SECONDS)
            val currentDeviceTime =
                deviceTimeDataSource.getCurrentDeviceTime().truncatedTo(ChronoUnit.SECONDS)

            emit(getFriendlyTimeRemaining(deviceLockTime, currentDeviceTime))
        }
    }

    private fun getFriendlyTimeRemaining(
        deviceLockTime: OffsetDateTime,
        currentDeviceTime: OffsetDateTime
    ): String {

        val timeRemaining = currentDeviceTime.until(deviceLockTime, ChronoUnit.SECONDS)

        return OffsetDateTime.of(
            LocalDateTime.ofEpochSecond(timeRemaining, 0, ZoneOffset.UTC),
            ZoneOffset.UTC
        ).format(timerFormatter)
    }
}