package com.scottbarbour.devicelocktimer.data

import com.scottbarbour.devicelocktimer.data.model.ActiveUsagePeriod
import kotlinx.coroutines.delay
import java.time.OffsetDateTime

class DeviceTimeDataSource {

    fun getCurrentDeviceTime() : OffsetDateTime {
        return OffsetDateTime.now()
    }

    // TODO: look into this or getting the system time without manual configuration to cheat the system
    suspend fun getCurrentDateTime(): ActiveUsagePeriod {
        delay(500) // simulate network call instead of relying on device time
        return ActiveUsagePeriod(
            OffsetDateTime.now()
        )
    }
}
