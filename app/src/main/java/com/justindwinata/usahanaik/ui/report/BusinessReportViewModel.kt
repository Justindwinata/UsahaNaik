package com.justindwinata.usahanaik.ui.report

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.justindwinata.usahanaik.data.repository.BusinessProfileRepository
import com.justindwinata.usahanaik.data.repository.BusinessReportSnapshotRepository
import com.justindwinata.usahanaik.data.repository.ContentCalendarRepository
import com.justindwinata.usahanaik.data.repository.ContentIdeaRepository
import com.justindwinata.usahanaik.data.repository.FinancialEntryRepository
import com.justindwinata.usahanaik.data.repository.WeeklyPlanRepository
import com.justindwinata.usahanaik.data.repository.WeeklyProgressHistoryRepository
import com.justindwinata.usahanaik.data.repository.WeeklyRetrospectiveRepository
import com.justindwinata.usahanaik.domain.diagnosis.BusinessDiagnosisEngine
import com.justindwinata.usahanaik.domain.model.BusinessReport
import com.justindwinata.usahanaik.domain.model.BusinessReportPeriod
import com.justindwinata.usahanaik.domain.model.BusinessReportSnapshot
import com.justindwinata.usahanaik.domain.report.BusinessReportGenerator
import com.justindwinata.usahanaik.domain.report.BusinessReportInput
import com.justindwinata.usahanaik.domain.report.ExportReadyReportRenderer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate

class BusinessReportViewModel(
    private val businessProfileRepository: BusinessProfileRepository,
    private val financialEntryRepository: FinancialEntryRepository,
    private val weeklyPlanRepository: WeeklyPlanRepository,
    private val contentIdeaRepository: ContentIdeaRepository,
    private val contentCalendarRepository: ContentCalendarRepository,
    private val weeklyProgressHistoryRepository: WeeklyProgressHistoryRepository,
    private val weeklyRetrospectiveRepository: WeeklyRetrospectiveRepository,
    private val snapshotRepository: BusinessReportSnapshotRepository,
    private val diagnosisEngine: BusinessDiagnosisEngine = BusinessDiagnosisEngine(),
    private val reportGenerator: BusinessReportGenerator = BusinessReportGenerator(),
    private val reportRenderer: ExportReadyReportRenderer = ExportReadyReportRenderer(),
    private val currentDateProvider: () -> LocalDate = { LocalDate.now() }
) : ViewModel() {
    private val _uiState = MutableStateFlow(BusinessReportUiState(isLoading = true))
    val uiState: StateFlow<BusinessReportUiState> = _uiState.asStateFlow()

    init {
        refresh()
    }

    fun selectPeriod(period: BusinessReportPeriod) {
        if (_uiState.value.selectedPeriod == period) return
        _uiState.value = _uiState.value.copy(selectedPeriod = period, successMessage = null, errorMessage = null)
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            val selectedPeriod = _uiState.value.selectedPeriod
            _uiState.value = _uiState.value.copy(isLoading = true, successMessage = null, errorMessage = null)
            runCatching {
                val report = buildReport(selectedPeriod)
                BusinessReportUiState(
                    isLoading = false,
                    selectedPeriod = selectedPeriod,
                    report = report,
                    exportReadyReport = report.exportReadyReport,
                    snapshots = snapshotRepository.listSnapshots()
                )
            }.onSuccess { nextState ->
                _uiState.value = nextState
            }.onFailure { error ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = error.message ?: "Failed to generate business report."
                )
            }
        }
    }

    fun saveCurrentSnapshot() {
        val report = _uiState.value.report ?: return
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isSavingSnapshot = true, successMessage = null, errorMessage = null)
            runCatching {
                snapshotRepository.saveSnapshot(report.toSnapshot())
                snapshotRepository.listSnapshots()
            }.onSuccess { snapshots ->
                _uiState.value = _uiState.value.copy(
                    isSavingSnapshot = false,
                    snapshots = snapshots,
                    successMessage = "Business report snapshot saved locally."
                )
            }.onFailure { error ->
                _uiState.value = _uiState.value.copy(
                    isSavingSnapshot = false,
                    errorMessage = error.message ?: "Failed to save report snapshot."
                )
            }
        }
    }

    private suspend fun buildReport(period: BusinessReportPeriod): BusinessReport {
        val profile = businessProfileRepository.getActiveBusinessProfile()
        val entries = financialEntryRepository.listEntries()
        val monthPrefix = currentDateProvider().toString().take(7)
        val financialSummary = financialEntryRepository.getFinancialSummary(
            monthPrefix = monthPrefix,
            targetMonthlyRevenue = profile?.draft?.targetMonthlyRevenue,
            targetMonthlyProfit = profile?.draft?.targetMonthlyProfit
        )
        val diagnosis = diagnosisEngine.diagnose(profile, financialSummary)
        val report = reportGenerator.generate(
            period = period,
            input = BusinessReportInput(
                profile = profile,
                financialEntries = entries,
                diagnosis = diagnosis,
                activeWeeklyPlan = weeklyPlanRepository.getActivePlan(),
                contentIdeas = contentIdeaRepository.listIdeas(),
                contentCalendarItems = contentCalendarRepository.listSchedules(),
                progressSnapshots = weeklyProgressHistoryRepository.listSnapshots(),
                latestRetrospective = weeklyRetrospectiveRepository.getLatestRetrospective()
            )
        )
        val exportReadyReport = reportRenderer.render(report)
        return report.copy(exportReadyReport = exportReadyReport)
    }

    private fun BusinessReport.toSnapshot(): BusinessReportSnapshot = BusinessReportSnapshot(
        period = period,
        businessName = businessName,
        generatedAt = generatedAt,
        headlineSummary = insights.firstOrNull()?.message ?: "Business report generated from local UsahaNaik data.",
        exportReadyText = exportReadyReport.body,
        healthScore = diagnosisSummary.healthScore,
        totalRevenue = financialSummary.totalRevenue,
        totalExpenses = financialSummary.totalExpenses,
        estimatedProfit = financialSummary.estimatedProfit,
        taskCompletionRate = weeklyExecution.taskCompletionRate,
        contentExecutionRate = contentPerformance.executionRate
    )
}

class BusinessReportViewModelFactory(
    private val businessProfileRepository: BusinessProfileRepository,
    private val financialEntryRepository: FinancialEntryRepository,
    private val weeklyPlanRepository: WeeklyPlanRepository,
    private val contentIdeaRepository: ContentIdeaRepository,
    private val contentCalendarRepository: ContentCalendarRepository,
    private val weeklyProgressHistoryRepository: WeeklyProgressHistoryRepository,
    private val weeklyRetrospectiveRepository: WeeklyRetrospectiveRepository,
    private val snapshotRepository: BusinessReportSnapshotRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BusinessReportViewModel::class.java)) {
            return BusinessReportViewModel(
                businessProfileRepository = businessProfileRepository,
                financialEntryRepository = financialEntryRepository,
                weeklyPlanRepository = weeklyPlanRepository,
                contentIdeaRepository = contentIdeaRepository,
                contentCalendarRepository = contentCalendarRepository,
                weeklyProgressHistoryRepository = weeklyProgressHistoryRepository,
                weeklyRetrospectiveRepository = weeklyRetrospectiveRepository,
                snapshotRepository = snapshotRepository
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}
