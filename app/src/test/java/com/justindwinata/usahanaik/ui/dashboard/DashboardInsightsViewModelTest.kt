package com.justindwinata.usahanaik.ui.dashboard

import com.justindwinata.usahanaik.data.repository.BusinessProfileRepository
import com.justindwinata.usahanaik.data.repository.FinancialEntryRepository
import com.justindwinata.usahanaik.domain.finance.FinancialCalculator
import com.justindwinata.usahanaik.domain.model.AvailableTime
import com.justindwinata.usahanaik.domain.model.BusinessProfile
import com.justindwinata.usahanaik.domain.model.BusinessSetupDraft
import com.justindwinata.usahanaik.domain.model.FinancialEntry
import com.justindwinata.usahanaik.domain.model.FinancialEntryType
import com.justindwinata.usahanaik.domain.model.FinancialTrackingSummary
import com.justindwinata.usahanaik.domain.model.MonthlyFocus
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
class DashboardInsightsViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun loadsSavedProfileAndGeneratesDiagnosis() = runTest {
        val profileRepository = FakeBusinessProfileRepository(sampleProfile())
        val financialRepository = FakeFinancialEntryRepository(
            entries = listOf(
                FinancialEntry(
                    type = FinancialEntryType.Income,
                    title = "Sales",
                    amount = 10_000_000L,
                    category = "Product sales",
                    date = "2026-07-19"
                ),
                FinancialEntry(
                    type = FinancialEntryType.Expense,
                    title = "Stock",
                    amount = 5_000_000L,
                    category = "Raw materials",
                    date = "2026-07-19"
                )
            )
        )

        val viewModel = DashboardInsightsViewModel(profileRepository, financialRepository)
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertNotNull(state.profile)
        assertNotNull(state.diagnosis)
        assertEquals(10_000_000L, state.financialSummary.totalIncome)
        assertTrue(state.diagnosis!!.priorityActions.size in 3..5)
    }

    @Test
    fun handlesNoProfileAsEmptyState() = runTest {
        val viewModel = DashboardInsightsViewModel(
            businessProfileRepository = FakeBusinessProfileRepository(null),
            financialEntryRepository = FakeFinancialEntryRepository()
        )
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertEquals("Complete business setup first", state.diagnosis!!.insights.first().title)
        assertTrue(state.emptyStateMessage!!.contains("Complete your business setup"))
    }

    @Test
    fun refreshRecomputesDiagnosisAfterFinancialEntriesChange() = runTest {
        val financialRepository = FakeFinancialEntryRepository()
        val viewModel = DashboardInsightsViewModel(
            businessProfileRepository = FakeBusinessProfileRepository(sampleProfile()),
            financialEntryRepository = financialRepository
        )
        advanceUntilIdle()
        assertFalse(viewModel.uiState.value.financialSummary.hasEntries)

        financialRepository.addEntry(
            FinancialEntry(
                type = FinancialEntryType.Income,
                title = "Sales",
                amount = 4_000_000L,
                category = "Product sales",
                date = "2026-07-19"
            )
        )
        viewModel.refresh()
        advanceUntilIdle()

        assertTrue(viewModel.uiState.value.financialSummary.hasEntries)
        assertEquals(4_000_000L, viewModel.uiState.value.financialSummary.totalIncome)
    }

    private fun sampleProfile(): BusinessProfile {
        return BusinessProfile(
            draft = BusinessSetupDraft(
                businessName = "Toko Maju",
                categoryId = "food_beverage",
                targetMonthlyRevenue = "Rp 12.000.000",
                targetMonthlyProfit = "Rp 4.000.000",
                mainFocus = MonthlyFocus.ImproveSales,
                availableTime = AvailableTime.SixToTenHours
            ),
            createdAt = 1L,
            updatedAt = 1L
        )
    }

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
        entries: List<FinancialEntry> = emptyList()
    ) : FinancialEntryRepository {
        private val entries = MutableStateFlow(entries)
        private var nextId = 1L

        override suspend fun addEntry(entry: FinancialEntry): FinancialEntry {
            val saved = entry.copy(id = nextId++)
            entries.value = entries.value + saved
            return saved
        }

        override suspend fun updateEntry(entry: FinancialEntry): FinancialEntry {
            entries.value = entries.value.map { if (it.id == entry.id) entry else it }
            return entry
        }

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
        ): FinancialTrackingSummary {
            return FinancialCalculator.buildSummary(
                entries = listEntriesForMonth(monthPrefix),
                targetMonthlyRevenue = targetMonthlyRevenue,
                targetMonthlyProfit = targetMonthlyProfit
            )
        }
    }
}
