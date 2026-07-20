package com.justindwinata.usahanaik.data.reminder

import com.justindwinata.usahanaik.domain.model.BusinessReminder
import com.justindwinata.usahanaik.domain.model.ReminderFrequency
import com.justindwinata.usahanaik.domain.model.ReminderType
import java.time.LocalDateTime
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ReminderSchedulePlannerTest {
    private val now = LocalDateTime.of(2026, 7, 21, 10, 0)
    private val planner = ReminderSchedulePlanner(nowProvider = { now })

    @Test
    fun dailyReminderUsesNextSameDayTimeWhenFuture() {
        val plan = planner.plan(sampleReminder(timeLabel = "20:00"))

        assertEquals("business_reminder_7", plan.uniqueWorkName)
        assertEquals(600L, plan.initialDelayMinutes)
        assertEquals(24L, plan.repeatIntervalHours)
        assertTrue(plan.isPeriodic)
    }

    @Test
    fun dailyReminderMovesToTomorrowWhenTimePassed() {
        val plan = planner.plan(sampleReminder(timeLabel = "08:00"))

        assertEquals(22L * 60L, plan.initialDelayMinutes)
    }

    @Test
    fun weeklyReminderUsesSelectedDay() {
        val plan = planner.plan(
            sampleReminder(
                frequency = ReminderFrequency.Weekly,
                scheduledDay = "Wednesday",
                timeLabel = "09:00"
            )
        )

        assertEquals(23L * 60L, plan.initialDelayMinutes)
        assertEquals(168L, plan.repeatIntervalHours)
    }

    @Test
    fun onceReminderIsOneTimeWork() {
        val plan = planner.plan(
            sampleReminder(
                frequency = ReminderFrequency.Once,
                scheduledDate = "2026-07-22",
                timeLabel = "12:00"
            )
        )

        assertEquals(26L * 60L, plan.initialDelayMinutes)
        assertFalse(plan.isPeriodic)
    }

    private fun sampleReminder(
        frequency: ReminderFrequency = ReminderFrequency.Daily,
        scheduledDay: String = "",
        scheduledDate: String = "",
        timeLabel: String = "20:00"
    ): BusinessReminder {
        return BusinessReminder(
            id = 7L,
            title = "Record sales",
            description = "Record today's business activity.",
            type = ReminderType.DailyFinancialTracking,
            frequency = frequency,
            scheduledDay = scheduledDay,
            scheduledDate = scheduledDate,
            timeLabel = timeLabel
        )
    }
}
