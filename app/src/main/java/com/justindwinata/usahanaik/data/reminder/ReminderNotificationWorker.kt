package com.justindwinata.usahanaik.data.reminder

import android.Manifest
import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.justindwinata.usahanaik.MainActivity
import com.justindwinata.usahanaik.R
import com.justindwinata.usahanaik.data.local.UsahaNaikDatabase
import com.justindwinata.usahanaik.data.mapper.BusinessReminderMapper.toDomain
import com.justindwinata.usahanaik.domain.model.BusinessReminder
import com.justindwinata.usahanaik.domain.model.ReminderPermissionState

class ReminderNotificationWorker(
    appContext: Context,
    workerParameters: WorkerParameters
) : CoroutineWorker(appContext, workerParameters) {
    override suspend fun doWork(): Result {
        val reminderId = ReminderNotificationWorkData.reminderIdFrom(inputData)
        if (reminderId <= 0L) return Result.success()

        val database = UsahaNaikDatabase.getDatabase(applicationContext)
        val reminder = database.businessReminderDao().getReminder(reminderId)?.toDomain()
        val permissionHelper = AndroidReminderPermissionHelper(applicationContext)
        val decision = ReminderNotificationWorkerPolicy.decide(
            reminder = reminder,
            permissionState = permissionHelper.currentState()
        )

        if (decision != ReminderNotificationDecision.ShowNotification || reminder == null) {
            return Result.success()
        }

        ReminderNotificationManager(applicationContext).ensureReminderChannel()
        showNotification(reminder)
        return Result.success()
    }

    @SuppressLint("MissingPermission")
    private fun showNotification(reminder: BusinessReminder) {
        if (!canPostNotification()) return

        val message = ReminderMessageFactory.createMessage(reminder)
        val intent = Intent(applicationContext, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        val pendingIntent = PendingIntent.getActivity(
            applicationContext,
            reminder.id.toInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val notification = NotificationCompat.Builder(
            applicationContext,
            ReminderNotificationManager.CHANNEL_ID
        )
            .setSmallIcon(R.drawable.ic_launcher)
            .setContentTitle(message.title)
            .setContentText(message.body)
            .setStyle(NotificationCompat.BigTextStyle().bigText(message.body))
            .setCategory(NotificationCompat.CATEGORY_REMINDER)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        NotificationManagerCompat.from(applicationContext).notify(
            reminder.notificationId(),
            notification
        )
    }

    private fun canPostNotification(): Boolean {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU ||
            ContextCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
    }

    private fun BusinessReminder.notificationId(): Int {
        return id.coerceIn(1L, Int.MAX_VALUE.toLong()).toInt()
    }
}
