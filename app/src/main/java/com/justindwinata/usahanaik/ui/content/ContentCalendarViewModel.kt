package com.justindwinata.usahanaik.ui.content

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.justindwinata.usahanaik.data.repository.ContentCalendarRepository
import com.justindwinata.usahanaik.domain.model.ContentCalendarStatus
import com.justindwinata.usahanaik.domain.model.ContentIdea
import com.justindwinata.usahanaik.domain.model.ContentPlatform
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ContentCalendarViewModel(
    private val repository: ContentCalendarRepository,
    private val todayProvider: () -> String = {
        SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date())
    }
) : ViewModel() {
    private val _uiState = MutableStateFlow(ContentCalendarUiState(isLoading = true))
    val uiState: StateFlow<ContentCalendarUiState> = _uiState.asStateFlow()

    init {
        loadSchedules()
    }

    fun loadSchedules() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = false,
                schedules = repository.listSchedules(),
                errorMessage = null
            )
        }
    }

    fun requestSchedule(idea: ContentIdea) {
        if (idea.id == 0L) {
            _uiState.value = _uiState.value.copy(errorMessage = "Save the idea before scheduling it.")
            return
        }
        _uiState.value = _uiState.value.copy(
            form = idea.toScheduleForm(todayProvider()),
            successMessage = null,
            errorMessage = null
        )
    }

    fun cancelSchedule() {
        _uiState.value = _uiState.value.copy(form = ContentScheduleFormState())
    }

    fun updateScheduledDate(value: String) = updateForm { copy(scheduledDate = value) }
    fun updateTimeLabel(value: String) = updateForm { copy(timeLabel = value) }
    fun updatePostingNote(value: String) = updateForm { copy(postingNote = value) }
    fun updatePlatform(platform: ContentPlatform) = updateForm { copy(platform = platform) }

    fun saveSchedule() {
        val form = _uiState.value.form
        if (!form.canSave) {
            _uiState.value = _uiState.value.copy(errorMessage = form.dateError ?: "Complete schedule details first.")
            return
        }
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isSaving = true, errorMessage = null, successMessage = null)
            runCatching {
                repository.scheduleContent(form.toSchedule())
            }.onSuccess {
                _uiState.value = _uiState.value.copy(
                    isSaving = false,
                    schedules = repository.listSchedules(),
                    form = ContentScheduleFormState(),
                    successMessage = "Content scheduled locally."
                )
            }.onFailure { error ->
                _uiState.value = _uiState.value.copy(
                    isSaving = false,
                    errorMessage = error.message ?: "Failed to schedule content."
                )
            }
        }
    }

    fun updateStatus(id: Long, status: ContentCalendarStatus) {
        viewModelScope.launch {
            repository.updateStatus(id, status)
            _uiState.value = _uiState.value.copy(schedules = repository.listSchedules())
        }
    }

    fun deleteSchedule(id: Long) {
        viewModelScope.launch {
            repository.deleteSchedule(id)
            _uiState.value = _uiState.value.copy(
                schedules = repository.listSchedules(),
                successMessage = "Scheduled content deleted."
            )
        }
    }

    private fun updateForm(reducer: ContentScheduleFormState.() -> ContentScheduleFormState) {
        _uiState.value = _uiState.value.copy(form = _uiState.value.form.reducer())
    }
}

class ContentCalendarViewModelFactory(
    private val repository: ContentCalendarRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ContentCalendarViewModel::class.java)) {
            return ContentCalendarViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}
