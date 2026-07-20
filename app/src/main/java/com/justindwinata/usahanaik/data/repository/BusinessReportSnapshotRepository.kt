package com.justindwinata.usahanaik.data.repository

import com.justindwinata.usahanaik.domain.model.BusinessReportSnapshot
import kotlinx.coroutines.flow.Flow

interface BusinessReportSnapshotRepository {
    suspend fun saveSnapshot(snapshot: BusinessReportSnapshot): BusinessReportSnapshot
    suspend fun getSnapshot(id: Long): BusinessReportSnapshot?
    suspend fun getLatestSnapshot(): BusinessReportSnapshot?
    suspend fun listSnapshots(): List<BusinessReportSnapshot>
    fun observeSnapshots(): Flow<List<BusinessReportSnapshot>>
    suspend fun deleteSnapshot(id: Long)
    suspend fun clearSnapshots()
}
