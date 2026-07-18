package com.justindwinata.usahanaik.data.repository

import com.justindwinata.usahanaik.domain.model.FinancialEntry
import com.justindwinata.usahanaik.domain.model.FinancialTrackingSummary
import kotlinx.coroutines.flow.Flow

interface FinancialEntryRepository {
    suspend fun addEntry(entry: FinancialEntry): FinancialEntry

    suspend fun updateEntry(entry: FinancialEntry): FinancialEntry

    suspend fun deleteEntry(id: Long)

    suspend fun getEntry(id: Long): FinancialEntry?

    suspend fun listEntries(): List<FinancialEntry>

    fun observeEntries(): Flow<List<FinancialEntry>>

    suspend fun listEntriesForMonth(monthPrefix: String): List<FinancialEntry>

    fun observeEntriesForMonth(monthPrefix: String): Flow<List<FinancialEntry>>

    suspend fun getFinancialSummary(
        monthPrefix: String,
        targetMonthlyRevenue: String? = null,
        targetMonthlyProfit: String? = null
    ): FinancialTrackingSummary
}
