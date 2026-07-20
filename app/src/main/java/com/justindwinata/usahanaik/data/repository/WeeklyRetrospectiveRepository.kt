package com.justindwinata.usahanaik.data.repository

import com.justindwinata.usahanaik.domain.model.WeeklyRetrospective
import kotlinx.coroutines.flow.Flow

interface WeeklyRetrospectiveRepository {
    suspend fun saveRetrospective(retrospective: WeeklyRetrospective): WeeklyRetrospective
    suspend fun getRetrospectiveForWeek(weekLabel: String): WeeklyRetrospective?
    suspend fun getLatestRetrospective(): WeeklyRetrospective?
    suspend fun listRetrospectives(): List<WeeklyRetrospective>
    fun observeRetrospectives(): Flow<List<WeeklyRetrospective>>
    suspend fun clearRetrospectives()
}
