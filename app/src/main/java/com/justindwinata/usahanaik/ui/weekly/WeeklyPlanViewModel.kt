package com.justindwinata.usahanaik.ui.weekly

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.justindwinata.usahanaik.data.repository.BusinessProfileRepository
import com.justindwinata.usahanaik.data.repository.FinancialEntryRepository
import com.justindwinata.usahanaik.data.repository.WeeklyPlanRepository
import com.justindwinata.usahanaik.domain.diagnosis.BusinessDiagnosisEngine
import com.justindwinata.usahanaik.domain.model.WeeklyTaskStatus
import com.justindwinata.usahanaik.domain.weekly.WeeklyPlanGenerator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class WeeklyPlanViewModel(
    private val businessProfileRepository: BusinessProfileRepository,
    private val financialEntryRepository: FinancialEntryRepository,
    private val weeklyPlanRepository: WeeklyPlanRepository,
    private val diagnosisEngine: BusinessDiagnosisEngine = BusinessDiagnosisEngine(),
    private val weeklyPlanGenerator: WeeklyPlanGenerator = WeeklyPlanGenerator(),
    private val monthPrefix: String = "2026-07",
    private val generatedDateProvider: () -> String = { "2026-07-19" }
) : ViewModel() {
    private val _uiState = MutableStateFlow(WeeklyPlanUiState(isLoading = true))
    val uiState: StateFlow<WeeklyPlanUiState> = _uiState.asStateFlow()

    init {
        loadActivePlan()
    }

    fun loadActivePlan() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            runCatching {
                val profile = businessProfileRepository.getActiveBusinessProfile()
                val plan = weeklyPlanRepository.getActivePlan()
                WeeklyPlanUiState(
                    isLoading = false,
                    profile = profile,
                    activePlan = plan
                )
            }.onSuccess { state ->
                _uiState.value = state
            }.onFailure { error ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = error.message ?: "Failed to load weekly plan."
                )
            }
        }
    }

    fun generatePlan(replaceExisting: Boolean = false) {
        val currentPlan = _uiState.value.activePlan
        if (currentPlan != null && !replaceExisting) {
            _uiState.value = _uiState.value.copy(showRegenerateConfirmation = true)
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isGenerating = true, errorMessage = null, successMessage = null)
            runCatching {
                val profile = businessProfileRepository.getActiveBusinessProfile()
                    ?: error("Complete business setup first.")
                val financialSummary = financialEntryRepository.getFinancialSummary(
                    monthPrefix = monthPrefix,
                    targetMonthlyRevenue = profile.draft.targetMonthlyRevenue,
                    targetMonthlyProfit = profile.draft.targetMonthlyProfit
                )
                val diagnosis = diagnosisEngine.diagnose(profile, financialSummary)
                val generatedPlan = weeklyPlanGenerator.generate(
                    profile = profile,
                    financialSummary = financialSummary,
                    diagnosis = diagnosis,
                    generatedDate = generatedDateProvider()
                )
                weeklyPlanRepository.savePlan(generatedPlan) to profile
            }.onSuccess { (savedPlan, profile) ->
                _uiState.value = _uiState.value.copy(
                    isGenerating = false,
                    profile = profile,
                    activePlan = savedPlan,
                    showRegenerateConfirmation = false,
                    successMessage = "Weekly growth plan generated locally."
                )
            }.onFailure { error ->
                _uiState.value = _uiState.value.copy(
                    isGenerating = false,
                    showRegenerateConfirmation = false,
                    errorMessage = error.message ?: "Failed to generate weekly plan."
                )
            }
        }
    }

    fun requestRegeneratePlan() {
        _uiState.value = _uiState.value.copy(showRegenerateConfirmation = true)
    }

    fun cancelRegeneratePlan() {
        _uiState.value = _uiState.value.copy(showRegenerateConfirmation = false)
    }

    fun confirmRegeneratePlan() {
        generatePlan(replaceExisting = true)
    }

    fun toggleTaskCompletion(taskId: String) {
        val plan = _uiState.value.activePlan ?: return
        val task = plan.tasks.firstOrNull { it.id == taskId } ?: return
        val nextStatus = if (task.status == WeeklyTaskStatus.Completed) {
            WeeklyTaskStatus.Pending
        } else {
            WeeklyTaskStatus.Completed
        }
        viewModelScope.launch {
            runCatching {
                weeklyPlanRepository.updateTaskStatus(taskId, nextStatus)
            }.onSuccess { updatedPlan ->
                _uiState.value = _uiState.value.copy(
                    activePlan = updatedPlan,
                    successMessage = null,
                    errorMessage = null
                )
            }.onFailure { error ->
                _uiState.value = _uiState.value.copy(
                    errorMessage = error.message ?: "Failed to update task."
                )
            }
        }
    }

    fun deleteActivePlan() {
        viewModelScope.launch {
            runCatching {
                weeklyPlanRepository.deleteActivePlan()
            }.onSuccess {
                _uiState.value = _uiState.value.copy(
                    activePlan = null,
                    successMessage = "Weekly plan deleted locally.",
                    errorMessage = null
                )
            }.onFailure { error ->
                _uiState.value = _uiState.value.copy(
                    errorMessage = error.message ?: "Failed to delete weekly plan."
                )
            }
        }
    }
}

class WeeklyPlanViewModelFactory(
    private val businessProfileRepository: BusinessProfileRepository,
    private val financialEntryRepository: FinancialEntryRepository,
    private val weeklyPlanRepository: WeeklyPlanRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WeeklyPlanViewModel::class.java)) {
            return WeeklyPlanViewModel(
                businessProfileRepository = businessProfileRepository,
                financialEntryRepository = financialEntryRepository,
                weeklyPlanRepository = weeklyPlanRepository
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}
