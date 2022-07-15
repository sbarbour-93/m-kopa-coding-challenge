package com.scottbarbour.devicelocktimer.data

import com.scottbarbour.devicelocktimer.data.model.ActiveUsagePeriod
import com.scottbarbour.devicelocktimer.data.model.CountryIsoCode
import com.scottbarbour.devicelocktimer.data.model.TimerState
import com.scottbarbour.devicelocktimer.data.model.TimerWarningStatus
import com.scottbarbour.devicelocktimer.util.TIMER_FORMATTER_PATTERN
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import java.time.OffsetDateTime
import java.time.ZoneOffset
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

            repository.getTimerState().first()

            coVerify(exactly = 1) { activeUsagePeriodRemoteDataSource.getLockingInfo() }
            coVerify(exactly = 1) { activeUsagePeriodLocalDataSource.storeLockingInfoLocally(any()) }
        }

    @Test
    fun `repository obtains data from local source when already cached`() =
        runTest(testDispatcher) {
            coEvery { activeUsagePeriodLocalDataSource.getLockingInfo() } returns mockk(relaxed = true)
            justRun { activeUsagePeriodLocalDataSource.storeLockingInfoLocally(any()) }
            every { deviceTimeDataSource.getCurrentDeviceTime() } returns mockk(relaxed = true)

            repository.getTimerState().first()

            coVerify(inverse = true) { activeUsagePeriodRemoteDataSource.getLockingInfo() }
            coVerify(inverse = true) { activeUsagePeriodLocalDataSource.storeLockingInfoLocally(any()) }
        }

    @Test
    fun `derive a healthy timer status`() =
        runTest(testDispatcher) {
            val mockDeviceLockTime = OffsetDateTime.of(2022, 7, 16, 23, 0, 0, 0, ZoneOffset.UTC)
            val mockLockingInfo = mockk<ActiveUsagePeriod>()
            every { mockLockingInfo.lockTime } returns mockDeviceLockTime

            val mockCurrentDeviceTime = OffsetDateTime.of(2022, 7, 16, 18, 0, 0, 0, ZoneOffset.UTC)

            every { deviceTimeDataSource.getCurrentDeviceTime() } returns mockCurrentDeviceTime

            val expectedResult = TimerState(
                "05:00:00",
                TimerWarningStatus.HEALTHY
            )

            coEvery { activeUsagePeriodLocalDataSource.getLockingInfo() } returns mockLockingInfo

            val actualResult = repository.getTimerState().first()

            assertEquals(expectedResult, actualResult)
        }

    @Test
    fun `derive a warning timer status for Uganda users`() =
        runTest(testDispatcher) {
            val mockDeviceLockTime = OffsetDateTime.of(2022, 7, 16, 23, 0, 0, 0, ZoneOffset.UTC)
            val mockLockingInfo = mockk<ActiveUsagePeriod>()
            every { mockLockingInfo.lockTime } returns mockDeviceLockTime

            val mockCurrentDeviceTime = OffsetDateTime.of(2022, 7, 16, 21, 0, 0, 0, ZoneOffset.UTC)

            every { deviceTimeDataSource.getCurrentDeviceTime() } returns mockCurrentDeviceTime

            val expectedResult = TimerState(
                "02:00:00",
                TimerWarningStatus.WARNING
            )

            coEvery { activeUsagePeriodLocalDataSource.getLockingInfo() } returns mockLockingInfo

            coEvery { countryIsoCodeDataSource.getCountryIsoCode() } returns CountryIsoCode.UG

            val actualResult = repository.getTimerState().first()

            assertEquals(expectedResult, actualResult)
        }

    @Test
    fun `derive a warning timer status for Kenya users`() =
        runTest(testDispatcher) {
            val mockDeviceLockTime = OffsetDateTime.of(2022, 7, 16, 23, 0, 0, 0, ZoneOffset.UTC)
            val mockLockingInfo = mockk<ActiveUsagePeriod>()
            every { mockLockingInfo.lockTime } returns mockDeviceLockTime

            val mockCurrentDeviceTime = OffsetDateTime.of(2022, 7, 16, 21, 0, 1, 0, ZoneOffset.UTC)

            every { deviceTimeDataSource.getCurrentDeviceTime() } returns mockCurrentDeviceTime

            val expectedResult = TimerState(
                "01:59:59",
                TimerWarningStatus.WARNING
            )

            coEvery { activeUsagePeriodLocalDataSource.getLockingInfo() } returns mockLockingInfo

            coEvery { countryIsoCodeDataSource.getCountryIsoCode() } returns CountryIsoCode.KE

            val actualResult = repository.getTimerState().first()

            assertEquals(expectedResult, actualResult)
        }


    @Test
    fun `derive a locked timer status`() =
        runTest(testDispatcher) {
            val mockDeviceLockTime = OffsetDateTime.of(2022, 7, 16, 23, 0, 0, 0, ZoneOffset.UTC)
            val mockLockingInfo = mockk<ActiveUsagePeriod>()
            every { mockLockingInfo.lockTime } returns mockDeviceLockTime

            val mockCurrentDeviceTime = OffsetDateTime.of(2022, 7, 16, 23, 0, 1, 0, ZoneOffset.UTC)

            every { deviceTimeDataSource.getCurrentDeviceTime() } returns mockCurrentDeviceTime

            val expectedResult = TimerState(
                "00:00:00",
                TimerWarningStatus.LOCKED
            )

            coEvery { activeUsagePeriodLocalDataSource.getLockingInfo() } returns mockLockingInfo

            coEvery { countryIsoCodeDataSource.getCountryIsoCode() } returns CountryIsoCode.UG

            val actualResult = repository.getTimerState().first()

            assertEquals(expectedResult, actualResult)
        }
}