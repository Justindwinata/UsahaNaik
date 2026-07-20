package com.justindwinata.usahanaik.domain.model

data class ContentCalendarSchedule(
    val id: Long = 0L,
    val contentIdeaId: Long,
    val title: String,
    val platform: ContentPlatform,
    val scheduledDate: String,
    val timeLabel: String = "",
    val postingNote: String = "",
    val status: ContentCalendarStatus = ContentCalendarStatus.Planned,
    val createdAt: Long = 0L,
    val updatedAt: Long = 0L
)

enum class ContentCalendarStatus(val label: String) {
    Planned("Planned"),
    Posted("Posted"),
    Skipped("Skipped"),
    Done("Done")
}

data class ContentCalendarDay(
    val date: String,
    val items: List<ContentCalendarSchedule>
) {
    val plannedCount: Int = items.count { it.status == ContentCalendarStatus.Planned }
    val postedOrDoneCount: Int = items.count {
        it.status == ContentCalendarStatus.Posted || it.status == ContentCalendarStatus.Done
    }
    val skippedCount: Int = items.count { it.status == ContentCalendarStatus.Skipped }
}

data class ContentCalendarSummary(
    val totalScheduled: Int,
    val plannedCount: Int,
    val postedCount: Int,
    val skippedCount: Int,
    val doneCount: Int,
    val nextScheduledTitle: String,
    val nextScheduledDate: String,
    val executionRate: Float
) {
    val postedOrDoneCount: Int = postedCount + doneCount
    val hasScheduledContent: Boolean = totalScheduled > 0
}

object ContentCalendarSummaryCalculator {
    fun summarize(items: List<ContentCalendarSchedule>): ContentCalendarSummary {
        val sortedItems = items.sortedWith(compareBy<ContentCalendarSchedule> { it.scheduledDate }.thenBy { it.timeLabel })
        val nextItem = sortedItems.firstOrNull { it.status == ContentCalendarStatus.Planned } ?: sortedItems.firstOrNull()
        val postedCount = items.count { it.status == ContentCalendarStatus.Posted }
        val doneCount = items.count { it.status == ContentCalendarStatus.Done }
        val totalScheduled = items.size

        return ContentCalendarSummary(
            totalScheduled = totalScheduled,
            plannedCount = items.count { it.status == ContentCalendarStatus.Planned },
            postedCount = postedCount,
            skippedCount = items.count { it.status == ContentCalendarStatus.Skipped },
            doneCount = doneCount,
            nextScheduledTitle = nextItem?.title ?: "No scheduled content yet",
            nextScheduledDate = nextItem?.scheduledDate ?: "-",
            executionRate = if (totalScheduled == 0) {
                0f
            } else {
                ((postedCount + doneCount).toFloat() / totalScheduled).coerceIn(0f, 1f)
            }
        )
    }

    fun groupByDay(items: List<ContentCalendarSchedule>): List<ContentCalendarDay> {
        return items
            .sortedWith(compareBy<ContentCalendarSchedule> { it.scheduledDate }.thenBy { it.timeLabel })
            .groupBy { it.scheduledDate }
            .map { (date, schedules) -> ContentCalendarDay(date = date, items = schedules) }
    }
}
