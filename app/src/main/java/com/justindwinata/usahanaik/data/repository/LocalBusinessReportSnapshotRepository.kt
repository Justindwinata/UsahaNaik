package com.justindwinata.usahanaik.data.repository

import com.justindwinata.usahanaik.data.local.BusinessReportSnapshotDao
import com.justindwinata.usahanaik.data.mapper.BusinessReportSnapshotMapper.toDomain
import com.justindwinata.usahanaik.data.mapper.BusinessReportSnapshotMapper.toEntity
import com.justindwinata.usahanaik.domain.model.BusinessReportSnapshot
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LocalBusinessReportSnapshotRepository(
    private val dao: BusinessReportSnapshotDao,
    private val nowProvider: () -> Long = { System.currentTimeMillis() }
) : BusinessReportSnapshotRepository {
    override suspend fun saveSnapshot(snapshot: BusinessReportSnapshot): BusinessReportSnapshot {
        val now = nowProvider()
        val entity = snapshot.toEntity(now)
        val id = dao.insertSnapshot(entity)
        return entity.copy(id = id).toDomain()
    }

    override suspend fun getSnapshot(id: Long): BusinessReportSnapshot? {
        return dao.getSnapshot(id)?.toDomain()
    }

    override suspend fun getLatestSnapshot(): BusinessReportSnapshot? {
        return dao.getLatestSnapshot()?.toDomain()
    }

    override suspend fun listSnapshots(): List<BusinessReportSnapshot> {
        return dao.listSnapshots().map { it.toDomain() }
    }

    override fun observeSnapshots(): Flow<List<BusinessReportSnapshot>> {
        return dao.observeSnapshots().map { snapshots -> snapshots.map { it.toDomain() } }
    }

    override suspend fun deleteSnapshot(id: Long) {
        dao.deleteSnapshot(id)
    }

    override suspend fun clearSnapshots() {
        dao.clearSnapshots()
    }
}
