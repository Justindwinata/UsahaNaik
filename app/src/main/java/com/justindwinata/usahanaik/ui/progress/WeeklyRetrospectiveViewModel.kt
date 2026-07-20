package com.justindwinata.usahanaik.ui.progress

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.justindwinata.usahanaik.data.repository.BusinessProfileRepository
import com.justindwinata.usahanaik.data.repository.ContentCalendarRepository
import com.justindwinata.usahanaik.data.repository.ContentIdeaRepository
import com.justindwinata.usahanaik.data.repository.FinancialEntryRepository
import com.justindwinata.usahanaik.data.repository.WeeklyPlanRepository
import com.justindwinata.usahanaik.data.repository.WeeklyProgressHistoryRepository
import com.justindwinata.usahanaik.data.repository.WeeklyRetrospectiveRepository
import com.justindwinata.usahanaik.domain.diagnosis.BusinessDiagnosisEngine
import com.justindwinata.usahanaik.domain.model.ContentIdeaFilter
import com.justindwinata.usahanaik.domain.progress.WeeklyProgressSnapshotGenerator
import com.justindwinata.usahanaik.domain.progress.WeeklyRetrospectiveGenerator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class WeeklyRetrospectiveViewModel(
    private val businessProfileRepository: BusinessProfileRepository,
    private val financialEntryRepository: FinancialEntryRepository,
    private val weeklyPlanRepository: WeeklyPlanRepository,
    private val contentIdeaRepository: ContentIdeaRepository,
    private val contentCalendarRepository: ContentCalendarRepository,
    private val progressHistoryRepository: WeeklyProgressHistoryRepository,
    private val retrospectiveRepository: WeeklyRetrospectiveRepository,
    private val snapshotGenerator: WeeklyProgressSnapshotGenerator = WeeklyProgressSnapshotGenerator(),
    private val retrospectiveGenerator: WeeklyRetrospectiveGenerator = WeeklyRetrospectiveGenerator(),
    private val diagnosisEngine: BusinessDiagnosisEngine = BusinessDiagnosisEngine(),
    private val dateProvider: () -> Calendar = { Calendar.getInstance(Locale.US) }
) : ViewModel() {
    private val _uiState = MutableStateFlow(WeeklyRetrospectiveUiState(isLoading = true))
    val uiState: StateFlow<WeeklyRetrospectiveUiState> = _uiState.asStateFlow()

    init {
        load()
    }

    fun load() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = false,
                latestRetrospective = retrospectiveRepository.getLatestRetrospective(),
                history = retrospectiveRepository.listRetrospectives(),
                progressHistorySummary = progressHistoryRepository.getHistorySummary(),
                errorMessage = null
            )
        }
    }

    fun generateAndSaveRetrospective() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isGenerating = true, errorMessage = null, successMessage = null)
            runCatching {
                val profile = businessProfileRepository.getActiveBusinessProfile()
                val activePlan = weeklyPlanRepository.getActivePlan()
                val weekInfo = currentWeekInfo()
                val financialSummary = financialEntryRepository.getFinancialSummary(
                    monthPrefix = weekInfo.monthPrefix,
                    targetMonthlyRevenue = profile?.draft?.targetMonthlyRevenue,
                    targetMonthlyProfit = profile?.draft?.targetMonthlyProfit
                )
                val contentSummary = contentCalendarRepository.getSummary()
                val savedIdeasCount = contentIdeaRepository.listIdeas(ContentIdeaFilter.All).size
                val diagnosis = profile?.let {
                    diagnosisEngine.diagnose(
                        profile = it,
                        financialSummary = financialSummary
                    )
                }
                val snapshot = snapshotGenerator.generate(
                    weekLabel = weekInfo.weekLabel,
                    weekStartDate = weekInfo.weekStartDate,
                    activePlan = activePlan,
                    financialSummary = financialSummary,
                    contentCalendarSummary = contentSummary,
                    savedIdeasCount = savedIdeasCount,
                    diagnosis = diagnosis
                )
                val savedSnapshot = progressHistoryRepository.saveSnapshot(snapshot)
                val retrospective = retrospectiveGenerator.generate(
                    weekLabel = weekInfo.weekLabel,
                    generatedDate = weekInfo.generatedDate,
                    snapshot = savedSnapshot,
                    activePlan = activePlan
                )
                retrospectiveRepository.saveRetrospective(retrospective)
                savedSnapshot
            }.onSuccess { snapshot ->
                _uiState.value = _uiState.value.copy(
                    isGenerating = false,
                    currentSnapshot = snapshot,
                    latestRetrospective = retrospectiveRepository.getLatestRetrospective(),
                    history = retrospectiveRepository.listRetrospectives(),
                    progressHistorySummary = progressHistoryRepository.getHistorySummary(),
                    successMessage = "Weekly retrospective saved locally."
                )
            }.onFailure { error ->
                _uiState.value = _uiState.value.copy(
                    isGenerating = false,
                    errorMessage = error.message ?: "Failed to generate retrospective."
                )
            }
        }
    }

    private fun currentWeekInfo(): WeekInfo {
        val calendar = dateProvider()
        val generatedDate = DATE_FORMAT.format(calendar.time)
        val monthPrefix = generatedDate.take(7)
        val weekNumber = calendar.get(Calendar.WEEK_OF_YEAR)
        val year = calendar.get(Calendar.YEAR)
        val weekStart = calendar.clone() as Calendar
        weekStart.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
        return WeekInfo(
            weekLabel = "Week $weekNumber, $year",
            weekStartDate = DATE_FORMAT.format(weekStart.time),
            generatedDate = generatedDate,
            monthPrefix = monthPrefix
        )
    }

    private data class WeekInfo(
        val weekLabel: String,
        val weekStartDate: String,
        val generatedDate: String,
        val monthPrefix: String
    )

    companion object {
        private val DATE_FORMAT = SimpleDateFormat("yyyy-MM-dd", Locale.US)
    }
}

class WeeklyRetrospectiveViewModelFactory(
    private val businessProfileRepository: BusinessProfileRepository,
    private val financialEntryRepository: FinancialEntryRepository,
    private val weeklyPlanRepository: WeeklyPlanRepository,
    private val contentIdeaRepository: ContentIdeaRepository,
    private val contentCalendarRepository: ContentCalendarRepository,
    private val progressHistoryRepository: WeeklyProgressHistoryRepository,
    private val retrospectiveRepository: WeeklyRetrospectiveRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WeeklyRetrospectiveViewModel::class.java)) {
            return WeeklyRetrospectiveViewModel(
                businessProfileRepository = businessProfileRepository,
                financialEntryRepository = financialEntryRepository,
                weeklyPlanRepository = weeklyPlanRepository,
                contentIdeaRepository = contentIdeaRepository,
                contentCalendarRepository = contentCalendarRepository,
                progressHistoryRepository = progressHistoryRepository,
                retrospectiveRepository = retrospectiveRepository
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}
