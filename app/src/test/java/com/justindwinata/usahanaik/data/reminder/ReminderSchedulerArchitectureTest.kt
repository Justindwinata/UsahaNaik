package com.justindwinata.usahanaik.data.reminder

import com.justindwinata.usahanaik.domain.model.BusinessReminder
import com.justindwinata.usahanaik.domain.model.ReminderFrequency
import com.justindwinata.usahanaik.domain.model.ReminderPermissionState
import com.justindwinata.usahanaik.domain.model.ReminderStatus
import com.justindwinata.usahanaik.domain.model.ReminderType
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ReminderSchedulerArchitectureTest {
    @Test
    fun fakeSchedulerSchedulesActiveReminder() = runTest {
        val scheduler = FakeReminderScheduler()

        val result = scheduler.schedule(sampleReminder(id = 1L))

        assertEquals(ReminderScheduleStatus.ScheduledInApp, result.status)
        assertTrue(result.isSuccess)
        assertEquals(listOf(1L), scheduler.scheduledIds)
    }

    @Test
    fun fakeSchedulerCancelsDisabledReminder() = runTest {
        val scheduler = FakeReminderScheduler()

        val result = scheduler.cancel(4L)

        assertEquals(ReminderScheduleStatus.Cancelled, result.status)
        assertEquals(listOf(4L), scheduler.cancelledIds)
    }

    @Test
    fun permissionDeniedDoesNotCrashScheduling() = runTest {
        val scheduler = PermissionAwareFakeScheduler(ReminderPermissionState.Denied)

        val result = scheduler.schedule(sampleReminder())

        assertEquals(ReminderScheduleStatus.PermissionDenied, result.status)
        assertFalse(result.isSuccess)
    }

    @Test
    fun disabledReminderIsSkipped() = runTest {
        val scheduler = PermissionAwareFakeScheduler(ReminderPermissionState.Granted)

        val result = scheduler.schedule(sampleReminder(status = ReminderStatus.Paused))

        assertEquals(ReminderScheduleStatus.SkippedDisabled, result.status)
    }

    @Test
    fun messageFactoryReturnsSafeBusinessCopy() {
        val message = ReminderMessageFactory.createMessage(
            sampleReminder(
                title = "",
                description = "",
                type = ReminderType.BusinessReportReview
            )
        )

        assertEquals(ReminderType.BusinessReportReview.defaultTitle, message.title)
        assertTrue(message.body.contains("local business report"))
        assertFalse(message.body.contains("guaranteed", ignoreCase = true))
    }

    private fun sampleReminder(
        id: Long = 0L,
        title: String = "Record sales",
        description: String = "Record today's business activity.",
        type: ReminderType = ReminderType.DailyFinancialTracking,
        status: ReminderStatus = ReminderStatus.Active
    ): BusinessReminder {
        return BusinessReminder(
            id = id,
            title = title,
            description = description,
            type = type,
            frequency = ReminderFrequency.Daily,
            timeLabel = "20:00",
            status = status
        )
    }

    private class FakeReminderScheduler : ReminderScheduler {
        val scheduledIds = mutableListOf<Long>()
        val cancelledIds = mutableListOf<Long>()

        override suspend fun schedule(reminder: BusinessReminder): ReminderScheduleResult {
            scheduledIds += reminder.id
            return ReminderScheduleResult(
                status = ReminderScheduleStatus.ScheduledInApp,
                message = "Scheduled in app."
            )
        }

        override suspend fun cancel(reminderId: Long): ReminderScheduleResult {
            cancelledIds += reminderId
            return ReminderScheduleResult(
                status = ReminderScheduleStatus.Cancelled,
                message = "Cancelled."
            )
        }
    }

    private class PermissionAwareFakeScheduler(
        private val permissionState: ReminderPermissionState
    ) : ReminderScheduler {
        override suspend fun schedule(reminder: BusinessReminder): ReminderScheduleResult {
            if (!reminder.isActive) {
                return ReminderScheduleResult(
                    status = ReminderScheduleStatus.SkippedDisabled,
                    message = "Skipped."
                )
            }
            return when (permissionState) {
                ReminderPermissionState.Granted,
                ReminderPermissionState.NotRequired -> ReminderScheduleResult(
                    status = ReminderScheduleStatus.ScheduledInApp,
                    message = "Scheduled."
                )
                ReminderPermissionState.Denied,
                ReminderPermissionState.Unknown -> ReminderScheduleResult(
                    status = ReminderScheduleStatus.PermissionDenied,
                    message = "In-app fallback."
                )
            }
        }

        override suspend fun cancel(reminderId: Long): ReminderScheduleResult {
            return ReminderScheduleResult(ReminderScheduleStatus.Cancelled, "Cancelled.")
        }
    }
}
