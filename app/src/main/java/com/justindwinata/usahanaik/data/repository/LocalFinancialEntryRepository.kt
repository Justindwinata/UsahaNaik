package com.justindwinata.usahanaik.data.repository

import com.justindwinata.usahanaik.data.local.FinancialEntryDao
import com.justindwinata.usahanaik.data.mapper.FinancialEntryMapper.toDomain
import com.justindwinata.usahanaik.data.mapper.FinancialEntryMapper.toEntity
import com.justindwinata.usahanaik.domain.finance.FinancialCalculator
import com.justindwinata.usahanaik.domain.model.FinancialEntry
import com.justindwinata.usahanaik.domain.model.FinancialTrackingSummary
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LocalFinancialEntryRepository(
    private val dao: FinancialEntryDao,
    private val nowProvider: () -> Long = { System.currentTimeMillis() }
) : FinancialEntryRepository {
    override suspend fun addEntry(entry: FinancialEntry): FinancialEntry {
        val now = nowProvider()
        val entity = entry.copy(createdAt = now, updatedAt = now).toEntity()
        val id = dao.insertEntry(entity)
        return entity.copy(id = id).toDomain()
    }

    override suspend fun updateEntry(entry: FinancialEntry): FinancialEntry {
        val now = nowProvider()
        val existing = dao.getEntry(entry.id)
        val entity = entry.copy(
            createdAt = existing?.createdAt ?: entry.createdAt,
            updatedAt = now
        ).toEntity()
        dao.updateEntry(entity)
        return entity.toDomain()
    }

    override suspend fun deleteEntry(id: Long) {
        dao.deleteEntryById(id)
    }

    override suspend fun getEntry(id: Long): FinancialEntry? = dao.getEntry(id)?.toDomain()

    override suspend fun listEntries(): List<FinancialEntry> = dao.listEntries().map { it.toDomain() }

    override fun observeEntries(): Flow<List<FinancialEntry>> = dao.observeEntries().map { entries ->
        entries.map { it.toDomain() }
    }

    override suspend fun listEntriesForMonth(monthPrefix: String): List<FinancialEntry> {
        return dao.listEntriesForMonth(monthPrefix).map { it.toDomain() }
    }

    override fun observeEntriesForMonth(monthPrefix: String): Flow<List<FinancialEntry>> {
        return dao.observeEntriesForMonth(monthPrefix).map { entries -> entries.map { it.toDomain() } }
    }

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
