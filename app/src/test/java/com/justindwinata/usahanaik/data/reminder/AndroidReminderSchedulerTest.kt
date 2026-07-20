package com.justindwinata.usahanaik.data.reminder

import com.justindwinata.usahanaik.domain.model.BusinessReminder
import com.justindwinata.usahanaik.domain.model.ReminderFrequency
import com.justindwinata.usahanaik.domain.model.ReminderPermissionState
import com.justindwinata.usahanaik.domain.model.ReminderStatus
import com.justindwinata.usahanaik.domain.model.ReminderType
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class AndroidReminderSchedulerTest {
    @Test
    fun schedulesActiveReminderWhenPermissionGranted() = runTest {
        val enqueuer = FakeReminderWorkEnqueuer()
        val channelManager = FakeReminderChannelManager()
        val scheduler = scheduler(enqueuer = enqueuer, channelManager = channelManager)

        val result = scheduler.schedule(sampleReminder())

        assertEquals(ReminderScheduleStatus.ScheduledSystem, result.status)
        assertTrue(result.isSuccess)
        assertEquals("business_reminder_9", enqueuer.enqueued.single().uniqueWorkName)
        assertTrue(channelManager.created)
    }

    @Test
    fun cancelsPausedReminderInsteadOfScheduling() = runTest {
        val enqueuer = FakeReminderWorkEnqueuer()
        val scheduler = scheduler(enqueuer = enqueuer)

        val result = scheduler.schedule(sampleReminder(status = ReminderStatus.Paused))

        assertEquals(ReminderScheduleStatus.SkippedDisabled, result.status)
        assertEquals(listOf("business_reminder_9"), enqueuer.cancelled)
    }

    @Test
    fun permissionDeniedDoesNotEnqueueWork() = runTest {
        val enqueuer = FakeReminderWorkEnqueuer()
        val scheduler = scheduler(
            enqueuer = enqueuer,
            permissionState = ReminderPermissionState.Denied
        )

        val result = scheduler.schedule(sampleReminder())

        assertEquals(ReminderScheduleStatus.PermissionDenied, result.status)
        assertTrue(enqueuer.enqueued.isEmpty())
    }

    @Test
    fun cancelUsesStableUniqueWorkName() = runTest {
        val enqueuer = FakeReminderWorkEnqueuer()
        val scheduler = scheduler(enqueuer = enqueuer)

        val result = scheduler.cancel(42L)

        assertEquals(ReminderScheduleStatus.Cancelled, result.status)
        assertEquals(listOf("business_reminder_42"), enqueuer.cancelled)
    }

    private fun scheduler(
        enqueuer: FakeReminderWorkEnqueuer = FakeReminderWorkEnqueuer(),
        channelManager: FakeReminderChannelManager = FakeReminderChannelManager(),
        permissionState: ReminderPermissionState = ReminderPermissionState.Granted
    ): AndroidReminderScheduler {
        return AndroidReminderScheduler(
            notificationManager = channelManager,
            permissionHelper = FakePermissionHelper(permissionState),
            workEnqueuer = enqueuer,
            schedulePlanner = ReminderSchedulePlanner {
                java.time.LocalDateTime.of(2026, 7, 21, 10, 0)
            }
        )
    }

    private fun sampleReminder(status: ReminderStatus = ReminderStatus.Active): BusinessReminder {
        return BusinessReminder(
            id = 9L,
            title = "Record sales",
            description = "Record today's business activity.",
            type = ReminderType.DailyFinancialTracking,
            frequency = ReminderFrequency.Daily,
            timeLabel = "20:00",
            status = status
        )
    }

    private class FakeReminderWorkEnqueuer : ReminderWorkEnqueuer {
        val enqueued = mutableListOf<ReminderWorkPlan>()
        val cancelled = mutableListOf<String>()

        override fun enqueue(plan: ReminderWorkPlan) {
            enqueued += plan
        }

        override fun cancel(uniqueWorkName: String) {
            cancelled += uniqueWorkName
        }
    }

    private class FakeReminderChannelManager : ReminderChannelManager {
        var created = false
        override fun ensureReminderChannel() {
            created = true
        }
    }

    private class FakePermissionHelper(
        private val permissionState: ReminderPermissionState
    ) : ReminderPermissionHelper {
        override fun currentState(): ReminderPermissionState = permissionState
    }
}
