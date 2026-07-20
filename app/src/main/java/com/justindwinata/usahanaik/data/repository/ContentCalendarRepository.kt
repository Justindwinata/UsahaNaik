package com.justindwinata.usahanaik.data.repository

import com.justindwinata.usahanaik.domain.model.ContentCalendarSchedule
import com.justindwinata.usahanaik.domain.model.ContentCalendarStatus
import com.justindwinata.usahanaik.domain.model.ContentCalendarSummary
import kotlinx.coroutines.flow.Flow

interface ContentCalendarRepository {
    suspend fun scheduleContent(item: ContentCalendarSchedule): ContentCalendarSchedule
    suspend fun getSchedule(id: Long): ContentCalendarSchedule?
    suspend fun listSchedules(): List<ContentCalendarSchedule>
    fun observeSchedules(): Flow<List<ContentCalendarSchedule>>
    suspend fun listUpcoming(fromDate: String): List<ContentCalendarSchedule>
    suspend fun listBetween(startDate: String, endDate: String): List<ContentCalendarSchedule>
    suspend fun updateStatus(id: Long, status: ContentCalendarStatus): ContentCalendarSchedule?
    suspend fun deleteSchedule(id: Long)
    suspend fun getSummary(startDate: String? = null, endDate: String? = null): ContentCalendarSummary
}
