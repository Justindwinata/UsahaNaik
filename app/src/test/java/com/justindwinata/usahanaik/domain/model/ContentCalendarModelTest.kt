package com.justindwinata.usahanaik.domain.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ContentCalendarModelTest {
    @Test
    fun emptySummaryHasNoScheduledContent() {
        val summary = ContentCalendarSummaryCalculator.summarize(emptyList())

        assertFalse(summary.hasScheduledContent)
        assertEquals(0, summary.totalScheduled)
        assertEquals(0f, summary.executionRate)
        assertEquals("No scheduled content yet", summary.nextScheduledTitle)
    }

    @Test
    fun summaryCountsStatusesAndExecutionRate() {
        val summary = ContentCalendarSummaryCalculator.summarize(
            listOf(
                sampleSchedule(status = ContentCalendarStatus.Planned),
                sampleSchedule(status = ContentCalendarStatus.Posted),
                sampleSchedule(status = ContentCalendarStatus.Done),
                sampleSchedule(status = ContentCalendarStatus.Skipped)
            )
        )

        assertTrue(summary.hasScheduledContent)
        assertEquals(4, summary.totalScheduled)
        assertEquals(1, summary.plannedCount)
        assertEquals(1, summary.postedCount)
        assertEquals(1, summary.doneCount)
        assertEquals(1, summary.skippedCount)
        assertEquals(0.5f, summary.executionRate)
    }

    @Test
    fun groupsCalendarItemsByDate() {
        val days = ContentCalendarSummaryCalculator.groupByDay(
            listOf(
                sampleSchedule(title = "Second day", date = "2026-07-22"),
                sampleSchedule(title = "First morning", date = "2026-07-21", time = "09:00"),
                sampleSchedule(title = "First afternoon", date = "2026-07-21", time = "15:00")
            )
        )

        assertEquals(2, days.size)
        assertEquals("2026-07-21", days.first().date)
        assertEquals(2, days.first().items.size)
        assertEquals("First morning", days.first().items.first().title)
    }

    private fun sampleSchedule(
        title: String = "Menu highlight",
        date: String = "2026-07-21",
        time: String = "",
        status: ContentCalendarStatus = ContentCalendarStatus.Planned
    ): ContentCalendarSchedule {
        return ContentCalendarSchedule(
            contentIdeaId = 1L,
            title = title,
            platform = ContentPlatform.InstagramReels,
            scheduledDate = date,
            timeLabel = time,
            postingNote = "Review before posting.",
            status = status
        )
    }
}
