package com.justindwinata.usahanaik.data.reminder

import com.justindwinata.usahanaik.domain.model.BusinessReminder

interface ReminderScheduler {
    suspend fun schedule(reminder: BusinessReminder): ReminderScheduleResult
    suspend fun cancel(reminderId: Long): ReminderScheduleResult
}

data class ReminderScheduleResult(
    val status: ReminderScheduleStatus,
    val message: String
) {
    val isSuccess: Boolean = status == ReminderScheduleStatus.ScheduledInApp ||
        status == ReminderScheduleStatus.ScheduledSystem ||
        status == ReminderScheduleStatus.Cancelled
}

enum class ReminderScheduleStatus {
    ScheduledSystem,
    ScheduledInApp,
    Cancelled,
    SkippedDisabled,
    PermissionDenied,
    Unsupported
}
