package com.scottbarbour.devicelocktimer.data

import com.scottbarbour.devicelocktimer.data.model.ActiveUsagePeriod
import kotlinx.coroutines.delay
import java.time.OffsetDateTime

class ActiveUsagePeriodDataSource {
    suspend fun getLockingInfo(): ActiveUsagePeriod {
        delay(500)
        return ActiveUsagePeriod( // TODO: cache this value so not to keep spamming the network
            OffsetDateTime.now().withHour(23).withMinute(0).withSecond(0)
        )
    }
}