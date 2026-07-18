package com.justindwinata.usahanaik.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.justindwinata.usahanaik.data.repository.BusinessProfileRepository
import com.justindwinata.usahanaik.data.repository.FinancialEntryRepository
import com.justindwinata.usahanaik.domain.diagnosis.BusinessDiagnosisEngine
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DashboardInsightsViewModel(
    private val businessProfileRepository: BusinessProfileRepository,
    private val financialEntryRepository: FinancialEntryRepository,
    private val diagnosisEngine: BusinessDiagnosisEngine = BusinessDiagnosisEngine(),
    private val monthPrefix: String = "2026-07"
) : ViewModel() {
    private val _uiState = MutableStateFlow(DashboardInsightsUiState(isLoading = true))
    val uiState: StateFlow<DashboardInsightsUiState> = _uiState.asStateFlow()

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            runCatching {
                val profile = businessProfileRepository.getActiveBusinessProfile()
                val financialSummary = financialEntryRepository.getFinancialSummary(
                    monthPrefix = monthPrefix,
                    targetMonthlyRevenue = profile?.draft?.targetMonthlyRevenue,
                    targetMonthlyProfit = profile?.draft?.targetMonthlyProfit
                )
                val diagnosis = diagnosisEngine.diagnose(profile, financialSummary)
                DashboardInsightsUiState(
                    isLoading = false,
                    profile = profile,
                    financialSummary = financialSummary,
                    diagnosis = diagnosis,
                    emptyStateMessage = diagnosis.summary.emptyStateMessage
                )
            }.onSuccess { nextState ->
                _uiState.value = nextState
            }.onFailure { error ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = error.message ?: "Failed to generate dashboard insights."
                )
            }
        }
    }
}

class DashboardInsightsViewModelFactory(
    private val businessProfileRepository: BusinessProfileRepository,
    private val financialEntryRepository: FinancialEntryRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DashboardInsightsViewModel::class.java)) {
            return DashboardInsightsViewModel(
                businessProfileRepository = businessProfileRepository,
                financialEntryRepository = financialEntryRepository
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}
