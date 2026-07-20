package com.justindwinata.usahanaik.ui.content

import com.justindwinata.usahanaik.data.repository.ContentCalendarRepository
import com.justindwinata.usahanaik.domain.model.ContentCalendarSchedule
import com.justindwinata.usahanaik.domain.model.ContentCalendarStatus
import com.justindwinata.usahanaik.domain.model.ContentCalendarSummary
import com.justindwinata.usahanaik.domain.model.ContentCalendarSummaryCalculator
import com.justindwinata.usahanaik.domain.model.ContentGoal
import com.justindwinata.usahanaik.domain.model.ContentIdea
import com.justindwinata.usahanaik.domain.model.ContentPlatform
import com.justindwinata.usahanaik.test.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ContentCalendarViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun requestSchedulePrefillsIdeaDetails() = runTest {
        val viewModel = ContentCalendarViewModel(
            repository = FakeContentCalendarRepository(),
            todayProvider = { "2026-07-20" }
        )
        advanceUntilIdle()

        viewModel.requestSchedule(sampleIdea())

        val form = viewModel.uiState.value.form
        assertTrue(form.isActive)
        assertEquals("Content plan", form.title)
        assertEquals("2026-07-20", form.scheduledDate)
    }

    @Test
    fun saveSchedulePersistsItem() = runTest {
        val viewModel = ContentCalendarViewModel(
            repository = FakeContentCalendarRepository(),
            todayProvider = { "2026-07-20" }
        )
        advanceUntilIdle()

        viewModel.requestSchedule(sampleIdea())
        viewModel.updateTimeLabel("09:00")
        viewModel.saveSchedule()
        advanceUntilIdle()

        assertFalse(viewModel.uiState.value.form.isActive)
        assertEquals(1, viewModel.uiState.value.schedules.size)
        assertEquals("Content scheduled locally.", viewModel.uiState.value.successMessage)
    }

    @Test
    fun rejectsScheduleWithoutSavedIdea() = runTest {
        val viewModel = ContentCalendarViewModel(
            repository = FakeContentCalendarRepository(),
            todayProvider = { "2026-07-20" }
        )
        advanceUntilIdle()

        viewModel.requestSchedule(sampleIdea(id = 0L))

        assertEquals("Save the idea before scheduling it.", viewModel.uiState.value.errorMessage)
    }

    @Test
    fun updatesStatusAndDeletesSchedule() = runTest {
        val repository = FakeContentCalendarRepository()
        val saved = repository.scheduleContent(sampleSchedule())
        val viewModel = ContentCalendarViewModel(repository = repository)
        advanceUntilIdle()

        viewModel.updateStatus(saved.id, ContentCalendarStatus.Posted)
        advanceUntilIdle()

        assertEquals(ContentCalendarStatus.Posted, viewModel.uiState.value.schedules.first().status)

        viewModel.deleteSchedule(saved.id)
        advanceUntilIdle()

        assertTrue(viewModel.uiState.value.schedules.isEmpty())
    }

    private fun sampleIdea(id: Long = 9L): ContentIdea {
        return ContentIdea(
            id = id,
            title = "Content plan",
            category = "Food & Beverage",
            platformSuggestion = "Instagram Feed",
            angle = "Show menu value.",
            cta = "Order via WhatsApp.",
            platform = ContentPlatform.InstagramFeed,
            goal = ContentGoal.PromotionCampaign,
            recommendedPostingNote = "Review before posting."
        )
    }

    private fun sampleSchedule(): ContentCalendarSchedule {
        return ContentCalendarSchedule(
            contentIdeaId = 9L,
            title = "Content plan",
            platform = ContentPlatform.InstagramFeed,
            scheduledDate = "2026-07-20"
        )
    }

    private class FakeContentCalendarRepository : ContentCalendarRepository {
        private val schedules = MutableStateFlow<List<ContentCalendarSchedule>>(emptyList())
        private var nextId = 1L

        override suspend fun scheduleContent(item: ContentCalendarSchedule): ContentCalendarSchedule {
            val saved = item.copy(id = if (item.id == 0L) nextId++ else item.id)
            schedules.value = schedules.value.filterNot { it.id == saved.id } + saved
            return saved
        }

        override suspend fun getSchedule(id: Long): ContentCalendarSchedule? {
            return schedules.value.firstOrNull { it.id == id }
        }

        override suspend fun listSchedules(): List<ContentCalendarSchedule> = schedules.value

        override fun observeSchedules(): Flow<List<ContentCalendarSchedule>> = schedules

        override suspend fun listUpcoming(fromDate: String): List<ContentCalendarSchedule> {
            return schedules.value.filter { it.scheduledDate >= fromDate }
        }

        override suspend fun listBetween(startDate: String, endDate: String): List<ContentCalendarSchedule> {
            return schedules.value.filter { it.scheduledDate in startDate..endDate }
        }

        override suspend fun updateStatus(id: Long, status: ContentCalendarStatus): ContentCalendarSchedule? {
            var updated: ContentCalendarSchedule? = null
            schedules.value = schedules.value.map {
                if (it.id == id) {
                    it.copy(status = status).also { item -> updated = item }
                } else {
                    it
                }
            }
            return updated
        }

        override suspend fun deleteSchedule(id: Long) {
            schedules.value = schedules.value.filterNot { it.id == id }
        }

        override suspend fun getSummary(startDate: String?, endDate: String?): ContentCalendarSummary {
            return ContentCalendarSummaryCalculator.summarize(schedules.value)
        }
    }
}
