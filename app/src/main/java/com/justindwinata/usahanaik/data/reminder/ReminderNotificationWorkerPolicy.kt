package com.justindwinata.usahanaik.data.reminder

import com.justindwinata.usahanaik.domain.model.BusinessReminder
import com.justindwinata.usahanaik.domain.model.ReminderPermissionState

object ReminderNotificationWorkerPolicy {
    fun decide(
        reminder: BusinessReminder?,
        permissionState: ReminderPermissionState
    ): ReminderNotificationDecision {
        if (reminder == null) return ReminderNotificationDecision.SkipMissingReminder
        if (!reminder.isActive) return ReminderNotificationDecision.SkipInactiveReminder

        return when (permissionState) {
            ReminderPermissionState.Granted,
            ReminderPermissionState.NotRequired -> ReminderNotificationDecision.ShowNotification
            ReminderPermissionState.Denied,
            ReminderPermissionState.Unknown -> ReminderNotificationDecision.SkipPermissionUnavailable
        }
    }
}

enum class ReminderNotificationDecision {
    ShowNotification,
    SkipMissingReminder,
    SkipInactiveReminder,
    SkipPermissionUnavailable
}
