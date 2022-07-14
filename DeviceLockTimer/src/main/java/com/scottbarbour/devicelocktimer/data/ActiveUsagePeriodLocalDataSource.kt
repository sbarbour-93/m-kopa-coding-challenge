package com.scottbarbour.devicelocktimer.data

import com.scottbarbour.devicelocktimer.data.model.ActiveUsagePeriod
import kotlinx.coroutines.delay

class ActiveUsagePeriodLocalDataSource {

    private var lockingInfo: ActiveUsagePeriod? = null

    suspend fun getLockingInfo(): ActiveUsagePeriod? {
        delay(500) // simulate DB operation
        return lockingInfo
    }

    fun storeLockingInfoLocally(lockingInfo: ActiveUsagePeriod) {
        this.lockingInfo = lockingInfo
    }
}
