package com.justindwinata.usahanaik.data.reminder

import com.justindwinata.usahanaik.domain.model.BusinessReminder
import com.justindwinata.usahanaik.domain.model.ReminderPermissionState

class AndroidReminderScheduler(
    private val notificationManager: ReminderChannelManager,
    private val permissionHelper: ReminderPermissionHelper,
    private val workEnqueuer: ReminderWorkEnqueuer,
    private val schedulePlanner: ReminderSchedulePlanner = ReminderSchedulePlanner()
) : ReminderScheduler {
    override suspend fun schedule(reminder: BusinessReminder): ReminderScheduleResult {
        if (!reminder.isActive) {
            workEnqueuer.cancel(ReminderNotificationWorkData.uniqueWorkName(reminder.id))
            return ReminderScheduleResult(
                status = ReminderScheduleStatus.SkippedDisabled,
                message = "Reminder is not active, so no notification was scheduled."
            )
        }

        notificationManager.ensureReminderChannel()
        return when (permissionHelper.currentState()) {
            ReminderPermissionState.Granted,
            ReminderPermissionState.NotRequired -> {
                workEnqueuer.enqueue(schedulePlanner.plan(reminder))
                ReminderScheduleResult(
                    status = ReminderScheduleStatus.ScheduledSystem,
                    message = "Approximate local notification scheduled."
                )
            }
            ReminderPermissionState.Denied,
            ReminderPermissionState.Unknown -> ReminderScheduleResult(
                status = ReminderScheduleStatus.PermissionDenied,
                message = "Reminder saved in app. Enable notifications later for system alerts."
            )
        }
    }

    override suspend fun cancel(reminderId: Long): ReminderScheduleResult {
        workEnqueuer.cancel(ReminderNotificationWorkData.uniqueWorkName(reminderId))
        return ReminderScheduleResult(
            status = ReminderScheduleStatus.Cancelled,
            message = "Scheduled notification work cancelled locally."
        )
    }
}
