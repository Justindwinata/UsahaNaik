package com.justindwinata.usahanaik.data.repository

import com.justindwinata.usahanaik.domain.model.WeeklyProgressHistorySummary
import com.justindwinata.usahanaik.domain.model.WeeklyProgressSnapshot
import kotlinx.coroutines.flow.Flow

interface WeeklyProgressHistoryRepository {
    suspend fun saveSnapshot(snapshot: WeeklyProgressSnapshot): WeeklyProgressSnapshot
    suspend fun getSnapshotForWeek(weekLabel: String): WeeklyProgressSnapshot?
    suspend fun listSnapshots(): List<WeeklyProgressSnapshot>
    fun observeSnapshots(): Flow<List<WeeklyProgressSnapshot>>
    suspend fun clearSnapshots()
    suspend fun getHistorySummary(): WeeklyProgressHistorySummary
}
