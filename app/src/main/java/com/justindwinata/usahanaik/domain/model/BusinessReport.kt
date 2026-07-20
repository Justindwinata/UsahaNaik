package com.justindwinata.usahanaik.domain.model

data class BusinessReport(
    val period: BusinessReportPeriod,
    val businessName: String,
    val categoryName: String,
    val generatedAt: String,
    val kpis: List<BusinessReportKpi>,
    val financialSummary: FinancialReportSummary,
    val revenueExpenseChart: ReportChartData,
    val expenseBreakdown: List<ReportExpenseBreakdownItem>,
    val weeklyExecution: WeeklyExecutionSummary,
    val contentPerformance: ContentPerformanceSummary,
    val diagnosisSummary: ReportDiagnosisSummary,
    val retrospectiveSummary: ReportRetrospectiveSummary,
    val insights: List<ReportInsight>,
    val exportReadyReport: ExportReadyReport,
    val isLimitedData: Boolean
)

enum class BusinessReportPeriod(val label: String) {
    ThisWeek("This week"),
    ThisMonth("This month"),
    LastThirtyDays("Last 30 days"),
    AllTime("All local data")
}

data class BusinessReportSection(
    val title: String,
    val description: String,
    val insights: List<ReportInsight> = emptyList()
)

data class BusinessReportKpi(
    val label: String,
    val value: String,
    val helper: String,
    val status: ReportKpiStatus = ReportKpiStatus.Neutral
)

enum class ReportKpiStatus {
    Positive,
    Warning,
    Neutral
}

data class FinancialReportSummary(
    val totalRevenue: Long,
    val totalExpenses: Long,
    val estimatedProfit: Long,
    val profitMarginPercent: Int,
    val largestExpenseCategory: String,
    val recentEntryCount: Int,
    val cautionMessage: String
)

data class ReportChartData(
    val title: String,
    val points: List<ReportChartPoint>
)

data class ReportChartPoint(
    val label: String,
    val primaryValue: Long,
    val secondaryValue: Long = 0L
)

data class ReportExpenseBreakdownItem(
    val category: String,
    val amount: Long,
    val percentage: Int
)

data class ContentPerformanceSummary(
    val savedIdeasCount: Int,
    val plannedIdeasCount: Int,
    val favoriteIdeasCount: Int,
    val doneIdeasCount: Int,
    val scheduledContentCount: Int,
    val postedOrDoneContentCount: Int,
    val skippedContentCount: Int,
    val nextScheduledContent: String,
    val executionRate: Float
)

data class WeeklyExecutionSummary(
    val focusTitle: String,
    val completedTasks: Int,
    val totalTasks: Int,
    val taskCompletionRate: Float,
    val milestoneProgress: Float,
    val nextTaskTitle: String
)

data class ReportDiagnosisSummary(
    val healthScore: Int,
    val statusLabel: String,
    val warningCount: Int,
    val criticalCount: Int,
    val topInsights: List<String>,
    val priorityActions: List<String>
)

data class ReportRetrospectiveSummary(
    val weekLabel: String,
    val keyTakeaway: String,
    val whatImproved: String,
    val needsAttention: String,
    val nextWeekSuggestion: String
)

data class ReportInsight(
    val title: String,
    val message: String,
    val severity: InsightSeverity
)

data class ExportReadyReport(
    val title: String,
    val body: String,
    val disclaimer: String = "This report is generated from local app data and is not professional financial advice or an official accounting/tax document."
)

data class BusinessReportSnapshot(
    val id: Long = 0L,
    val period: BusinessReportPeriod,
    val businessName: String,
    val generatedAt: String,
    val headlineSummary: String,
    val exportReadyText: String,
    val healthScore: Int,
    val totalRevenue: Long,
    val totalExpenses: Long,
    val estimatedProfit: Long,
    val taskCompletionRate: Float,
    val contentExecutionRate: Float,
    val createdAt: Long = 0L,
    val updatedAt: Long = 0L
)
