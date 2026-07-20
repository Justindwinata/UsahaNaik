package com.justindwinata.usahanaik.data.reminder

import androidx.work.Data

object ReminderNotificationWorkData {
    const val KEY_REMINDER_ID = "reminder_id"
    const val WORK_TAG = "usahanaik_business_reminder"

    fun uniqueWorkName(reminderId: Long): String = "business_reminder_$reminderId"

    fun inputData(reminderId: Long): Data {
        return Data.Builder()
            .putLong(KEY_REMINDER_ID, reminderId)
            .build()
    }

    fun reminderIdFrom(data: Data): Long {
        return data.getLong(KEY_REMINDER_ID, 0L)
    }
}
