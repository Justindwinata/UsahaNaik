package com.justindwinata.usahanaik.data.reminder

interface ReminderWorkEnqueuer {
    fun enqueue(plan: ReminderWorkPlan)
    fun cancel(uniqueWorkName: String)
}
