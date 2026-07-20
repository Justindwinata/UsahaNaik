package com.justindwinata.usahanaik.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface WeeklyProgressSnapshotDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSnapshot(snapshot: WeeklyProgressSnapshotEntity): Long

    @Query("SELECT * FROM weekly_progress_snapshots WHERE id = :id LIMIT 1")
    suspend fun getSnapshot(id: Long): WeeklyProgressSnapshotEntity?

    @Query("SELECT * FROM weekly_progress_snapshots WHERE weekLabel = :weekLabel LIMIT 1")
    suspend fun getSnapshotByWeek(weekLabel: String): WeeklyProgressSnapshotEntity?

    @Query("SELECT * FROM weekly_progress_snapshots ORDER BY weekStartDate DESC")
    suspend fun listSnapshots(): List<WeeklyProgressSnapshotEntity>

    @Query("SELECT * FROM weekly_progress_snapshots ORDER BY weekStartDate DESC")
    fun observeSnapshots(): Flow<List<WeeklyProgressSnapshotEntity>>

    @Query("DELETE FROM weekly_progress_snapshots WHERE weekLabel = :weekLabel")
    suspend fun deleteSnapshotByWeek(weekLabel: String)

    @Query("DELETE FROM weekly_progress_snapshots")
    suspend fun clearSnapshots()
}
