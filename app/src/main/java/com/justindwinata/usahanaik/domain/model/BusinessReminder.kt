package com.justindwinata.usahanaik.domain.model

data class BusinessReminder(
    val id: Long = 0L,
    val title: String,
    val description: String,
    val type: ReminderType,
    val frequency: ReminderFrequency,
    val scheduledDay: String = "",
    val scheduledDate: String = "",
    val timeLabel: String,
    val status: ReminderStatus = ReminderStatus.Active,
    val relatedEntityId: Long? = null,
    val createdAt: Long = 0L,
    val updatedAt: Long = 0L
) {
    val isActive: Boolean = status == ReminderStatus.Active

    val scheduleLabel: String
        get() = when (frequency) {
            ReminderFrequency.Daily -> "Every day at $timeLabel"
            ReminderFrequency.Weekly -> listOf(scheduledDay.ifBlank { "Weekly" }, timeLabel)
                .filter { it.isNotBlank() }
                .joinToString(" at ")
            ReminderFrequency.Once -> listOf(scheduledDate.ifBlank { "Selected date" }, timeLabel)
                .filter { it.isNotBlank() }
                .joinToString(" at ")
            ReminderFrequency.Custom -> listOf(scheduledDay.ifBlank { "Custom reminder" }, timeLabel)
                .filter { it.isNotBlank() }
                .joinToString(" at ")
        }
}

enum class ReminderType(val label: String, val defaultTitle: String, val defaultDescription: String) {
    DailyFinancialTracking(
        label = "Daily financial tracking",
        defaultTitle = "Record today\u2019s sales and expenses",
        defaultDescription = "Add income and expense entries so your dashboard stays accurate."
    ),
    WeeklyPlanTask(
        label = "Weekly plan task",
        defaultTitle = "Review weekly growth tasks",
        defaultDescription = "Check your active plan and complete one practical business task."
    ),
    ContentSchedule(
        label = "Scheduled content",
        defaultTitle = "Prepare scheduled content",
        defaultDescription = "Review your content idea before posting it."
    ),
    WeeklyRetrospective(
        label = "Weekly retrospective",
        defaultTitle = "Review weekly progress",
        defaultDescription = "Evaluate completed tasks, content activity, and financial progress."
    ),
    BusinessReportReview(
        label = "Business report review",
        defaultTitle = "Review business report",
        defaultDescription = "Open the report dashboard and review estimated progress from local data."
    )
}

enum class ReminderFrequency(val label: String) {
    Daily("Daily"),
    Weekly("Weekly"),
    Once("Once"),
    Custom("Custom")
}

enum class ReminderStatus(val label: String) {
    Active("Active"),
    Paused("Paused"),
    Completed("Completed"),
    Disabled("Disabled")
}

data class ReminderTime(
    val hour: Int,
    val minute: Int
) {
    val label: String
        get() = "%02d:%02d".format(hour.coerceIn(0, 23), minute.coerceIn(0, 59))
}

data class ReminderSchedule(
    val frequency: ReminderFrequency,
    val scheduledDay: String = "",
    val scheduledDate: String = "",
    val time: ReminderTime
) {
    val timeLabel: String = time.label
}

enum class ReminderPermissionState(val label: String) {
    NotRequired("Notification permission not required"),
    Granted("Notifications enabled"),
    Denied("Notifications not enabled"),
    Unknown("Permission status unknown")
}

data class ReminderSummary(
    val totalCount: Int,
    val activeCount: Int,
    val pausedCount: Int,
    val completedCount: Int,
    val disabledCount: Int,
    val nextReminderTitle: String,
    val nextReminderTimeLabel: String,
    val permissionState: ReminderPermissionState = ReminderPermissionState.Unknown
) {
    val hasActiveReminders: Boolean = activeCount > 0
}

object ReminderSummaryCalculator {
    fun summarize(
        reminders: List<BusinessReminder>,
        permissionState: ReminderPermissionState = ReminderPermissionState.Unknown
    ): ReminderSummary {
        val activeReminders = reminders
            .filter { it.status == ReminderStatus.Active }
            .sortedWith(compareBy<BusinessReminder> { it.scheduledDate.ifBlank { "9999-12-31" } }.thenBy { it.timeLabel })
        val nextReminder = activeReminders.firstOrNull()

        return ReminderSummary(
            totalCount = reminders.size,
            activeCount = activeReminders.size,
            pausedCount = reminders.count { it.status == ReminderStatus.Paused },
            completedCount = reminders.count { it.status == ReminderStatus.Completed },
            disabledCount = reminders.count { it.status == ReminderStatus.Disabled },
            nextReminderTitle = nextReminder?.title ?: "No active reminders",
            nextReminderTimeLabel = nextReminder?.scheduleLabel ?: "Add a reminder from Profile",
            permissionState = permissionState
        )
    }
}
