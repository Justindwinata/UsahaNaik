package com.justindwinata.usahanaik.domain.progress

import com.justindwinata.usahanaik.domain.model.BusinessDiagnosis
import com.justindwinata.usahanaik.domain.model.ContentCalendarSummary
import com.justindwinata.usahanaik.domain.model.FinancialTrackingSummary
import com.justindwinata.usahanaik.domain.model.InsightSeverity
import com.justindwinata.usahanaik.domain.model.ProgressTrendPoint
import com.justindwinata.usahanaik.domain.model.WeeklyGrowthPlan
import com.justindwinata.usahanaik.domain.model.WeeklyProgressHistorySummary
import com.justindwinata.usahanaik.domain.model.WeeklyProgressSnapshot

class WeeklyProgressSnapshotGenerator {
    fun generate(
        weekLabel: String,
        weekStartDate: String,
        activePlan: WeeklyGrowthPlan?,
        financialSummary: FinancialTrackingSummary,
        contentCalendarSummary: ContentCalendarSummary,
        savedIdeasCount: Int,
        diagnosis: BusinessDiagnosis?
    ): WeeklyProgressSnapshot {
        val progress = activePlan?.progressSummary
        val warningCount = diagnosis?.insights?.count { it.severity == InsightSeverity.Warning }.orZero() +
            diagnosis?.riskSignals?.count { it.severity == InsightSeverity.Warning }.orZero()
        val criticalCount = diagnosis?.insights?.count { it.severity == InsightSeverity.Critical }.orZero() +
            diagnosis?.riskSignals?.count { it.severity == InsightSeverity.Critical }.orZero()

        return WeeklyProgressSnapshot(
            weekLabel = weekLabel,
            weekStartDate = weekStartDate,
            totalTasks = progress?.totalTasks ?: 0,
            completedTasks = progress?.completedTasks ?: 0,
            taskCompletionRate = progress?.taskProgress ?: 0f,
            milestoneProgress = progress?.milestoneProgress ?: 0f,
            weeklyIncome = financialSummary.totalIncome,
            weeklyExpenses = financialSummary.totalExpenses,
            weeklyEstimatedProfit = financialSummary.estimatedProfit,
            profitMarginPercent = financialSummary.profitMarginPercent,
            savedIdeasCount = savedIdeasCount,
            plannedContentCount = contentCalendarSummary.plannedCount,
            postedOrDoneContentCount = contentCalendarSummary.postedOrDoneCount,
            skippedContentCount = contentCalendarSummary.skippedCount,
            businessHealthScore = diagnosis?.healthScore?.score ?: 0,
            warningInsightCount = warningCount,
            criticalInsightCount = criticalCount
        )
    }

    fun summarizeHistory(snapshots: List<WeeklyProgressSnapshot>): WeeklyProgressHistorySummary {
        val sorted = snapshots.sortedByDescending { it.weekStartDate }
        val trendPoints = sorted
            .takeLast(6)
            .map {
                ProgressTrendPoint(
                    label = it.weekLabel,
                    taskCompletionRate = it.taskCompletionRate,
                    businessHealthScore = it.businessHealthScore
                )
            }
        val averageCompletion = if (snapshots.isEmpty()) {
            0f
        } else {
            snapshots.map { it.taskCompletionRate }.average().toFloat().coerceIn(0f, 1f)
        }
        return WeeklyProgressHistorySummary(
            latestSnapshot = sorted.firstOrNull(),
            trendPoints = trendPoints,
            averageTaskCompletionRate = averageCompletion
        )
    }

    private fun Int?.orZero(): Int = this ?: 0
}
