package com.justindwinata.usahanaik.data.repository

import com.justindwinata.usahanaik.data.local.ContentCalendarDao
import com.justindwinata.usahanaik.data.mapper.ContentCalendarMapper.toDomain
import com.justindwinata.usahanaik.data.mapper.ContentCalendarMapper.toEntity
import com.justindwinata.usahanaik.domain.model.ContentCalendarSchedule
import com.justindwinata.usahanaik.domain.model.ContentCalendarStatus
import com.justindwinata.usahanaik.domain.model.ContentCalendarSummary
import com.justindwinata.usahanaik.domain.model.ContentCalendarSummaryCalculator
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LocalContentCalendarRepository(
    private val dao: ContentCalendarDao,
    private val nowProvider: () -> Long = { System.currentTimeMillis() }
) : ContentCalendarRepository {
    override suspend fun scheduleContent(item: ContentCalendarSchedule): ContentCalendarSchedule {
        val now = nowProvider()
        val entity = item.toEntity(now)
        val id = dao.insertItem(entity)
        return entity.copy(id = id).toDomain()
    }

    override suspend fun getSchedule(id: Long): ContentCalendarSchedule? = dao.getItem(id)?.toDomain()

    override suspend fun listSchedules(): List<ContentCalendarSchedule> = dao.listItems().map { it.toDomain() }

    override fun observeSchedules(): Flow<List<ContentCalendarSchedule>> {
        return dao.observeItems().map { items -> items.map { it.toDomain() } }
    }

    override suspend fun listUpcoming(fromDate: String): List<ContentCalendarSchedule> {
        return dao.listUpcoming(fromDate).map { it.toDomain() }
    }

    override suspend fun listBetween(startDate: String, endDate: String): List<ContentCalendarSchedule> {
        return dao.listBetween(startDate, endDate).map { it.toDomain() }
    }

    override suspend fun updateStatus(id: Long, status: ContentCalendarStatus): ContentCalendarSchedule? {
        dao.updateStatus(id, status.name, nowProvider())
        return dao.getItem(id)?.toDomain()
    }

    override suspend fun deleteSchedule(id: Long) {
        dao.deleteItem(id)
    }

    override suspend fun getSummary(startDate: String?, endDate: String?): ContentCalendarSummary {
        val items = if (startDate != null && endDate != null) {
            listBetween(startDate, endDate)
        } else {
            listSchedules()
        }
        return ContentCalendarSummaryCalculator.summarize(items)
    }
}
