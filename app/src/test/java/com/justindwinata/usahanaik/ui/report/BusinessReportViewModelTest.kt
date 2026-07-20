package com.justindwinata.usahanaik.ui.report

import com.justindwinata.usahanaik.data.repository.BusinessProfileRepository
import com.justindwinata.usahanaik.data.repository.BusinessReportSnapshotRepository
import com.justindwinata.usahanaik.data.repository.ContentCalendarRepository
import com.justindwinata.usahanaik.data.repository.ContentIdeaRepository
import com.justindwinata.usahanaik.data.repository.FinancialEntryRepository
import com.justindwinata.usahanaik.data.repository.WeeklyPlanRepository
import com.justindwinata.usahanaik.data.repository.WeeklyProgressHistoryRepository
import com.justindwinata.usahanaik.data.repository.WeeklyRetrospectiveRepository
import com.justindwinata.usahanaik.domain.finance.FinancialCalculator
import com.justindwinata.usahanaik.domain.model.AvailableTime
import com.justindwinata.usahanaik.domain.model.BusinessProfile
import com.justindwinata.usahanaik.domain.model.BusinessReportPeriod
import com.justindwinata.usahanaik.domain.model.BusinessReportSnapshot
import com.justindwinata.usahanaik.domain.model.BusinessSetupDraft
import com.justindwinata.usahanaik.domain.model.ContentCalendarSchedule
import com.justindwinata.usahanaik.domain.model.ContentCalendarStatus
import com.justindwinata.usahanaik.domain.model.ContentCalendarSummary
import com.justindwinata.usahanaik.domain.model.ContentIdea
import com.justindwinata.usahanaik.domain.model.ContentIdeaFilter
import com.justindwinata.usahanaik.domain.model.ContentIdeaStatus
import com.justindwinata.usahanaik.domain.model.FinancialEntry
import com.justindwinata.usahanaik.domain.model.FinancialEntryType
import com.justindwinata.usahanaik.domain.model.FinancialTrackingSummary
import com.justindwinata.usahanaik.domain.model.MilestoneStatus
import com.justindwinata.usahanaik.domain.model.MonthlyFocus
import com.justindwinata.usahanaik.domain.model.WeeklyGrowthPlan
import com.justindwinata.usahanaik.domain.model.WeeklyProgressHistorySummary
import com.justindwinata.usahanaik.domain.model.WeeklyProgressSnapshot
import com.justindwinata.usahanaik.domain.model.WeeklyRetrospective
import com.justindwinata.usahanaik.domain.model.WeeklyTaskStatus
import com.justindwinata.usahanaik.domain.report.BusinessReportGenerator
import com.justindwinata.usahanaik.test.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import java.time.LocalDate

@OptIn(ExperimentalCoroutinesApi::class)
class BusinessReportViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun loadsEmptyStateWithoutProfile() = runTest {
        val viewModel = createViewModel(profile = null)
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertEquals("Complete business setup first.", state.emptyStateMessage)
        assertEquals("Complete business setup first", state.report?.businessName)
    }

    @Test
    fun changesPeriodAndRegeneratesReport() = runTest {
        val viewModel = createViewModel(profile = sampleProfile())
        advanceUntilIdle()

        viewModel.selectPeriod(BusinessReportPeriod.LastThirtyDays)
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals(BusinessReportPeriod.LastThirtyDays, state.selectedPeriod)
        assertEquals("Last 30 days", state.report?.period?.label)
        assertNotNull(state.exportReadyReport)
    }

    @Test
    fun savesCurrentReportSnapshot() = runTest {
        val snapshotRepository = FakeBusinessReportSnapshotRepository()
        val viewModel = createViewModel(
            profile = sampleProfile(),
            financialEntries = listOf(sampleIncome()),
            snapshotRepository = snapshotRepository
        )
        advanceUntilIdle()

        viewModel.saveCurrentSnapshot()
        advanceUntilIdle()

        assertEquals(1, snapshotRepository.listSnapshots().size)
        assertTrue(viewModel.uiState.value.successMessage!!.contains("saved locally"))
    }

    private fun createViewModel(
        profile: BusinessProfile?,
        financialEntries: List<FinancialEntry> = emptyList(),
        snapshotRepository: FakeBusinessReportSnapshotRepository = FakeBusinessReportSnapshotRepository()
    ): BusinessReportViewModel = BusinessReportViewModel(
        businessProfileRepository = FakeBusinessProfileRepository(profile),
        financialEntryRepository = FakeFinancialEntryRepository(financialEntries),
        weeklyPlanRepository = FakeWeeklyPlanRepository(),
        contentIdeaRepository = FakeContentIdeaRepository(),
        contentCalendarRepository = FakeContentCalendarRepository(),
        weeklyProgressHistoryRepository = FakeWeeklyProgressHistoryRepository(),
        weeklyRetrospectiveRepository = FakeWeeklyRetrospectiveRepository(),
        snapshotRepository = snapshotRepository,
        reportGenerator = BusinessReportGenerator(currentDateProvider = { LocalDate.parse("2026-07-20") }),
        currentDateProvider = { LocalDate.parse("2026-07-20") }
    )

    private fun sampleProfile(): BusinessProfile = BusinessProfile(
        draft = BusinessSetupDraft(
            businessName = "Kedai Naik",
            categoryId = "food_beverage",
            targetMonthlyRevenue = "Rp 10.000.000",
            targetMonthlyProfit = "Rp 3.000.000",
            mainFocus = MonthlyFocus.ImproveSales,
            availableTime = AvailableTime.ThreeToFiveHours
        ),
        createdAt = 1L,
        updatedAt = 1L
    )

    private fun sampleIncome(): FinancialEntry = FinancialEntry(
        type = FinancialEntryType.Income,
        title = "Sales",
        amount = 2_000_000,
        category = "Product sales",
        date = "2026-07-19"
    )

    private class FakeBusinessProfileRepository(initialProfile: BusinessProfile?) : BusinessProfileRepository {
        private val profile = MutableStateFlow(initialProfile)

        override suspend fun saveBusinessProfile(draft: BusinessSetupDraft): BusinessProfile {
            val saved = BusinessProfile(draft = draft, createdAt = 1L, updatedAt = 2L)
            profile.value = saved
            return saved
        }

        override suspend fun getActiveBusinessProfile(): BusinessProfile? = profile.value
        override fun observeActiveBusinessProfile(): Flow<BusinessProfile?> = profile
        override suspend fun deleteBusinessProfile() {
            profile.value = null
        }
        override suspend fun hasBusinessProfile(): Boolean = profile.value != null
    }

    private class FakeFinancialEntryRepository(
        initialEntries: List<FinancialEntry>
    ) : FinancialEntryRepository {
        private val entries = MutableStateFlow(initialEntries)

        override suspend fun addEntry(entry: FinancialEntry): FinancialEntry {
            entries.value = entries.value + entry
            return entry
        }
        override suspend fun updateEntry(entry: FinancialEntry): FinancialEntry = entry
        override suspend fun deleteEntry(id: Long) {
            entries.value = entries.value.filterNot { it.id == id }
        }
        override suspend fun getEntry(id: Long): FinancialEntry? = entries.value.firstOrNull { it.id == id }
        override suspend fun listEntries(): List<FinancialEntry> = entries.value
        override fun observeEntries(): Flow<List<FinancialEntry>> = entries
        override suspend fun listEntriesForMonth(monthPrefix: String): List<FinancialEntry> {
            return entries.value.filter { it.date.startsWith(monthPrefix) }
        }
        override fun observeEntriesForMonth(monthPrefix: String): Flow<List<FinancialEntry>> = entries
        override suspend fun getFinancialSummary(
            monthPrefix: String,
            targetMonthlyRevenue: String?,
            targetMonthlyProfit: String?
        ): FinancialTrackingSummary = FinancialCalculator.buildSummary(
            entries = listEntriesForMonth(monthPrefix),
            targetMonthlyRevenue = targetMonthlyRevenue,
            targetMonthlyProfit = targetMonthlyProfit
        )
    }

    private class FakeWeeklyPlanRepository : WeeklyPlanRepository {
        override suspend fun savePlan(plan: WeeklyGrowthPlan): WeeklyGrowthPlan = plan
        override suspend fun getActivePlan(): WeeklyGrowthPlan? = null
        override fun observeActivePlan(): Flow<WeeklyGrowthPlan?> = MutableStateFlow(null)
        override suspend fun updateTaskStatus(taskId: String, status: WeeklyTaskStatus): WeeklyGrowthPlan? = null
        override suspend fun updateMilestoneStatus(milestoneId: String, status: MilestoneStatus): WeeklyGrowthPlan? = null
        override suspend fun deleteActivePlan() = Unit
        override suspend fun hasActivePlan(): Boolean = false
    }

    private class FakeContentIdeaRepository : ContentIdeaRepository {
        override suspend fun saveIdea(idea: ContentIdea): ContentIdea = idea
        override suspend fun listIdeas(filter: ContentIdeaFilter): List<ContentIdea> = emptyList()
        override fun observeIdeas(): Flow<List<ContentIdea>> = MutableStateFlow(emptyList())
        override suspend fun updateStatus(id: Long, status: ContentIdeaStatus): ContentIdea? = null
        override suspend fun updateFavorite(id: Long, isFavorite: Boolean): ContentIdea? = null
        override suspend fun deleteIdea(id: Long) = Unit
        override suspend fun clearIdeas() = Unit
    }

    private class FakeContentCalendarRepository : ContentCalendarRepository {
        override suspend fun scheduleContent(item: ContentCalendarSchedule): ContentCalendarSchedule = item
        override suspend fun getSchedule(id: Long): ContentCalendarSchedule? = null
        override suspend fun listSchedules(): List<ContentCalendarSchedule> = emptyList()
        override fun observeSchedules(): Flow<List<ContentCalendarSchedule>> = MutableStateFlow(emptyList())
        override suspend fun listUpcoming(fromDate: String): List<ContentCalendarSchedule> = emptyList()
        override suspend fun listBetween(startDate: String, endDate: String): List<ContentCalendarSchedule> = emptyList()
        override suspend fun updateStatus(id: Long, status: ContentCalendarStatus): ContentCalendarSchedule? = null
        override suspend fun deleteSchedule(id: Long) = Unit
        override suspend fun getSummary(startDate: String?, endDate: String?): ContentCalendarSummary = ContentCalendarSummary(
            totalScheduled = 0,
            plannedCount = 0,
            postedCount = 0,
            skippedCount = 0,
            doneCount = 0,
            nextScheduledTitle = "No scheduled content yet",
            nextScheduledDate = "-",
            executionRate = 0f
        )
    }

    private class FakeWeeklyProgressHistoryRepository : WeeklyProgressHistoryRepository {
        override suspend fun saveSnapshot(snapshot: WeeklyProgressSnapshot): WeeklyProgressSnapshot = snapshot
        override suspend fun getSnapshotForWeek(weekLabel: String): WeeklyProgressSnapshot? = null
        override suspend fun listSnapshots(): List<WeeklyProgressSnapshot> = emptyList()
        override fun observeSnapshots(): Flow<List<WeeklyProgressSnapshot>> = MutableStateFlow(emptyList())
        override suspend fun clearSnapshots() = Unit
        override suspend fun getHistorySummary(): WeeklyProgressHistorySummary = WeeklyProgressHistorySummary(
            latestSnapshot = null,
            trendPoints = emptyList(),
            averageTaskCompletionRate = 0f
        )
    }

    private class FakeWeeklyRetrospectiveRepository : WeeklyRetrospectiveRepository {
        override suspend fun saveRetrospective(retrospective: WeeklyRetrospective): WeeklyRetrospective = retrospective
        override suspend fun getRetrospectiveForWeek(weekLabel: String): WeeklyRetrospective? = null
        override suspend fun getLatestRetrospective(): WeeklyRetrospective? = null
        override suspend fun listRetrospectives(): List<WeeklyRetrospective> = emptyList()
        override fun observeRetrospectives(): Flow<List<WeeklyRetrospective>> = MutableStateFlow(emptyList())
        override suspend fun clearRetrospectives() = Unit
    }

    private class FakeBusinessReportSnapshotRepository : BusinessReportSnapshotRepository {
        private val snapshots = MutableStateFlow<List<BusinessReportSnapshot>>(emptyList())

        override suspend fun saveSnapshot(snapshot: BusinessReportSnapshot): BusinessReportSnapshot {
            val saved = snapshot.copy(id = snapshots.value.size + 1L)
            snapshots.value = snapshots.value + saved
            return saved
        }
        override suspend fun getSnapshot(id: Long): BusinessReportSnapshot? = snapshots.value.firstOrNull { it.id == id }
        override suspend fun getLatestSnapshot(): BusinessReportSnapshot? = snapshots.value.lastOrNull()
        override suspend fun listSnapshots(): List<BusinessReportSnapshot> = snapshots.value
        override fun observeSnapshots(): Flow<List<BusinessReportSnapshot>> = snapshots
        override suspend fun deleteSnapshot(id: Long) {
            snapshots.value = snapshots.value.filterNot { it.id == id }
        }
        override suspend fun clearSnapshots() {
            snapshots.value = emptyList()
        }
    }
}
