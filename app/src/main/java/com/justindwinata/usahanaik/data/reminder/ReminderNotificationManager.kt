package com.justindwinata.usahanaik.data.reminder

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build

class ReminderNotificationManager(
    private val context: Context
) {
    fun ensureReminderChannel() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return

        val channel = NotificationChannel(
            CHANNEL_ID,
            CHANNEL_NAME,
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = "Local business routine reminders for UsahaNaik."
        }
        context.getSystemService(NotificationManager::class.java)
            ?.createNotificationChannel(channel)
    }

    companion object {
        const val CHANNEL_ID = "usahanaik_business_reminders"
        const val CHANNEL_NAME = "Business reminders"
    }
}
