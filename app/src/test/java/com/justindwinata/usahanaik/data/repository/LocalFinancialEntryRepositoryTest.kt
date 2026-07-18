package com.justindwinata.usahanaik.data.repository

import com.justindwinata.usahanaik.data.local.FinancialEntryDao
import com.justindwinata.usahanaik.data.local.FinancialEntryEntity
import com.justindwinata.usahanaik.domain.model.FinancialEntry
import com.justindwinata.usahanaik.domain.model.FinancialEntryType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class LocalFinancialEntryRepositoryTest {
    @Test
    fun savesIncomeAndExpenseThenListsEntries() = runTest {
        val repository = LocalFinancialEntryRepository(FakeFinancialEntryDao(), nowProvider = { 100L })

        val income = repository.addEntry(sampleEntry(FinancialEntryType.Income, 1_000_000))
        val expense = repository.addEntry(sampleEntry(FinancialEntryType.Expense, 300_000))
        val entries = repository.listEntries()

        assertEquals(1L, income.id)
        assertEquals(2L, expense.id)
        assertEquals(2, entries.size)
    }

    @Test
    fun deletesEntry() = runTest {
        val repository = LocalFinancialEntryRepository(FakeFinancialEntryDao(), nowProvider = { 100L })
        val entry = repository.addEntry(sampleEntry(FinancialEntryType.Income, 1_000_000))

        repository.deleteEntry(entry.id)

        assertNull(repository.getEntry(entry.id))
    }

    @Test
    fun buildsSummaryFromPersistedMonthEntries() = runTest {
        val repository = LocalFinancialEntryRepository(FakeFinancialEntryDao(), nowProvider = { 100L })
        repository.addEntry(sampleEntry(FinancialEntryType.Income, 1_000_000, date = "2026-07-19"))
        repository.addEntry(sampleEntry(FinancialEntryType.Expense, 250_000, date = "2026-07-19"))
        repository.addEntry(sampleEntry(FinancialEntryType.Income, 5_000_000, date = "2026-08-01"))

        val summary = repository.getFinancialSummary("2026-07", "Rp 2.000.000", "Rp 1.000.000")

        assertEquals(1_000_000L, summary.totalIncome)
        assertEquals(250_000L, summary.totalExpenses)
        assertEquals(750_000L, summary.estimatedProfit)
        assertEquals(0.5f, summary.targetRevenueProgress)
    }

    private fun sampleEntry(
        type: FinancialEntryType,
        amount: Long,
        date: String = "2026-07-19"
    ): FinancialEntry = FinancialEntry(
        type = type,
        title = if (type == FinancialEntryType.Income) "Sales" else "Stock",
        amount = amount,
        category = if (type == FinancialEntryType.Income) "Product sales" else "Raw materials",
        date = date
    )

    private class FakeFinancialEntryDao : FinancialEntryDao {
        private val entries = MutableStateFlow<List<FinancialEntryEntity>>(emptyList())
        private var nextId = 1L

        override suspend fun insertEntry(entry: FinancialEntryEntity): Long {
            val id = if (entry.id == 0L) nextId++ else entry.id
            entries.value = entries.value + entry.copy(id = id)
            return id
        }

        override suspend fun updateEntry(entry: FinancialEntryEntity) {
            entries.value = entries.value.map { if (it.id == entry.id) entry else it }
        }

        override suspend fun deleteEntry(entry: FinancialEntryEntity) {
            entries.value = entries.value.filterNot { it.id == entry.id }
        }

        override suspend fun deleteEntryById(id: Long) {
            entries.value = entries.value.filterNot { it.id == id }
        }

        override suspend fun getEntry(id: Long): FinancialEntryEntity? = entries.value.firstOrNull { it.id == id }

        override suspend fun listEntries(): List<FinancialEntryEntity> = entries.value.sortedByDescending { it.date }

        override fun observeEntries(): Flow<List<FinancialEntryEntity>> = entries

        override suspend fun listEntriesForMonth(monthPrefix: String): List<FinancialEntryEntity> {
            return listEntries().filter { it.date.startsWith(monthPrefix) }
        }

        override fun observeEntriesForMonth(monthPrefix: String): Flow<List<FinancialEntryEntity>> = entries
    }
}
