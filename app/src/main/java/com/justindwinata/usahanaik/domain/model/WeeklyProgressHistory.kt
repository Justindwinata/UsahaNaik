package com.justindwinata.usahanaik.domain.model

data class WeeklyProgressSnapshot(
    val id: Long = 0L,
    val weekLabel: String,
    val weekStartDate: String,
    val totalTasks: Int,
    val completedTasks: Int,
    val taskCompletionRate: Float,
    val milestoneProgress: Float,
    val weeklyIncome: Long,
    val weeklyExpenses: Long,
    val weeklyEstimatedProfit: Long,
    val profitMarginPercent: Int,
    val savedIdeasCount: Int,
    val plannedContentCount: Int,
    val postedOrDoneContentCount: Int,
    val skippedContentCount: Int,
    val businessHealthScore: Int,
    val warningInsightCount: Int,
    val criticalInsightCount: Int,
    val createdAt: Long = 0L,
    val updatedAt: Long = 0L
) {
    val hasProgressData: Boolean
        get() = totalTasks > 0 || weeklyIncome > 0L || weeklyExpenses > 0L || savedIdeasCount > 0
}

data class ProgressMetric(
    val label: String,
    val value: String,
    val helper: String
)

data class ProgressTrendPoint(
    val label: String,
    val taskCompletionRate: Float,
    val businessHealthScore: Int
)

data class WeeklyProgressHistorySummary(
    val latestSnapshot: WeeklyProgressSnapshot?,
    val trendPoints: List<ProgressTrendPoint>,
    val averageTaskCompletionRate: Float
) {
    val hasHistory: Boolean = latestSnapshot != null
}
