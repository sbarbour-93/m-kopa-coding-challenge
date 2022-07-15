package com.scottbarbour.devicelocktimer.data

import com.scottbarbour.devicelocktimer.data.model.ActiveUsagePeriod
import kotlinx.coroutines.delay
import java.time.OffsetDateTime

class ActiveUsagePeriodRemoteDataSource {
    suspend fun getLockingInfo(): ActiveUsagePeriod {
        delay(500)
        return ActiveUsagePeriod(
            OffsetDateTime.now().withHour(23).withMinute(0).withSecond(0)
        )
    }
}