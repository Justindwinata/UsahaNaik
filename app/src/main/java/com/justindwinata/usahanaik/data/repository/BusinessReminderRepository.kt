package com.justindwinata.usahanaik.data.repository

import com.justindwinata.usahanaik.domain.model.BusinessReminder
import com.justindwinata.usahanaik.domain.model.ReminderStatus
import com.justindwinata.usahanaik.domain.model.ReminderSummary
import kotlinx.coroutines.flow.Flow

interface BusinessReminderRepository {
    suspend fun createReminder(reminder: BusinessReminder): BusinessReminder
    suspend fun updateReminder(reminder: BusinessReminder): BusinessReminder
    suspend fun getReminder(id: Long): BusinessReminder?
    suspend fun listReminders(): List<BusinessReminder>
    suspend fun listActiveReminders(): List<BusinessReminder>
    fun observeReminders(): Flow<List<BusinessReminder>>
    suspend fun updateStatus(id: Long, status: ReminderStatus): BusinessReminder?
    suspend fun pauseReminder(id: Long): BusinessReminder?
    suspend fun enableReminder(id: Long): BusinessReminder?
    suspend fun deleteReminder(id: Long)
    suspend fun clearReminders()
    suspend fun getReminderSummary(): ReminderSummary
}
