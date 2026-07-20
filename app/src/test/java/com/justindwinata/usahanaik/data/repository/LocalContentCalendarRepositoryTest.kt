package com.justindwinata.usahanaik.data.repository

import com.justindwinata.usahanaik.data.local.ContentCalendarDao
import com.justindwinata.usahanaik.data.local.ContentCalendarEntity
import com.justindwinata.usahanaik.domain.model.ContentCalendarSchedule
import com.justindwinata.usahanaik.domain.model.ContentCalendarStatus
import com.justindwinata.usahanaik.domain.model.ContentPlatform
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class LocalContentCalendarRepositoryTest {
    @Test
    fun schedulesAndListsContent() = runTest {
        val repository = LocalContentCalendarRepository(FakeContentCalendarDao(), nowProvider = { 100L })

        val saved = repository.scheduleContent(sampleSchedule())

        assertEquals(1L, saved.id)
        assertEquals(1, repository.listSchedules().size)
    }

    @Test
    fun listsUpcomingItemsByDate() = runTest {
        val repository = LocalContentCalendarRepository(FakeContentCalendarDao(), nowProvider = { 100L })
        repository.scheduleContent(sampleSchedule(title = "Past", date = "2026-07-19"))
        repository.scheduleContent(sampleSchedule(title = "Upcoming", date = "2026-07-21"))

        val upcoming = repository.listUpcoming("2026-07-20")

        assertEquals(1, upcoming.size)
        assertEquals("Upcoming", upcoming.first().title)
    }

    @Test
    fun updatesStatusAndSummaryCounts() = runTest {
        val repository = LocalContentCalendarRepository(FakeContentCalendarDao(), nowProvider = { 100L })
        val saved = repository.scheduleContent(sampleSchedule())

        repository.updateStatus(saved.id, ContentCalendarStatus.Posted)

        val updated = repository.getSchedule(saved.id)
        val summary = repository.getSummary()
        assertEquals(ContentCalendarStatus.Posted, updated?.status)
        assertEquals(1, summary.postedCount)
        assertEquals(1f, summary.executionRate)
    }

    @Test
    fun deletesScheduledContent() = runTest {
        val repository = LocalContentCalendarRepository(FakeContentCalendarDao(), nowProvider = { 100L })
        val saved = repository.scheduleContent(sampleSchedule())

        repository.deleteSchedule(saved.id)

        assertNull(repository.getSchedule(saved.id))
    }

    private fun sampleSchedule(
        title: String = "Menu highlight",
        date: String = "2026-07-21"
    ): ContentCalendarSchedule {
        return ContentCalendarSchedule(
            contentIdeaId = 7L,
            title = title,
            platform = ContentPlatform.InstagramFeed,
            scheduledDate = date,
            timeLabel = "09:00",
            postingNote = "Review before posting."
        )
    }

    private class FakeContentCalendarDao : ContentCalendarDao {
        private val items = MutableStateFlow<List<ContentCalendarEntity>>(emptyList())
        private var nextId = 1L

        override suspend fun insertItem(item: ContentCalendarEntity): Long {
            val id = if (item.id == 0L) nextId++ else item.id
            items.value = items.value.filterNot { it.id == id } + item.copy(id = id)
            return id
        }

        override suspend fun getItem(id: Long): ContentCalendarEntity? = items.value.firstOrNull { it.id == id }

        override suspend fun listItems(): List<ContentCalendarEntity> {
            return items.value.sortedWith(compareBy<ContentCalendarEntity> { it.scheduledDate }.thenBy { it.timeLabel })
        }

        override fun observeItems(): Flow<List<ContentCalendarEntity>> = items

        override suspend fun listUpcoming(date: String): List<ContentCalendarEntity> {
            return listItems().filter { it.scheduledDate >= date }
        }

        override suspend fun listBetween(startDate: String, endDate: String): List<ContentCalendarEntity> {
            return listItems().filter { it.scheduledDate in startDate..endDate }
        }

        override suspend fun updateStatus(id: Long, status: String, updatedAt: Long) {
            items.value = items.value.map {
                if (it.id == id) it.copy(status = status, updatedAt = updatedAt) else it
            }
        }

        override suspend fun deleteItem(id: Long) {
            items.value = items.value.filterNot { it.id == id }
        }

        override suspend fun clearItems() {
            items.value = emptyList()
        }
    }
}
