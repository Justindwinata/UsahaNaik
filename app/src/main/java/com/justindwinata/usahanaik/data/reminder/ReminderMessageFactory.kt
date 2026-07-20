package com.justindwinata.usahanaik.data.reminder

import com.justindwinata.usahanaik.domain.model.BusinessReminder
import com.justindwinata.usahanaik.domain.model.ReminderType

data class ReminderNotificationMessage(
    val title: String,
    val body: String
)

object ReminderMessageFactory {
    fun createMessage(reminder: BusinessReminder): ReminderNotificationMessage {
        val safeBody = when (reminder.type) {
            ReminderType.DailyFinancialTracking ->
                "Record today's income and expenses so your local dashboard stays easier to review."
            ReminderType.WeeklyPlanTask ->
                "Open your weekly plan and choose one practical task to continue."
            ReminderType.ContentSchedule ->
                "Review your scheduled content idea before posting. Adjust the caption if needed."
            ReminderType.WeeklyRetrospective ->
                "Review completed tasks, content activity, and financial progress for this week."
            ReminderType.BusinessReportReview ->
                "Open your local business report summary and review estimated progress."
        }

        return ReminderNotificationMessage(
            title = reminder.title.ifBlank { reminder.type.defaultTitle },
            body = reminder.description.ifBlank { safeBody }
        )
    }
}
