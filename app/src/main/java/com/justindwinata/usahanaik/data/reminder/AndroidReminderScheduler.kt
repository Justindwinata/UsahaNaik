package com.justindwinata.usahanaik.data.reminder

import com.justindwinata.usahanaik.domain.model.BusinessReminder
import com.justindwinata.usahanaik.domain.model.ReminderPermissionState

class AndroidReminderScheduler(
    private val notificationManager: ReminderNotificationManager,
    private val permissionHelper: ReminderPermissionHelper
) : ReminderScheduler {
    override suspend fun schedule(reminder: BusinessReminder): ReminderScheduleResult {
        if (!reminder.isActive) {
            return ReminderScheduleResult(
                status = ReminderScheduleStatus.SkippedDisabled,
                message = "Reminder is not active, so no notification was scheduled."
            )
        }

        notificationManager.ensureReminderChannel()
        return when (permissionHelper.currentState()) {
            ReminderPermissionState.Granted,
            ReminderPermissionState.NotRequired -> ReminderScheduleResult(
                status = ReminderScheduleStatus.ScheduledInApp,
                message = "Reminder saved locally. System scheduling is prepared for a future milestone."
            )
            ReminderPermissionState.Denied,
            ReminderPermissionState.Unknown -> ReminderScheduleResult(
                status = ReminderScheduleStatus.PermissionDenied,
                message = "Reminder saved in app. Enable notifications later for system alerts."
            )
        }
    }

    override suspend fun cancel(reminderId: Long): ReminderScheduleResult {
        return ReminderScheduleResult(
            status = ReminderScheduleStatus.Cancelled,
            message = "Reminder notification request cancelled locally."
        )
    }
}
