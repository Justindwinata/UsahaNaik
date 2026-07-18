package com.justindwinata.usahanaik.domain.model

data class BusinessDiagnosis(
    val healthScore: DiagnosisHealthScore,
    val breakdown: List<DiagnosisScoreBreakdown>,
    val insights: List<BusinessInsight>,
    val riskSignals: List<BusinessRiskSignal>,
    val priorityActions: List<PriorityAction>,
    val summary: DashboardInsightSummary
)

data class DiagnosisHealthScore(
    val score: Int,
    val statusLabel: String,
    val explanation: String
)

data class DiagnosisScoreBreakdown(
    val component: DiagnosisScoreComponent,
    val score: Int,
    val maxScore: Int,
    val explanation: String
) {
    val progress: Float
        get() = if (maxScore <= 0) 0f else (score.toFloat() / maxScore.toFloat()).coerceIn(0f, 1f)
}

enum class DiagnosisScoreComponent(val label: String) {
    FinancialClarity("Financial clarity"),
    Profitability("Profitability"),
    GoalProgress("Goal progress"),
    ExpenseControl("Expense control"),
    ExecutionReadiness("Execution readiness"),
    CategoryFit("Category fit")
}

data class BusinessInsight(
    val title: String,
    val message: String,
    val category: InsightCategory,
    val severity: InsightSeverity
)

enum class InsightSeverity(val label: String) {
    Positive("Positive"),
    Info("Info"),
    Warning("Warning"),
    Critical("Critical")
}

enum class InsightCategory(val label: String) {
    Finance("Finance"),
    Sales("Sales"),
    Expense("Expense"),
    ProfitMargin("Profit margin"),
    Operations("Operations"),
    Marketing("Marketing"),
    Content("Content"),
    CustomerRetention("Customer retention"),
    Stock("Stock"),
    GoalProgress("Goal progress")
}

data class BusinessRiskSignal(
    val title: String,
    val message: String,
    val category: InsightCategory,
    val severity: InsightSeverity
)

data class PriorityAction(
    val title: String,
    val description: String,
    val category: InsightCategory,
    val difficulty: ActionDifficulty,
    val estimatedTime: ActionEstimatedTime,
    val reason: String,
    val expectedOutcome: String
)

enum class ActionDifficulty(val label: String) {
    Easy("Easy"),
    Medium("Medium"),
    Hard("Hard")
}

enum class ActionEstimatedTime(val label: String) {
    UnderFifteenMinutes("Under 15 minutes"),
    FifteenToThirtyMinutes("15-30 minutes"),
    ThirtyToSixtyMinutes("30-60 minutes"),
    MoreThanOneHour("More than 1 hour")
}

data class DashboardInsightSummary(
    val financeInsightCount: Int,
    val warningCount: Int,
    val priorityActionCount: Int,
    val goalProgressStatus: String,
    val emptyStateMessage: String? = null
)
