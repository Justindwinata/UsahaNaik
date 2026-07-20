package com.justindwinata.usahanaik.data.reminder

import com.justindwinata.usahanaik.domain.model.BusinessReminder
import com.justindwinata.usahanaik.domain.model.ReminderFrequency
import com.justindwinata.usahanaik.domain.model.ReminderPermissionState
import com.justindwinata.usahanaik.domain.model.ReminderStatus
import com.justindwinata.usahanaik.domain.model.ReminderType
import org.junit.Assert.assertEquals
import org.junit.Test

class ReminderNotificationWorkerPolicyTest {
    @Test
    fun workDataUsesStableReminderIdAndUniqueName() {
        val inputData = ReminderNotificationWorkData.inputData(42L)

        assertEquals(42L, ReminderNotificationWorkData.reminderIdFrom(inputData))
        assertEquals("business_reminder_42", ReminderNotificationWorkData.uniqueWorkName(42L))
    }

    @Test
    fun missingReminderIsSkippedSafely() {
        val decision = ReminderNotificationWorkerPolicy.decide(
            reminder = null,
            permissionState = ReminderPermissionState.Granted
        )

        assertEquals(ReminderNotificationDecision.SkipMissingReminder, decision)
    }

    @Test
    fun inactiveReminderIsSkippedSafely() {
        val decision = ReminderNotificationWorkerPolicy.decide(
            reminder = sampleReminder(status = ReminderStatus.Paused),
            permissionState = ReminderPermissionState.Granted
        )

        assertEquals(ReminderNotificationDecision.SkipInactiveReminder, decision)
    }

    @Test
    fun permissionDeniedSkipsNotificationWithoutFailure() {
        val decision = ReminderNotificationWorkerPolicy.decide(
            reminder = sampleReminder(),
            permissionState = ReminderPermissionState.Denied
        )

        assertEquals(ReminderNotificationDecision.SkipPermissionUnavailable, decision)
    }

    @Test
    fun grantedPermissionShowsNotification() {
        val decision = ReminderNotificationWorkerPolicy.decide(
            reminder = sampleReminder(),
            permissionState = ReminderPermissionState.Granted
        )

        assertEquals(ReminderNotificationDecision.ShowNotification, decision)
    }

    private fun sampleReminder(status: ReminderStatus = ReminderStatus.Active): BusinessReminder {
        return BusinessReminder(
            id = 7L,
            title = "Record sales",
            description = "Record today\u2019s finance activity.",
            type = ReminderType.DailyFinancialTracking,
            frequency = ReminderFrequency.Daily,
            timeLabel = "20:00",
            status = status
        )
    }
}
