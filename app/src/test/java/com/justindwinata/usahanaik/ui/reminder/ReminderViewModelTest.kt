package com.justindwinata.usahanaik.ui.reminder

import com.justindwinata.usahanaik.data.reminder.ReminderPermissionHelper
import com.justindwinata.usahanaik.data.reminder.ReminderScheduleResult
import com.justindwinata.usahanaik.data.reminder.ReminderScheduleStatus
import com.justindwinata.usahanaik.data.reminder.ReminderScheduler
import com.justindwinata.usahanaik.data.repository.BusinessReminderRepository
import com.justindwinata.usahanaik.domain.model.BusinessReminder
import com.justindwinata.usahanaik.domain.model.ReminderFrequency
import com.justindwinata.usahanaik.domain.model.ReminderPermissionState
import com.justindwinata.usahanaik.domain.model.ReminderStatus
import com.justindwinata.usahanaik.domain.model.ReminderSummary
import com.justindwinata.usahanaik.domain.model.ReminderSummaryCalculator
import com.justindwinata.usahanaik.domain.model.ReminderType
import com.justindwinata.usahanaik.test.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ReminderViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun loadsReminderSummary() = runTest {
        val repository = FakeBusinessReminderRepository()
        repository.createReminder(sampleReminder(title = "Record sales"))

        val viewModel = ReminderViewModel(repository, FakeReminderScheduler(), FakePermissionHelper())
        advanceUntilIdle()

        assertEquals(1, viewModel.uiState.value.summary.activeCount)
        assertEquals("Record sales", viewModel.uiState.value.summary.nextReminderTitle)
    }

    @Test
    fun rejectsMissingTitle() = runTest {
        val repository = FakeBusinessReminderRepository()
        val viewModel = ReminderViewModel(repository, FakeReminderScheduler(), FakePermissionHelper())
        advanceUntilIdle()

        viewModel.updateTitle("")
        viewModel.saveReminder()
        advanceUntilIdle()

        assertEquals(0, repository.reminders.value.size)
        assertEquals("Reminder title is required.", viewModel.uiState.value.visibleTitleError())
    }

    @Test
    fun savesActiveReminderAndSchedulesIt() = runTest {
        val repository = FakeBusinessReminderRepository()
        val scheduler = FakeReminderScheduler()
        val viewModel = ReminderViewModel(repository, scheduler, FakePermissionHelper())
        advanceUntilIdle()

        viewModel.updateTitle("Record daily sales")
        viewModel.updateTimeLabel("20:00")
        viewModel.saveReminder()
        advanceUntilIdle()

        assertEquals(1, repository.reminders.value.size)
        assertEquals(listOf(1L), scheduler.scheduledIds)
        assertTrue(viewModel.uiState.value.successMessage.orEmpty().contains("Reminder saved locally."))
    }

    @Test
    fun pausesAndEnablesReminder() = runTest {
        val repository = FakeBusinessReminderRepository()
        val scheduler = FakeReminderScheduler()
        val saved = repository.createReminder(sampleReminder())
        val viewModel = ReminderViewModel(repository, scheduler, FakePermissionHelper())
        advanceUntilIdle()

        viewModel.pauseReminder(saved.id)
        advanceUntilIdle()
        assertEquals(ReminderStatus.Paused, repository.getReminder(saved.id)?.status)
        assertEquals(listOf(saved.id), scheduler.cancelledIds)

        viewModel.enableReminder(saved.id)
        advanceUntilIdle()
        assertEquals(ReminderStatus.Active, repository.getReminder(saved.id)?.status)
        assertEquals(listOf(saved.id), scheduler.scheduledIds)
    }

    @Test
    fun deletesReminderAndCancelsScheduler() = runTest {
        val repository = FakeBusinessReminderRepository()
        val scheduler = FakeReminderScheduler()
        val saved = repository.createReminder(sampleReminder())
        val viewModel = ReminderViewModel(repository, scheduler, FakePermissionHelper())
        advanceUntilIdle()

        viewModel.deleteReminder(saved.id)
        advanceUntilIdle()

        assertEquals(0, repository.reminders.value.size)
        assertEquals(listOf(saved.id), scheduler.cancelledIds)
    }

    @Test
    fun refreshPermissionStateUpdatesSummaryPermission() = runTest {
        val permissionHelper = FakePermissionHelper(ReminderPermissionState.Denied)
        val repository = FakeBusinessReminderRepository()
        val saved = repository.createReminder(sampleReminder())
        val scheduler = FakeReminderScheduler()
        val viewModel = ReminderViewModel(repository, scheduler, permissionHelper)
        advanceUntilIdle()

        permissionHelper.permissionState = ReminderPermissionState.Granted
        viewModel.refreshPermissionState()
        advanceUntilIdle()

        assertEquals(ReminderPermissionState.Granted, viewModel.uiState.value.permissionState)
        assertEquals(ReminderPermissionState.Granted, viewModel.uiState.value.summary.permissionState)
        assertEquals(listOf(saved.id), scheduler.scheduledIds)
    }

    private fun sampleReminder(
        title: String = "Record sales",
        status: ReminderStatus = ReminderStatus.Active
    ): BusinessReminder {
        return BusinessReminder(
            title = title,
            description = "Record today's income and expenses.",
            type = ReminderType.DailyFinancialTracking,
            frequency = ReminderFrequency.Daily,
            timeLabel = "20:00",
            status = status
        )
    }

    private class FakeBusinessReminderRepository : BusinessReminderRepository {
        val reminders = MutableStateFlow<List<BusinessReminder>>(emptyList())
        private var nextId = 1L

        override suspend fun createReminder(reminder: BusinessReminder): BusinessReminder {
            val saved = reminder.copy(id = nextId++, createdAt = 100L, updatedAt = 100L)
            reminders.value = reminders.value + saved
            return saved
        }

        override suspend fun updateReminder(reminder: BusinessReminder): BusinessReminder {
            val updated = reminder.copy(updatedAt = 200L)
            reminders.value = reminders.value.map { if (it.id == reminder.id) updated else it }
            return updated
        }

        override suspend fun getReminder(id: Long): BusinessReminder? = reminders.value.firstOrNull { it.id == id }

        override suspend fun listReminders(): List<BusinessReminder> = reminders.value

        override suspend fun listActiveReminders(): List<BusinessReminder> {
            return reminders.value.filter { it.status == ReminderStatus.Active }
        }

        override fun observeReminders(): Flow<List<BusinessReminder>> = reminders

        override suspend fun updateStatus(id: Long, status: ReminderStatus): BusinessReminder? {
            reminders.value = reminders.value.map {
                if (it.id == id) it.copy(status = status, updatedAt = 200L) else it
            }
            return getReminder(id)
        }

        override suspend fun pauseReminder(id: Long): BusinessReminder? = updateStatus(id, ReminderStatus.Paused)

        override suspend fun enableReminder(id: Long): BusinessReminder? = updateStatus(id, ReminderStatus.Active)

        override suspend fun deleteReminder(id: Long) {
            reminders.value = reminders.value.filterNot { it.id == id }
        }

        override suspend fun clearReminders() {
            reminders.value = emptyList()
        }

        override suspend fun getReminderSummary(): ReminderSummary {
            return ReminderSummaryCalculator.summarize(reminders.value)
        }
    }

    private class FakeReminderScheduler : ReminderScheduler {
        val scheduledIds = mutableListOf<Long>()
        val cancelledIds = mutableListOf<Long>()

        override suspend fun schedule(reminder: BusinessReminder): ReminderScheduleResult {
            scheduledIds += reminder.id
            return ReminderScheduleResult(ReminderScheduleStatus.ScheduledInApp, "Scheduled in app.")
        }

        override suspend fun cancel(reminderId: Long): ReminderScheduleResult {
            cancelledIds += reminderId
            return ReminderScheduleResult(ReminderScheduleStatus.Cancelled, "Cancelled.")
        }
    }

    private class FakePermissionHelper(
        var permissionState: ReminderPermissionState = ReminderPermissionState.Granted
    ) : ReminderPermissionHelper {
        override fun currentState(): ReminderPermissionState = permissionState
    }
}
