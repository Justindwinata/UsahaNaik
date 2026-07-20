package com.justindwinata.usahanaik.data.repository

import com.justindwinata.usahanaik.data.local.BusinessReminderDao
import com.justindwinata.usahanaik.data.local.BusinessReminderEntity
import com.justindwinata.usahanaik.domain.model.BusinessReminder
import com.justindwinata.usahanaik.domain.model.ReminderFrequency
import com.justindwinata.usahanaik.domain.model.ReminderStatus
import com.justindwinata.usahanaik.domain.model.ReminderType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class LocalBusinessReminderRepositoryTest {
    @Test
    fun createsAndListsReminder() = runTest {
        val repository = LocalBusinessReminderRepository(FakeBusinessReminderDao(), nowProvider = { 100L })

        val saved = repository.createReminder(sampleReminder())

        assertEquals(1L, saved.id)
        assertEquals(1, repository.listReminders().size)
    }

    @Test
    fun updatesExistingReminder() = runTest {
        val repository = LocalBusinessReminderRepository(FakeBusinessReminderDao(), nowProvider = { 100L })
        val saved = repository.createReminder(sampleReminder())

        repository.updateReminder(saved.copy(title = "Updated title"))

        assertEquals("Updated title", repository.getReminder(saved.id)?.title)
    }

    @Test
    fun pausesAndEnablesReminder() = runTest {
        val repository = LocalBusinessReminderRepository(FakeBusinessReminderDao(), nowProvider = { 100L })
        val saved = repository.createReminder(sampleReminder())

        repository.pauseReminder(saved.id)
        assertEquals(ReminderStatus.Paused, repository.getReminder(saved.id)?.status)

        repository.enableReminder(saved.id)
        assertEquals(ReminderStatus.Active, repository.getReminder(saved.id)?.status)
    }

    @Test
    fun listsOnlyActiveRemindersAndSummary() = runTest {
        val repository = LocalBusinessReminderRepository(FakeBusinessReminderDao(), nowProvider = { 100L })
        repository.createReminder(sampleReminder(title = "Active one"))
        val paused = repository.createReminder(sampleReminder(title = "Paused one"))
        repository.pauseReminder(paused.id)

        assertEquals(1, repository.listActiveReminders().size)
        assertEquals(1, repository.getReminderSummary().activeCount)
        assertEquals("Active one", repository.getReminderSummary().nextReminderTitle)
    }

    @Test
    fun deletesReminder() = runTest {
        val repository = LocalBusinessReminderRepository(FakeBusinessReminderDao(), nowProvider = { 100L })
        val saved = repository.createReminder(sampleReminder())

        repository.deleteReminder(saved.id)

        assertNull(repository.getReminder(saved.id))
    }

    private fun sampleReminder(title: String = "Record sales"): BusinessReminder {
        return BusinessReminder(
            title = title,
            description = "Record today's income and expenses.",
            type = ReminderType.DailyFinancialTracking,
            frequency = ReminderFrequency.Daily,
            timeLabel = "20:00"
        )
    }

    private class FakeBusinessReminderDao : BusinessReminderDao {
        private val reminders = MutableStateFlow<List<BusinessReminderEntity>>(emptyList())
        private var nextId = 1L

        override suspend fun insertReminder(reminder: BusinessReminderEntity): Long {
            val id = if (reminder.id == 0L) nextId++ else reminder.id
            reminders.value = reminders.value.filterNot { it.id == id } + reminder.copy(id = id)
            return id
        }

        override suspend fun getReminder(id: Long): BusinessReminderEntity? {
            return reminders.value.firstOrNull { it.id == id }
        }

        override suspend fun listReminders(): List<BusinessReminderEntity> {
            return reminders.value.sortedWith(compareBy<BusinessReminderEntity> { it.status }.thenBy { it.timeLabel })
        }

        override suspend fun listActiveReminders(): List<BusinessReminderEntity> {
            return listReminders().filter { it.status == ReminderStatus.Active.name }
        }

        override fun observeReminders(): Flow<List<BusinessReminderEntity>> = reminders

        override suspend fun updateStatus(id: Long, status: String, updatedAt: Long) {
            reminders.value = reminders.value.map {
                if (it.id == id) it.copy(status = status, updatedAt = updatedAt) else it
            }
        }

        override suspend fun deleteReminder(id: Long) {
            reminders.value = reminders.value.filterNot { it.id == id }
        }

        override suspend fun clearReminders() {
            reminders.value = emptyList()
        }
    }
}
