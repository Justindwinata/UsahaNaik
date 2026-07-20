package com.justindwinata.usahanaik.data.repository

import com.justindwinata.usahanaik.data.local.BusinessReminderDao
import com.justindwinata.usahanaik.data.mapper.BusinessReminderMapper.toDomain
import com.justindwinata.usahanaik.data.mapper.BusinessReminderMapper.toEntity
import com.justindwinata.usahanaik.domain.model.BusinessReminder
import com.justindwinata.usahanaik.domain.model.ReminderStatus
import com.justindwinata.usahanaik.domain.model.ReminderSummary
import com.justindwinata.usahanaik.domain.model.ReminderSummaryCalculator
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LocalBusinessReminderRepository(
    private val dao: BusinessReminderDao,
    private val nowProvider: () -> Long = { System.currentTimeMillis() }
) : BusinessReminderRepository {
    override suspend fun createReminder(reminder: BusinessReminder): BusinessReminder {
        val now = nowProvider()
        val entity = reminder.toEntity(now)
        val id = dao.insertReminder(entity)
        return entity.copy(id = id).toDomain()
    }

    override suspend fun updateReminder(reminder: BusinessReminder): BusinessReminder {
        val now = nowProvider()
        val entity = reminder.toEntity(now)
        val id = dao.insertReminder(entity)
        return entity.copy(id = id).toDomain()
    }

    override suspend fun getReminder(id: Long): BusinessReminder? {
        return dao.getReminder(id)?.toDomain()
    }

    override suspend fun listReminders(): List<BusinessReminder> {
        return dao.listReminders().map { it.toDomain() }
    }

    override suspend fun listActiveReminders(): List<BusinessReminder> {
        return dao.listActiveReminders().map { it.toDomain() }
    }

    override fun observeReminders(): Flow<List<BusinessReminder>> {
        return dao.observeReminders().map { reminders -> reminders.map { it.toDomain() } }
    }

    override suspend fun updateStatus(id: Long, status: ReminderStatus): BusinessReminder? {
        dao.updateStatus(id, status.name, nowProvider())
        return dao.getReminder(id)?.toDomain()
    }

    override suspend fun pauseReminder(id: Long): BusinessReminder? {
        return updateStatus(id, ReminderStatus.Paused)
    }

    override suspend fun enableReminder(id: Long): BusinessReminder? {
        return updateStatus(id, ReminderStatus.Active)
    }

    override suspend fun deleteReminder(id: Long) {
        dao.deleteReminder(id)
    }

    override suspend fun clearReminders() {
        dao.clearReminders()
    }

    override suspend fun getReminderSummary(): ReminderSummary {
        return ReminderSummaryCalculator.summarize(listReminders())
    }
}
