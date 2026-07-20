package com.justindwinata.usahanaik.ui.reminder

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.justindwinata.usahanaik.data.reminder.ReminderPermissionHelper
import com.justindwinata.usahanaik.data.reminder.ReminderScheduler
import com.justindwinata.usahanaik.data.repository.BusinessReminderRepository
import com.justindwinata.usahanaik.domain.model.BusinessReminder
import com.justindwinata.usahanaik.domain.model.ReminderFrequency
import com.justindwinata.usahanaik.domain.model.ReminderPermissionState
import com.justindwinata.usahanaik.domain.model.ReminderStatus
import com.justindwinata.usahanaik.domain.model.ReminderSummaryCalculator
import com.justindwinata.usahanaik.domain.model.ReminderType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ReminderViewModel(
    private val repository: BusinessReminderRepository,
    private val scheduler: ReminderScheduler,
    private val permissionHelper: ReminderPermissionHelper
) : ViewModel() {
    private val _uiState = MutableStateFlow(ReminderUiState(isLoading = true))
    val uiState: StateFlow<ReminderUiState> = _uiState.asStateFlow()

    init {
        loadReminders()
    }

    fun loadReminders() {
        viewModelScope.launch {
            val permissionState = permissionHelper.currentState()
            val reminders = repository.listReminders()
            _uiState.value = _uiState.value.copy(
                isLoading = false,
                reminders = reminders,
                permissionState = permissionState,
                summary = ReminderSummaryCalculator.summarize(reminders, permissionState),
                errorMessage = null
            )
        }
    }

    fun refreshPermissionState() {
        viewModelScope.launch {
            val permissionState = permissionHelper.currentState()
            val reminders = _uiState.value.reminders.ifEmpty { repository.listReminders() }
            if (permissionState == ReminderPermissionState.Granted || permissionState == ReminderPermissionState.NotRequired) {
                reminders.filter { it.status == ReminderStatus.Active }.forEach { scheduler.schedule(it) }
            }
            _uiState.value = _uiState.value.copy(
                permissionState = permissionState,
                summary = ReminderSummaryCalculator.summarize(reminders, permissionState)
            )
        }
    }

    fun updateType(type: ReminderType) {
        val frequency = when (type) {
            ReminderType.DailyFinancialTracking -> ReminderFrequency.Daily
            ReminderType.ContentSchedule -> ReminderFrequency.Once
            ReminderType.WeeklyPlanTask,
            ReminderType.WeeklyRetrospective,
            ReminderType.BusinessReportReview -> ReminderFrequency.Weekly
        }
        updateForm {
            copy(
                type = type,
                title = type.defaultTitle,
                description = type.defaultDescription,
                frequency = frequency
            )
        }
    }

    fun updateTitle(value: String) = updateForm { copy(title = value) }
    fun updateDescription(value: String) = updateForm { copy(description = value) }
    fun updateFrequency(value: ReminderFrequency) = updateForm { copy(frequency = value) }
    fun updateScheduledDay(value: String) = updateForm { copy(scheduledDay = value) }
    fun updateScheduledDate(value: String) = updateForm { copy(scheduledDate = value) }
    fun updateTimeLabel(value: String) = updateForm { copy(timeLabel = value) }
    fun updateEnabled(value: Boolean) = updateForm { copy(enabled = value) }

    fun editReminder(reminder: BusinessReminder) {
        _uiState.value = _uiState.value.copy(
            form = reminder.toFormState(),
            validationResult = validate(reminder.toFormState()),
            hasAttemptedSave = false,
            successMessage = null,
            errorMessage = null
        )
    }

    fun resetForm() {
        _uiState.value = _uiState.value.copy(
            form = ReminderFormState(),
            validationResult = ReminderValidationResult(),
            hasAttemptedSave = false,
            successMessage = null,
            errorMessage = null
        )
    }

    fun saveReminder() {
        val form = _uiState.value.form
        val validation = validate(form)
        _uiState.value = _uiState.value.copy(
            validationResult = validation,
            hasAttemptedSave = true,
            successMessage = null,
            errorMessage = null
        )
        if (!validation.isValid) return

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isSaving = true)
            runCatching {
                val reminder = form.toReminder()
                val saved = if (form.editingReminderId == null) {
                    repository.createReminder(reminder)
                } else {
                    repository.updateReminder(reminder)
                }
                val scheduleResult = if (saved.status == ReminderStatus.Active) {
                    scheduler.schedule(saved)
                } else {
                    scheduler.cancel(saved.id)
                }
                saved to scheduleResult.message
            }.onSuccess { (_, scheduleMessage) ->
                refreshAfterMutation(
                    successMessage = "Reminder saved locally. $scheduleMessage",
                    resetForm = true
                )
            }.onFailure { error ->
                _uiState.value = _uiState.value.copy(
                    isSaving = false,
                    errorMessage = error.message ?: "Failed to save reminder."
                )
            }
        }
    }

    fun enableReminder(id: Long) {
        viewModelScope.launch {
            val reminder = repository.enableReminder(id)
            if (reminder != null) scheduler.schedule(reminder)
            refreshAfterMutation("Reminder enabled.")
        }
    }

    fun pauseReminder(id: Long) {
        viewModelScope.launch {
            repository.pauseReminder(id)
            scheduler.cancel(id)
            refreshAfterMutation("Reminder paused.")
        }
    }

    fun deleteReminder(id: Long) {
        viewModelScope.launch {
            repository.deleteReminder(id)
            scheduler.cancel(id)
            refreshAfterMutation("Reminder deleted.")
        }
    }

    private suspend fun refreshAfterMutation(
        successMessage: String,
        resetForm: Boolean = false
    ) {
        val permissionState = permissionHelper.currentState()
        val reminders = repository.listReminders()
        _uiState.value = _uiState.value.copy(
            isSaving = false,
            reminders = reminders,
            form = if (resetForm) ReminderFormState() else _uiState.value.form,
            validationResult = if (resetForm) ReminderValidationResult() else _uiState.value.validationResult,
            hasAttemptedSave = false,
            permissionState = permissionState,
            summary = ReminderSummaryCalculator.summarize(reminders, permissionState),
            successMessage = successMessage,
            errorMessage = null
        )
    }

    private fun updateForm(reducer: ReminderFormState.() -> ReminderFormState) {
        val next = _uiState.value.form.reducer()
        _uiState.value = _uiState.value.copy(
            form = next,
            validationResult = validate(next),
            successMessage = null,
            errorMessage = null
        )
    }

    private fun validate(form: ReminderFormState): ReminderValidationResult {
        return ReminderValidationResult(
            titleError = if (form.title.isBlank()) "Reminder title is required." else null,
            timeError = if (form.timeLabel.isBlank()) "Choose a reminder time." else null
        )
    }
}

class ReminderViewModelFactory(
    private val repository: BusinessReminderRepository,
    private val scheduler: ReminderScheduler,
    private val permissionHelper: ReminderPermissionHelper
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ReminderViewModel::class.java)) {
            return ReminderViewModel(repository, scheduler, permissionHelper) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}
