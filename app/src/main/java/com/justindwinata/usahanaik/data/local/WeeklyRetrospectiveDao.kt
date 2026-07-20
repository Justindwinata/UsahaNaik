package com.justindwinata.usahanaik.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface WeeklyRetrospectiveDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRetrospective(retrospective: WeeklyRetrospectiveEntity): Long

    @Query("SELECT * FROM weekly_retrospectives WHERE id = :id LIMIT 1")
    suspend fun getRetrospective(id: Long): WeeklyRetrospectiveEntity?

    @Query("SELECT * FROM weekly_retrospectives WHERE weekLabel = :weekLabel LIMIT 1")
    suspend fun getRetrospectiveForWeek(weekLabel: String): WeeklyRetrospectiveEntity?

    @Query("SELECT * FROM weekly_retrospectives ORDER BY generatedDate DESC, updatedAt DESC")
    suspend fun listRetrospectives(): List<WeeklyRetrospectiveEntity>

    @Query("SELECT * FROM weekly_retrospectives ORDER BY generatedDate DESC, updatedAt DESC LIMIT 1")
    suspend fun getLatestRetrospective(): WeeklyRetrospectiveEntity?

    @Query("SELECT * FROM weekly_retrospectives ORDER BY generatedDate DESC, updatedAt DESC")
    fun observeRetrospectives(): Flow<List<WeeklyRetrospectiveEntity>>

    @Query("DELETE FROM weekly_retrospectives WHERE weekLabel = :weekLabel")
    suspend fun deleteRetrospectiveForWeek(weekLabel: String)

    @Query("DELETE FROM weekly_retrospectives")
    suspend fun clearRetrospectives()
}
