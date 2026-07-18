package com.justindwinata.usahanaik.ui.finance

import com.justindwinata.usahanaik.data.repository.FinancialEntryRepository
import com.justindwinata.usahanaik.domain.finance.FinancialCalculator
import com.justindwinata.usahanaik.domain.model.FinancialEntry
import com.justindwinata.usahanaik.domain.model.FinancialEntryType
import com.justindwinata.usahanaik.domain.model.FinancialTrackingSummary
import com.justindwinata.usahanaik.test.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class FinancialEntryViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun rejectsInvalidAmount() = runTest {
        val repository = FakeFinancialEntryRepository()
        val viewModel = FinancialEntryViewModel(repository)
        advanceUntilIdle()

        viewModel.updateTitle("Sales")
        viewModel.updateAmount("0")
        viewModel.saveEntry()
        advanceUntilIdle()

        assertEquals(0, repository.entries.value.size)
        assertEquals("Amount must be greater than zero.", viewModel.uiState.value.visibleAmountError())
    }

    @Test
    fun savesValidIncomeEntryAndRefreshesSummary() = runTest {
        val repository = FakeFinancialEntryRepository()
        val viewModel = FinancialEntryViewModel(repository)
        advanceUntilIdle()

        viewModel.updateTitle("Product sales")
        viewModel.updateAmount("Rp 1.000.000")
        viewModel.updateCategory("Product sales")
        viewModel.updateDate("2026-07-19")
        viewModel.saveEntry()
        advanceUntilIdle()

        assertEquals(1, repository.entries.value.size)
        assertEquals(1_000_000L, viewModel.uiState.value.summary.totalIncome)
        assertEquals("Financial entry saved locally.", viewModel.uiState.value.successMessage)
        assertFalse(viewModel.uiState.value.hasAttemptedSave)
    }

    @Test
    fun savesExpenseAndDeletesEntry() = runTest {
        val repository = FakeFinancialEntryRepository()
        val viewModel = FinancialEntryViewModel(repository)
        advanceUntilIdle()

        viewModel.updateType(FinancialEntryType.Expense)
        viewModel.updateTitle("Raw materials")
        viewModel.updateAmount("250000")
        viewModel.updateCategory("Raw materials")
        viewModel.saveEntry()
        advanceUntilIdle()
        val entryId = repository.entries.value.first().id

        viewModel.requestDeleteEntry(entryId)
        viewModel.confirmDeleteEntry()
        advanceUntilIdle()

        assertTrue(repository.entries.value.isEmpty())
        assertEquals("Financial entry deleted.", viewModel.uiState.value.successMessage)
    }

    @Test
    fun refreshLoadsExistingEntries() = runTest {
        val repository = FakeFinancialEntryRepository()
        repository.addEntry(sampleEntry(amount = 500_000))
        val viewModel = FinancialEntryViewModel(repository)
        advanceUntilIdle()

        assertEquals(1, viewModel.uiState.value.entries.size)
        assertEquals(500_000L, viewModel.uiState.value.summary.totalIncome)
    }

    private fun sampleEntry(amount: Long): FinancialEntry = FinancialEntry(
        type = FinancialEntryType.Income,
        title = "Sales",
        amount = amount,
        category = "Product sales",
        date = "2026-07-19"
    )

    private class FakeFinancialEntryRepository : FinancialEntryRepository {
        val entries = MutableStateFlow<List<FinancialEntry>>(emptyList())
        private var nextId = 1L

        override suspend fun addEntry(entry: FinancialEntry): FinancialEntry {
            val saved = entry.copy(id = nextId++, createdAt = 100L, updatedAt = 100L)
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
