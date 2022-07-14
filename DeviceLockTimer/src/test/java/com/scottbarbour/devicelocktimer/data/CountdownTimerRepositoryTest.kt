package com.scottbarbour.devicelocktimer.data

import com.scottbarbour.devicelocktimer.data.model.ActiveUsagePeriod
import com.scottbarbour.devicelocktimer.util.TIMER_FORMATTER_PATTERN
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class CountdownTimerRepositoryTest {

    @MockK
    private lateinit var activeUsagePeriodRemoteDataSource: ActiveUsagePeriodRemoteDataSource

    @MockK
    private lateinit var activeUsagePeriodLocalDataSource: ActiveUsagePeriodLocalDataSource

    @MockK
    private lateinit var countryIsoCodeDataSource: CountryIsoCodeDataSource

    @MockK
    private lateinit var deviceTimeDataSource: DeviceTimeDataSource

    private val timerFormatter = DateTimeFormatter.ofPattern(TIMER_FORMATTER_PATTERN)

    private lateinit var repository: CountdownTimerRepository

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxed = true)
        repository = CountdownTimerRepository(
            activeUsagePeriodRemoteDataSource,
            activeUsagePeriodLocalDataSource,
            countryIsoCodeDataSource,
            deviceTimeDataSource,
            timerFormatter,
            testDispatcher
        )
    }

    @Test
    fun `repository obtains data from remote source when not already cached`() =
        runTest(testDispatcher) {
            coEvery { activeUsagePeriodLocalDataSource.getLockingInfo() } returns null
            justRun { activeUsagePeriodLocalDataSource.storeLockingInfoLocally(any()) }
            coEvery { activeUsagePeriodRemoteDataSource.getLockingInfo() } returns mockk(relaxed = true)
            every { deviceTimeDataSource.getCurrentDeviceTime() } returns mockk(relaxed = true)

            repository.getTimeUntilDeviceLocks()

            coVerify(exactly = 1) { activeUsagePeriodRemoteDataSource.getLockingInfo() }
            coVerify(exactly = 1) { activeUsagePeriodLocalDataSource.storeLockingInfoLocally(any()) }
        }

    @Test
    fun `repository obtains data from local source when already cached`() =
        runTest(testDispatcher) {
            coEvery { activeUsagePeriodLocalDataSource.getLockingInfo() } returns mockk(relaxed = true)
            justRun { activeUsagePeriodLocalDataSource.storeLockingInfoLocally(any()) }
            every { deviceTimeDataSource.getCurrentDeviceTime() } returns mockk(relaxed = true)

            repository.getTimeUntilDeviceLocks()

            coVerify(inverse = true) { activeUsagePeriodRemoteDataSource.getLockingInfo() }
            coVerify(inverse = true) { activeUsagePeriodLocalDataSource.storeLockingInfoLocally(any()) }
        }

    @Test
    fun `formatting of time until device lock time is displayed in a friendly format for the UI layer`() =
        runTest(testDispatcher) {
            val mockDeviceLockTime = OffsetDateTime.now().withHour(23).withMinute(0).withSecond(0)
            val mockLockingInfo = mockk<ActiveUsagePeriod>()
            every { mockLockingInfo.lockTime } returns mockDeviceLockTime

            val mockCurrentDeviceTime = OffsetDateTime.now().withHour(18).withMinute(0).withSecond(0)
            every { deviceTimeDataSource.getCurrentDeviceTime() } returns mockCurrentDeviceTime

            val expectedResult = "05:00:00"

            coEvery { activeUsagePeriodLocalDataSource.getLockingInfo() } returns mockLockingInfo

            val actualResult = repository.getTimeUntilDeviceLocks()

            assertEquals(expectedResult, actualResult)
        }

}