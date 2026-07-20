package com.justindwinata.usahanaik.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface BusinessReportSnapshotDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSnapshot(snapshot: BusinessReportSnapshotEntity): Long

    @Query("SELECT * FROM business_report_snapshots WHERE id = :id LIMIT 1")
    suspend fun getSnapshot(id: Long): BusinessReportSnapshotEntity?

    @Query("SELECT * FROM business_report_snapshots ORDER BY generatedAt DESC, updatedAt DESC")
    suspend fun listSnapshots(): List<BusinessReportSnapshotEntity>

    @Query("SELECT * FROM business_report_snapshots ORDER BY generatedAt DESC, updatedAt DESC LIMIT 1")
    suspend fun getLatestSnapshot(): BusinessReportSnapshotEntity?

    @Query("SELECT * FROM business_report_snapshots ORDER BY generatedAt DESC, updatedAt DESC")
    fun observeSnapshots(): Flow<List<BusinessReportSnapshotEntity>>

    @Query("DELETE FROM business_report_snapshots WHERE id = :id")
    suspend fun deleteSnapshot(id: Long)

    @Query("DELETE FROM business_report_snapshots")
    suspend fun clearSnapshots()
}
