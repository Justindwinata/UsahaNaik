package com.justindwinata.usahanaik.ui.weekly

import com.justindwinata.usahanaik.data.repository.BusinessProfileRepository
import com.justindwinata.usahanaik.data.repository.FinancialEntryRepository
import com.justindwinata.usahanaik.data.repository.WeeklyPlanRepository
import com.justindwinata.usahanaik.domain.finance.FinancialCalculator
import com.justindwinata.usahanaik.domain.model.AvailableTime
import com.justindwinata.usahanaik.domain.model.BusinessProfile
import com.justindwinata.usahanaik.domain.model.BusinessSetupDraft
import com.justindwinata.usahanaik.domain.model.FinancialEntry
import com.justindwinata.usahanaik.domain.model.FinancialEntryType
import com.justindwinata.usahanaik.domain.model.FinancialTrackingSummary
import com.justindwinata.usahanaik.domain.model.MilestoneStatus
import com.justindwinata.usahanaik.domain.model.WeeklyGrowthPlan
import com.justindwinata.usahanaik.domain.model.WeeklyTaskStatus
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

@OptIn(ExperimentalCoroutinesApi::class)
class WeeklyPlanViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun showsEmptyStateWithoutProfile() = runTest {
        val viewModel = WeeklyPlanViewModel(
            businessProfileRepository = FakeBusinessProfileRepository(null),
            financialEntryRepository = FakeFinancialEntryRepository(),
            weeklyPlanRepository = FakeWeeklyPlanRepository()
        )
        advanceUntilIdle()

        assertFalse(viewModel.uiState.value.isLoading)
        assertEquals("Complete business setup first.", viewModel.uiState.value.emptyStateMessage)
    }

    @Test
    fun generatePlanActionSavesActivePlan() = runTest {
        val planRepository = FakeWeeklyPlanRepository()
        val viewModel = WeeklyPlanViewModel(
            businessProfileRepository = FakeBusinessProfileRepository(sampleProfile()),
            financialEntryRepository = FakeFinancialEntryRepository(sampleEntries()),
            weeklyPlanRepository = planRepository
        )
        advanceUntilIdle()

        viewModel.generatePlan()
        advanceUntilIdle()

        assertNotNull(viewModel.uiState.value.activePlan)
        assertEquals(1, planRepository.saveCount)
        assertTrue(viewModel.uiState.value.activePlan!!.tasks.size in 5..7)
    }

    @Test
    fun togglesTaskCompletionAndUpdatesProgress() = runTest {
        val planRepository = FakeWeeklyPlanRepository()
        val viewModel = WeeklyPlanViewModel(
            businessProfileRepository = FakeBusinessProfileRepository(sampleProfile()),
            financialEntryRepository = FakeFinancialEntryRepository(sampleEntries()),
            weeklyPlanRepository = planRepository
        )
        advanceUntilIdle()
        viewModel.generatePlan()
        advanceUntilIdle()
        val firstTaskId = viewModel.uiState.value.activePlan!!.tasks.first().id

        viewModel.toggleTaskCompletion(firstTaskId)
        advanceUntilIdle()

        val updatedPlan = viewModel.uiState.value.activePlan!!
        assertEquals(WeeklyTaskStatus.Completed, updatedPlan.tasks.first().status)
        assertTrue(updatedPlan.progressSummary.taskProgress > 0f)
    }

    @Test
    fun existingPlanRequiresRegenerateConfirmation() = runTest {
        val viewModel = WeeklyPlanViewModel(
            businessProfileRepository = FakeBusinessProfileRepository(sampleProfile()),
            financialEntryRepository = FakeFinancialEntryRepository(sampleEntries()),
            weeklyPlanRepository = FakeWeeklyPlanRepository()
        )
        advanceUntilIdle()
        viewModel.generatePlan()
        advanceUntilIdle()

        viewModel.generatePlan()

        assertTrue(viewModel.uiState.value.showRegenerateConfirmation)
    }

    private fun sampleProfile(): BusinessProfile {
        return BusinessProfile(
            draft = BusinessSetupDraft(
                businessName = "Toko Maju",
                categoryId = "food_beverage",
                targetMonthlyRevenue = "Rp 12.000.000",
                targetMonthlyProfit = "Rp 4.000.000",
                availableTime = AvailableTime.SixToTenHours
            ),
            createdAt = 1L,
            updatedAt = 1L
        )
    }

    private fun sampleEntries(): List<FinancialEntry> {
        return listOf(
            FinancialEntry(
                type = FinancialEntryType.Income,
                title = "Sales",
                amount = 8_000_000L,
                category = "Product sales",
                date = "2026-07-19"
            ),
            FinancialEntry(
                type = FinancialEntryType.Expense,
                title = "Stock",
                amount = 4_000_000L,
                category = "Raw materials",
                date = "2026-07-19"
            )
        )
    }

    private class FakeBusinessProfileRepository(initialProfile: BusinessProfile?) : BusinessProfileRepository {
        private val profile = MutableStateFlow(initialProfile)

        override suspend fun saveBusinessProfile(draft: BusinessSetupDraft): BusinessProfile {
            val saved = BusinessProfile(draft, 1L, 1L)
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
        entries: List<FinancialEntry> = emptyList()
    ) : FinancialEntryRepository {
        private val entries = MutableStateFlow(entries)

        override suspend fun addEntry(entry: FinancialEntry): FinancialEntry = entry

        override suspend fun updateEntry(entry: FinancialEntry): FinancialEntry = entry

        override suspend fun deleteEntry(id: Long) = Unit

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
        ): FinancialTrackingSummary {
            return FinancialCalculator.buildSummary(
                entries = listEntriesForMonth(monthPrefix),
                targetMonthlyRevenue = targetMonthlyRevenue,
                targetMonthlyProfit = targetMonthlyProfit
            )
        }
    }

    private class FakeWeeklyPlanRepository : WeeklyPlanRepository {
        private val plan = MutableStateFlow<WeeklyGrowthPlan?>(null)
        var saveCount = 0

        override suspend fun savePlan(plan: WeeklyGrowthPlan): WeeklyGrowthPlan {
            saveCount += 1
            this.plan.value = plan
            return plan
        }

        override suspend fun getActivePlan(): WeeklyGrowthPlan? = plan.value

        override fun observeActivePlan(): Flow<WeeklyGrowthPlan?> = plan

        override suspend fun updateTaskStatus(taskId: String, status: WeeklyTaskStatus): WeeklyGrowthPlan? {
            val current = plan.value ?: return null
            val tasks = current.tasks.map { if (it.id == taskId) it.copy(status = status) else it }
            val completedIds = tasks.filter { it.status == WeeklyTaskStatus.Completed }.map { it.id }.toSet()
            val milestones = current.milestones.map { milestone ->
                val related = milestone.relatedTaskIds
                val progress = if (related.isEmpty()) 0 else ((related.count { it in completedIds }.toFloat() / related.size) * 100).toInt()
                milestone.copy(
                    progressPercentage = progress,
                    status = when {
                        progress >= 100 -> MilestoneStatus.Completed
                        progress > 0 -> MilestoneStatus.InProgress
                        else -> MilestoneStatus.NotStarted
                    }
                )
            }
            plan.value = current.copy(tasks = tasks, milestones = milestones)
            return plan.value
        }

        override suspend fun updateMilestoneStatus(milestoneId: String, status: MilestoneStatus): WeeklyGrowthPlan? {
            val current = plan.value ?: return null
            plan.value = current.copy(
                milestones = current.milestones.map { if (it.id == milestoneId) it.copy(status = status) else it }
            )
            return plan.value
        }

        override suspend fun deleteActivePlan() {
            plan.value = null
        }

        override suspend fun hasActivePlan(): Boolean = plan.value != null
    }
}
