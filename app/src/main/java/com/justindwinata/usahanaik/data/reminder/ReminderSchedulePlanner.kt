package com.justindwinata.usahanaik.data.reminder

import com.justindwinata.usahanaik.domain.model.BusinessReminder
import com.justindwinata.usahanaik.domain.model.ReminderFrequency
import java.time.DayOfWeek
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.Locale
import kotlin.math.max

data class ReminderWorkPlan(
    val uniqueWorkName: String,
    val reminderId: Long,
    val initialDelayMinutes: Long,
    val repeatIntervalHours: Long? = null
) {
    val isPeriodic: Boolean = repeatIntervalHours != null
}

class ReminderSchedulePlanner(
    private val nowProvider: () -> LocalDateTime = { LocalDateTime.now() }
) {
    fun plan(reminder: BusinessReminder): ReminderWorkPlan {
        val now = nowProvider()
        val targetTime = parseTime(reminder.timeLabel)
        val targetDateTime = when (reminder.frequency) {
            ReminderFrequency.Daily -> nextDaily(now, targetTime)
            ReminderFrequency.Weekly -> nextWeekly(now, targetTime, reminder.scheduledDay)
            ReminderFrequency.Once -> nextOnce(now, targetTime, reminder.scheduledDate)
            ReminderFrequency.Custom -> nextDaily(now, targetTime)
        }
        val initialDelay = max(0L, Duration.between(now, targetDateTime).toMinutes())

        return ReminderWorkPlan(
            uniqueWorkName = ReminderNotificationWorkData.uniqueWorkName(reminder.id),
            reminderId = reminder.id,
            initialDelayMinutes = initialDelay,
            repeatIntervalHours = when (reminder.frequency) {
                ReminderFrequency.Daily,
                ReminderFrequency.Custom -> 24L
                ReminderFrequency.Weekly -> 24L * 7L
                ReminderFrequency.Once -> null
            }
        )
    }

    private fun parseTime(value: String): LocalTime {
        return runCatching {
            val parts = value.split(":")
            LocalTime.of(parts.getOrNull(0)?.toInt() ?: 20, parts.getOrNull(1)?.toInt() ?: 0)
        }.getOrDefault(LocalTime.of(20, 0))
    }

    private fun nextDaily(now: LocalDateTime, time: LocalTime): LocalDateTime {
        val today = LocalDateTime.of(now.toLocalDate(), time)
        return if (today.isAfter(now)) today else today.plusDays(1)
    }

    private fun nextWeekly(now: LocalDateTime, time: LocalTime, dayLabel: String): LocalDateTime {
        val targetDay = parseDayOfWeek(dayLabel) ?: now.dayOfWeek
        var candidate = LocalDateTime.of(now.toLocalDate(), time)
        while (candidate.dayOfWeek != targetDay || !candidate.isAfter(now)) {
            candidate = candidate.plusDays(1)
        }
        return candidate
    }

    private fun nextOnce(now: LocalDateTime, time: LocalTime, dateLabel: String): LocalDateTime {
        val date = runCatching { LocalDate.parse(dateLabel) }.getOrDefault(now.toLocalDate())
        val candidate = LocalDateTime.of(date, time)
        return if (candidate.isAfter(now)) candidate else now.plusMinutes(15)
    }

    private fun parseDayOfWeek(dayLabel: String): DayOfWeek? {
        val normalized = dayLabel.trim().uppercase(Locale.US)
        return DayOfWeek.entries.firstOrNull { day ->
            day.name == normalized || day.name.take(3) == normalized.take(3)
        }
    }
}
