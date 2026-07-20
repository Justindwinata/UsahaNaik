package com.justindwinata.usahanaik.domain.progress

import com.justindwinata.usahanaik.domain.model.ContentCalendarSummary
import com.justindwinata.usahanaik.domain.model.NextWeekSuggestion
import com.justindwinata.usahanaik.domain.model.ProgressTrendPoint
import com.justindwinata.usahanaik.domain.model.WeeklyProgressHistorySummary
import com.justindwinata.usahanaik.domain.model.WeeklyProgressSnapshot
import com.justindwinata.usahanaik.domain.model.WeeklyRetrospective
import com.justindwinata.usahanaik.domain.weekly.WeeklyPlanDashboardSummary
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class DashboardContinuitySummaryMapperTest {
    @Test
    fun mapsEmptyRetrospectiveState() {
        val summary = DashboardContinuitySummaryMapper.from(
            weeklyPlanSummary = WeeklyPlanDashboardSummary(
                hasPlan = false,
                focusTitle = "No active weekly plan",
                taskProgressLabel = "0/0 tasks",
                milestoneProgressLabel = "0/0 milestones",
                taskProgress = 0f,
                milestoneProgress = 0f,
                nextTaskTitle = "Generate plan",
                ctaLabel = "Generate Weekly Plan"
            ),
            contentCalendarSummary = ContentCalendarSummary(0, 0, 0, 0, 0, "No scheduled content yet", "-", 0f),
            latestRetrospective = null,
            progressHistorySummary = WeeklyProgressHistorySummary(null, emptyList(), 0f)
        )

        assertEquals("No retrospective yet", summary.latestRetrospectiveLabel)
        assertFalse(summary.hasProgressHistory)
    }

    @Test
    fun mapsWeeklyCalendarRetrospectiveAndTrendData() {
        val summary = DashboardContinuitySummaryMapper.from(
            weeklyPlanSummary = WeeklyPlanDashboardSummary(
                hasPlan = true,
                focusTitle = "Improve sales",
                taskProgressLabel = "3/5 tasks",
                taskProgress = 0.6f,
                milestoneProgressLabel = "2/4 milestones",
                milestoneProgress = 0.5f,
                nextTaskTitle = "Post promo",
                ctaLabel = "Open Weekly Plan"
            ),
            contentCalendarSummary = ContentCalendarSummary(4, 2, 1, 1, 0, "Promo post", "2026-07-21", 0.25f),
            latestRetrospective = WeeklyRetrospective(
                weekLabel = "Week 30",
                generatedDate = "2026-07-20",
                summaryTitle = "Review",
                sections = emptyList(),
                nextWeekSuggestion = NextWeekSuggestion("Keep tracking", "Progress exists.", "Save another snapshot.")
            ),
            progressHistorySummary = WeeklyProgressHistorySummary(
                latestSnapshot = sampleSnapshot(),
                trendPoints = listOf(ProgressTrendPoint("Week 30", 0.6f, 72)),
                averageTaskCompletionRate = 0.6f
            )
        )

        assertEquals("3/5 tasks", summary.taskCompletionLabel)
        assertEquals(2, summary.plannedContentCount)
        assertEquals("Promo post", summary.nextScheduledContent)
        assertEquals("Keep tracking", summary.latestRetrospectiveTakeaway)
        assertTrue(summary.hasProgressHistory)
    }

    private fun sampleSnapshot(): WeeklyProgressSnapshot {
        return WeeklyProgressSnapshot(
            weekLabel = "Week 30",
            weekStartDate = "2026-07-20",
            totalTasks = 5,
            completedTasks = 3,
            taskCompletionRate = 0.6f,
            milestoneProgress = 0.5f,
            weeklyIncome = 1L,
            weeklyExpenses = 0L,
            weeklyEstimatedProfit = 1L,
            profitMarginPercent = 100,
            savedIdeasCount = 2,
            plannedContentCount = 1,
            postedOrDoneContentCount = 1,
            skippedContentCount = 0,
            businessHealthScore = 72,
            warningInsightCount = 0,
            criticalInsightCount = 0
        )
    }
}
