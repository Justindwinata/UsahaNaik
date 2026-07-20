package com.justindwinata.usahanaik.data.reminder

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

class WorkManagerReminderWorkEnqueuer(
    context: Context
) : ReminderWorkEnqueuer {
    private val workManager = WorkManager.getInstance(context.applicationContext)

    override fun enqueue(plan: ReminderWorkPlan) {
        if (plan.isPeriodic) {
            val request = PeriodicWorkRequestBuilder<ReminderNotificationWorker>(
                checkNotNull(plan.repeatIntervalHours),
                TimeUnit.HOURS
            )
                .setInputData(ReminderNotificationWorkData.inputData(plan.reminderId))
                .setInitialDelay(plan.initialDelayMinutes, TimeUnit.MINUTES)
                .addTag(ReminderNotificationWorkData.WORK_TAG)
                .build()

            workManager.enqueueUniquePeriodicWork(
                plan.uniqueWorkName,
                ExistingPeriodicWorkPolicy.CANCEL_AND_REENQUEUE,
                request
            )
        } else {
            val request = OneTimeWorkRequestBuilder<ReminderNotificationWorker>()
                .setInputData(ReminderNotificationWorkData.inputData(plan.reminderId))
                .setInitialDelay(plan.initialDelayMinutes, TimeUnit.MINUTES)
                .addTag(ReminderNotificationWorkData.WORK_TAG)
                .build()

            workManager.enqueueUniqueWork(
                plan.uniqueWorkName,
                ExistingWorkPolicy.REPLACE,
                request
            )
        }
    }

    override fun cancel(uniqueWorkName: String) {
        workManager.cancelUniqueWork(uniqueWorkName)
    }
}
