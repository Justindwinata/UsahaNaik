package com.justindwinata.usahanaik.domain.progress

import com.justindwinata.usahanaik.domain.model.ContentCalendarSummary
import com.justindwinata.usahanaik.domain.model.ProgressTrendPoint
import com.justindwinata.usahanaik.domain.model.WeeklyProgressHistorySummary
import com.justindwinata.usahanaik.domain.model.WeeklyRetrospective
import com.justindwinata.usahanaik.domain.weekly.WeeklyPlanDashboardSummary

data class DashboardContinuitySummary(
    val taskCompletionLabel: String,
    val taskCompletionProgress: Float,
    val milestoneProgressLabel: String,
    val milestoneProgress: Float,
    val plannedContentCount: Int,
    val postedOrDoneContentCount: Int,
    val nextScheduledContent: String,
    val latestRetrospectiveLabel: String,
    val latestRetrospectiveTakeaway: String,
    val trendPoints: List<ProgressTrendPoint>,
    val hasProgressHistory: Boolean
)

object DashboardContinuitySummaryMapper {
    fun from(
        weeklyPlanSummary: WeeklyPlanDashboardSummary,
        contentCalendarSummary: ContentCalendarSummary,
        latestRetrospective: WeeklyRetrospective?,
        progressHistorySummary: WeeklyProgressHistorySummary
    ): DashboardContinuitySummary {
        return DashboardContinuitySummary(
            taskCompletionLabel = weeklyPlanSummary.taskProgressLabel,
            taskCompletionProgress = weeklyPlanSummary.taskProgress,
            milestoneProgressLabel = weeklyPlanSummary.milestoneProgressLabel,
            milestoneProgress = weeklyPlanSummary.milestoneProgress,
            plannedContentCount = contentCalendarSummary.plannedCount,
            postedOrDoneContentCount = contentCalendarSummary.postedOrDoneCount,
            nextScheduledContent = contentCalendarSummary.nextScheduledTitle,
            latestRetrospectiveLabel = latestRetrospective?.weekLabel ?: "No retrospective yet",
            latestRetrospectiveTakeaway = latestRetrospective?.nextWeekSuggestion?.focus
                ?: "Generate a weekly retrospective to review progress continuity.",
            trendPoints = progressHistorySummary.trendPoints,
            hasProgressHistory = progressHistorySummary.hasHistory
        )
    }
}
