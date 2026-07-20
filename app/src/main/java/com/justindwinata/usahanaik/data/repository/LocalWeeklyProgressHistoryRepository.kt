package com.justindwinata.usahanaik.data.repository

import com.justindwinata.usahanaik.data.local.WeeklyProgressSnapshotDao
import com.justindwinata.usahanaik.data.mapper.WeeklyProgressSnapshotMapper.toDomain
import com.justindwinata.usahanaik.data.mapper.WeeklyProgressSnapshotMapper.toEntity
import com.justindwinata.usahanaik.domain.model.WeeklyProgressHistorySummary
import com.justindwinata.usahanaik.domain.model.WeeklyProgressSnapshot
import com.justindwinata.usahanaik.domain.progress.WeeklyProgressSnapshotGenerator
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LocalWeeklyProgressHistoryRepository(
    private val dao: WeeklyProgressSnapshotDao,
    private val nowProvider: () -> Long = { System.currentTimeMillis() },
    private val summaryGenerator: WeeklyProgressSnapshotGenerator = WeeklyProgressSnapshotGenerator()
) : WeeklyProgressHistoryRepository {
    override suspend fun saveSnapshot(snapshot: WeeklyProgressSnapshot): WeeklyProgressSnapshot {
        dao.deleteSnapshotByWeek(snapshot.weekLabel)
        val now = nowProvider()
        val entity = snapshot.toEntity(now)
        val id = dao.insertSnapshot(entity)
        return entity.copy(id = id).toDomain()
    }

    override suspend fun getSnapshotForWeek(weekLabel: String): WeeklyProgressSnapshot? {
        return dao.getSnapshotByWeek(weekLabel)?.toDomain()
    }

    override suspend fun listSnapshots(): List<WeeklyProgressSnapshot> {
        return dao.listSnapshots().map { it.toDomain() }
    }

    override fun observeSnapshots(): Flow<List<WeeklyProgressSnapshot>> {
        return dao.observeSnapshots().map { snapshots -> snapshots.map { it.toDomain() } }
    }

    override suspend fun clearSnapshots() {
        dao.clearSnapshots()
    }

    override suspend fun getHistorySummary(): WeeklyProgressHistorySummary {
        return summaryGenerator.summarizeHistory(listSnapshots())
    }
}
