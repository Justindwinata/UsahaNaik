package com.justindwinata.usahanaik.domain.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class BusinessReminderModelTest {
    @Test
    fun reminderLabelsAreReadableForUsers() {
        assertEquals("Daily financial tracking", ReminderType.DailyFinancialTracking.label)
        assertEquals("Weekly", ReminderFrequency.Weekly.label)
        assertEquals("Paused", ReminderStatus.Paused.label)
    }

    @Test
    fun activeStatusControlsActiveFlag() {
        assertTrue(sampleReminder().isActive)
        assertFalse(sampleReminder(status = ReminderStatus.Paused).isActive)
        assertFalse(sampleReminder(status = ReminderStatus.Disabled).isActive)
    }

    @Test
    fun scheduleLabelUsesFrequencyAndTime() {
        assertEquals("Every day at 20:00", sampleReminder().scheduleLabel)
        assertEquals(
            "Monday at 09:00",
            sampleReminder(
                frequency = ReminderFrequency.Weekly,
                scheduledDay = "Monday",
                timeLabel = "09:00"
            ).scheduleLabel
        )
    }

    @Test
    fun reminderTimeFormatsAsTwentyFourHourLabel() {
        assertEquals("08:05", ReminderTime(hour = 8, minute = 5).label)
        assertEquals("23:59", ReminderTime(hour = 99, minute = 99).label)
    }

    @Test
    fun summaryCountsReminderStatusesAndNextReminder() {
        val summary = ReminderSummaryCalculator.summarize(
            listOf(
                sampleReminder(title = "Evening finance", timeLabel = "20:00"),
                sampleReminder(title = "Morning report", timeLabel = "08:00"),
                sampleReminder(status = ReminderStatus.Paused),
                sampleReminder(status = ReminderStatus.Completed),
                sampleReminder(status = ReminderStatus.Disabled)
            ),
            permissionState = ReminderPermissionState.Denied
        )

        assertEquals(5, summary.totalCount)
        assertEquals(2, summary.activeCount)
        assertEquals(1, summary.pausedCount)
        assertEquals(1, summary.completedCount)
        assertEquals(1, summary.disabledCount)
        assertEquals("Morning report", summary.nextReminderTitle)
        assertEquals(ReminderPermissionState.Denied, summary.permissionState)
        assertTrue(summary.hasActiveReminders)
    }

    @Test
    fun emptySummaryShowsFallbackCopy() {
        val summary = ReminderSummaryCalculator.summarize(emptyList())

        assertEquals("No active reminders", summary.nextReminderTitle)
        assertEquals("Add a reminder from Profile", summary.nextReminderTimeLabel)
        assertFalse(summary.hasActiveReminders)
    }

    private fun sampleReminder(
        title: String = "Record sales",
        frequency: ReminderFrequency = ReminderFrequency.Daily,
        scheduledDay: String = "",
        timeLabel: String = "20:00",
        status: ReminderStatus = ReminderStatus.Active
    ): BusinessReminder {
        return BusinessReminder(
            title = title,
            description = "Record today's business activity.",
            type = ReminderType.DailyFinancialTracking,
            frequency = frequency,
            scheduledDay = scheduledDay,
            timeLabel = timeLabel,
            status = status
        )
    }
}
