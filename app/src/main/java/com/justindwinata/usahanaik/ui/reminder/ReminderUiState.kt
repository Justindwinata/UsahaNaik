package com.justindwinata.usahanaik.ui.reminder

import com.justindwinata.usahanaik.domain.model.BusinessReminder
import com.justindwinata.usahanaik.domain.model.ReminderFrequency
import com.justindwinata.usahanaik.domain.model.ReminderPermissionState
import com.justindwinata.usahanaik.domain.model.ReminderStatus
import com.justindwinata.usahanaik.domain.model.ReminderSummary
import com.justindwinata.usahanaik.domain.model.ReminderType

data class ReminderFormState(
    val type: ReminderType = ReminderType.DailyFinancialTracking,
    val title: String = ReminderType.DailyFinancialTracking.defaultTitle,
    val description: String = ReminderType.DailyFinancialTracking.defaultDescription,
    val frequency: ReminderFrequency = ReminderFrequency.Daily,
    val scheduledDay: String = "",
    val scheduledDate: String = "",
    val timeLabel: String = "20:00",
    val enabled: Boolean = true,
    val editingReminderId: Long? = null
) {
    val status: ReminderStatus
        get() = if (enabled) ReminderStatus.Active else ReminderStatus.Paused
}

data class ReminderValidationResult(
    val titleError: String? = null,
    val timeError: String? = null
) {
    val isValid: Boolean = listOf(titleError, timeError).all { it == null }
}

data class ReminderUiState(
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val reminders: List<BusinessReminder> = emptyList(),
    val form: ReminderFormState = ReminderFormState(),
    val validationResult: ReminderValidationResult = ReminderValidationResult(),
    val hasAttemptedSave: Boolean = false,
    val permissionState: ReminderPermissionState = ReminderPermissionState.Unknown,
    val summary: ReminderSummary = ReminderSummary(
        totalCount = 0,
        activeCount = 0,
        pausedCount = 0,
        completedCount = 0,
        disabledCount = 0,
        nextReminderTitle = "No active reminders",
        nextReminderTimeLabel = "Add a reminder from Profile"
    ),
    val successMessage: String? = null,
    val errorMessage: String? = null
) {
    val activeReminders: List<BusinessReminder> = reminders.filter { it.status == ReminderStatus.Active }

    fun visibleTitleError(): String? = validationResult.titleError.takeIf { hasAttemptedSave }

    fun visibleTimeError(): String? = validationResult.timeError.takeIf { hasAttemptedSave }
}

fun ReminderFormState.toReminder(): BusinessReminder {
    return BusinessReminder(
        id = editingReminderId ?: 0L,
        title = title.trim(),
        description = description.trim(),
        type = type,
        frequency = frequency,
        scheduledDay = scheduledDay.trim(),
        scheduledDate = scheduledDate.trim(),
        timeLabel = timeLabel.trim(),
        status = status
    )
}

fun BusinessReminder.toFormState(): ReminderFormState {
    return ReminderFormState(
        type = type,
        title = title,
        description = description,
        frequency = frequency,
        scheduledDay = scheduledDay,
        scheduledDate = scheduledDate,
        timeLabel = timeLabel,
        enabled = status == ReminderStatus.Active,
        editingReminderId = id
    )
}
