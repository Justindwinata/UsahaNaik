package com.justindwinata.usahanaik.domain.diagnosis

import com.justindwinata.usahanaik.domain.model.AvailableTime
import com.justindwinata.usahanaik.domain.model.BusinessChallenge
import com.justindwinata.usahanaik.domain.model.BusinessDiagnosis
import com.justindwinata.usahanaik.domain.model.BusinessInsight
import com.justindwinata.usahanaik.domain.model.BusinessProfile
import com.justindwinata.usahanaik.domain.model.BusinessRiskSignal
import com.justindwinata.usahanaik.domain.model.BusinessSetupDraft
import com.justindwinata.usahanaik.domain.model.DashboardInsightSummary
import com.justindwinata.usahanaik.domain.model.DiagnosisHealthScore
import com.justindwinata.usahanaik.domain.model.DiagnosisScoreBreakdown
import com.justindwinata.usahanaik.domain.model.DiagnosisScoreComponent
import com.justindwinata.usahanaik.domain.model.FinancialTrackingSummary
import com.justindwinata.usahanaik.domain.model.InsightCategory
import com.justindwinata.usahanaik.domain.model.InsightSeverity
import com.justindwinata.usahanaik.domain.model.StockIssue

class BusinessDiagnosisEngine {
    fun diagnose(
        profile: BusinessProfile?,
        financialSummary: FinancialTrackingSummary
    ): BusinessDiagnosis {
        if (profile == null) {
            val insight = BusinessInsight(
                title = "Complete business setup first",
                message = "Complete your business setup and record income/expenses to generate better insights.",
                category = InsightCategory.Operations,
                severity = InsightSeverity.Info
            )
            return BusinessDiagnosis(
                healthScore = DiagnosisHealthScore(
                    score = 0,
                    statusLabel = statusLabelFor(0),
                    explanation = "No saved business profile is available yet."
                ),
                breakdown = emptyBreakdown(),
                insights = listOf(insight),
                riskSignals = emptyList(),
                priorityActions = emptyList(),
                summary = DashboardInsightSummary(
                    financeInsightCount = 0,
                    warningCount = 0,
                    priorityActionCount = 0,
                    goalProgressStatus = "Setup needed",
                    emptyStateMessage = insight.message
                )
            )
        }

        val draft = profile.draft
        val breakdown = buildBreakdown(draft, financialSummary)
        val score = breakdown.sumOf { it.score }.coerceIn(0, 100)
        val insights = buildInsights(draft, financialSummary)
        val riskSignals = buildRiskSignals(draft, financialSummary)
        return BusinessDiagnosis(
            healthScore = DiagnosisHealthScore(
                score = score,
                statusLabel = statusLabelFor(score),
                explanation = explanationFor(score, financialSummary)
            ),
            breakdown = breakdown,
            insights = insights,
            riskSignals = riskSignals,
            priorityActions = emptyList(),
            summary = DashboardInsightSummary(
                financeInsightCount = insights.count { it.category in financeCategories },
                warningCount = insights.count { it.severity == InsightSeverity.Warning || it.severity == InsightSeverity.Critical } + riskSignals.size,
                priorityActionCount = 0,
                goalProgressStatus = goalProgressStatus(financialSummary),
                emptyStateMessage = if (!financialSummary.hasEntries) {
                    "Complete your business setup and record income/expenses to generate better insights."
                } else {
                    null
                }
            )
        )
    }

    private fun buildBreakdown(
        draft: BusinessSetupDraft,
        financialSummary: FinancialTrackingSummary
    ): List<DiagnosisScoreBreakdown> {
        return listOf(
            DiagnosisScoreBreakdown(
                component = DiagnosisScoreComponent.FinancialClarity,
                score = financialClarityScore(draft, financialSummary),
                maxScore = 15,
                explanation = if (financialSummary.hasEntries) {
                    "Financial records exist for this month."
                } else {
                    "No financial entries are recorded yet."
                }
            ),
            DiagnosisScoreBreakdown(
                component = DiagnosisScoreComponent.Profitability,
                score = profitabilityScore(financialSummary),
                maxScore = 25,
                explanation = profitabilityExplanation(financialSummary)
            ),
            DiagnosisScoreBreakdown(
                component = DiagnosisScoreComponent.GoalProgress,
                score = goalProgressScore(financialSummary),
                maxScore = 20,
                explanation = "Revenue and profit target progress are reviewed together."
            ),
            DiagnosisScoreBreakdown(
                component = DiagnosisScoreComponent.ExpenseControl,
                score = expenseControlScore(financialSummary),
                maxScore = 15,
                explanation = expenseControlExplanation(financialSummary)
            ),
            DiagnosisScoreBreakdown(
                component = DiagnosisScoreComponent.ExecutionReadiness,
                score = executionReadinessScore(draft),
                maxScore = 15,
                explanation = "Based on selected challenges and available weekly improvement time."
            ),
            DiagnosisScoreBreakdown(
                component = DiagnosisScoreComponent.CategoryFit,
                score = categoryFitScore(draft),
                maxScore = 10,
                explanation = "Based on whether setup data supports category-aware recommendations."
            )
        )
    }

    private fun buildInsights(
        draft: BusinessSetupDraft,
        financialSummary: FinancialTrackingSummary
    ): List<BusinessInsight> {
        val insights = mutableListOf<BusinessInsight>()
        if (!financialSummary.hasEntries) {
            insights += BusinessInsight(
                title = "Financial records are still empty",
                message = "Start recording income and expenses so UsahaNaik can generate more accurate rule-based insights.",
                category = InsightCategory.Finance,
                severity = InsightSeverity.Info
            )
        }

        if (financialSummary.hasEntries && financialSummary.estimatedProfit > 0) {
            insights += BusinessInsight(
                title = "Profit is currently positive",
                message = "Your recorded income is higher than your expenses. Keep tracking daily transactions to confirm the trend.",
                category = InsightCategory.Finance,
                severity = InsightSeverity.Positive
            )
        }

        if (financialSummary.hasEntries && financialSummary.estimatedProfit < 0) {
            insights += BusinessInsight(
                title = "Recorded profit is negative",
                message = "Your recorded expenses are higher than income. Review urgent costs and focus on sales activity this week.",
                category = InsightCategory.ProfitMargin,
                severity = InsightSeverity.Critical
            )
        }

        val expenseRatio = expenseRatio(financialSummary)
        if (expenseRatio >= 0.70f) {
            insights += BusinessInsight(
                title = "Expenses are taking a large share of revenue",
                message = "Your expenses are above 70% of recorded income. Review ${financialSummary.largestExpenseCategory} this week.",
                category = InsightCategory.Expense,
                severity = InsightSeverity.Warning
            )
        }

        if (financialSummary.hasEntries && financialSummary.targetProfitProgress < 0.4f) {
            insights += BusinessInsight(
                title = "Profit target is still far from goal",
                message = "Current recorded profit is below 40% of the monthly target. Focus on high-margin products and repeat customers.",
                category = InsightCategory.GoalProgress,
                severity = InsightSeverity.Critical
            )
        }

        if (BusinessChallenge.PoorFinancialRecords in draft.challenges) {
            insights += BusinessInsight(
                title = "Financial records need attention",
                message = "You selected poor financial records as a challenge. Record sales and expenses daily before reviewing decisions.",
                category = InsightCategory.Finance,
                severity = InsightSeverity.Warning
            )
        }

        if (BusinessChallenge.LowSales in draft.challenges) {
            insights += BusinessInsight(
                title = "Sales progress needs focus",
                message = "You selected low sales as a challenge. Prioritize offers, follow-ups, and best-selling products before adding new ideas.",
                category = InsightCategory.Sales,
                severity = InsightSeverity.Warning
            )
        }

        if (BusinessChallenge.InconsistentContent in draft.challenges) {
            insights += BusinessInsight(
                title = "Content consistency needs attention",
                message = "Start with three simple posts this week: education, testimonial, and promo.",
                category = InsightCategory.Content,
                severity = InsightSeverity.Info
            )
        }

        if (BusinessChallenge.StockProblems in draft.challenges || draft.stockIssue in stockAttentionIssues) {
            insights += BusinessInsight(
                title = "Stock flow needs review",
                message = "Review fast-moving and slow-moving items before the next restock decision.",
                category = InsightCategory.Stock,
                severity = InsightSeverity.Warning
            )
        }

        return insights
    }

    private fun buildRiskSignals(
        draft: BusinessSetupDraft,
        financialSummary: FinancialTrackingSummary
    ): List<BusinessRiskSignal> {
        val risks = mutableListOf<BusinessRiskSignal>()
        if (!financialSummary.hasEntries) {
            risks += BusinessRiskSignal(
                title = "No financial entries yet",
                message = "Dashboard insights are less reliable until income and expenses are recorded.",
                category = InsightCategory.Finance,
                severity = InsightSeverity.Warning
            )
        }
        if (financialSummary.hasEntries && financialSummary.estimatedProfit < 0) {
            risks += BusinessRiskSignal(
                title = "Negative estimated profit",
                message = "Expenses are currently higher than recorded income.",
                category = InsightCategory.ProfitMargin,
                severity = InsightSeverity.Critical
            )
        }
        if (expenseRatio(financialSummary) >= 0.80f) {
            risks += BusinessRiskSignal(
                title = "High expense ratio",
                message = "Expenses are above 80% of recorded income.",
                category = InsightCategory.Expense,
                severity = InsightSeverity.Warning
            )
        }
        if (financialSummary.hasEntries && financialSummary.targetRevenueProgress < 0.35f) {
            risks += BusinessRiskSignal(
                title = "Low revenue target progress",
                message = "Recorded revenue is still below 35% of the monthly target.",
                category = InsightCategory.GoalProgress,
                severity = InsightSeverity.Warning
            )
        }
        if (BusinessChallenge.PoorFinancialRecords in draft.challenges) {
            risks += BusinessRiskSignal(
                title = "Poor financial records selected",
                message = "Daily records should be fixed before relying on deeper recommendations.",
                category = InsightCategory.Finance,
                severity = InsightSeverity.Warning
            )
        }
        return risks
    }

    private fun financialClarityScore(
        draft: BusinessSetupDraft,
        financialSummary: FinancialTrackingSummary
    ): Int {
        var score = if (financialSummary.hasEntries) 12 else 5
        if (financialSummary.incomeEntryCount >= 3 && financialSummary.expenseEntryCount >= 2) score += 3
        if (BusinessChallenge.PoorFinancialRecords in draft.challenges) score -= 4
        return score.coerceIn(0, 15)
    }

    private fun profitabilityScore(financialSummary: FinancialTrackingSummary): Int {
        if (!financialSummary.hasEntries) return 10
        if (financialSummary.estimatedProfit < 0) return 2
        return when {
            financialSummary.profitMarginPercent >= 30 -> 25
            financialSummary.profitMarginPercent >= 20 -> 21
            financialSummary.profitMarginPercent >= 10 -> 16
            financialSummary.profitMarginPercent > 0 -> 10
            else -> 5
        }
    }

    private fun goalProgressScore(financialSummary: FinancialTrackingSummary): Int {
        if (!financialSummary.hasEntries) return 8
        val averageProgress = (financialSummary.targetRevenueProgress + financialSummary.targetProfitProgress) / 2f
        return (averageProgress * 20).toInt().coerceIn(0, 20)
    }

    private fun expenseControlScore(financialSummary: FinancialTrackingSummary): Int {
        if (!financialSummary.hasEntries || financialSummary.totalIncome <= 0L) return 8
        return when {
            expenseRatio(financialSummary) < 0.50f -> 15
            expenseRatio(financialSummary) < 0.65f -> 12
            expenseRatio(financialSummary) < 0.80f -> 7
            else -> 3
        }
    }

    private fun executionReadinessScore(draft: BusinessSetupDraft): Int {
        var score = when (draft.availableTime) {
            AvailableTime.MoreThanTenHours -> 15
            AvailableTime.SixToTenHours -> 13
            AvailableTime.ThreeToFiveHours -> 10
            AvailableTime.UnderThreeHours -> 7
            null -> 6
        }
        score -= draft.challenges.size.coerceAtMost(5)
        if (BusinessChallenge.TimeManagementProblem in draft.challenges) score -= 2
        return score.coerceIn(0, 15)
    }

    private fun categoryFitScore(draft: BusinessSetupDraft): Int {
        var score = if (draft.categoryId != null) 5 else 1
        if (draft.mainFocus != null) score += 2
        if (draft.mainCostDriver != null) score += 2
        if (draft.stockIssue != null) score += 1
        return score.coerceIn(0, 10)
    }

    private fun emptyBreakdown(): List<DiagnosisScoreBreakdown> {
        return DiagnosisScoreComponent.entries.map { component ->
            DiagnosisScoreBreakdown(
                component = component,
                score = 0,
                maxScore = when (component) {
                    DiagnosisScoreComponent.FinancialClarity -> 15
                    DiagnosisScoreComponent.Profitability -> 25
                    DiagnosisScoreComponent.GoalProgress -> 20
                    DiagnosisScoreComponent.ExpenseControl -> 15
                    DiagnosisScoreComponent.ExecutionReadiness -> 15
                    DiagnosisScoreComponent.CategoryFit -> 10
                },
                explanation = "Complete setup to calculate this component."
            )
        }
    }

    private fun profitabilityExplanation(financialSummary: FinancialTrackingSummary): String {
        return when {
            !financialSummary.hasEntries -> "Profitability uses setup baseline until entries exist."
            financialSummary.estimatedProfit < 0 -> "Recorded expenses are higher than income."
            financialSummary.profitMarginPercent >= 20 -> "Recorded profit margin is currently healthy."
            else -> "Recorded profit is positive but margin can improve."
        }
    }

    private fun expenseControlExplanation(financialSummary: FinancialTrackingSummary): String {
        return if (!financialSummary.hasEntries) {
            "Expense control needs income and expense records."
        } else {
            "Expense ratio is reviewed against recorded income."
        }
    }

    private fun explanationFor(score: Int, financialSummary: FinancialTrackingSummary): String {
        return when {
            !financialSummary.hasEntries -> "Rule-based score is limited because no financial entries are recorded yet."
            score < 45 -> "Several business signals need attention this week."
            score < 65 -> "The business foundation is forming, but key signals need consistency."
            score < 80 -> "Core business signals are moving in a healthier direction."
            else -> "Current records show strong momentum, but continue monitoring the basics."
        }
    }

    private fun statusLabelFor(score: Int): String {
        return when {
            score < 45 -> "Needs attention"
            score < 65 -> "Building foundation"
            score < 80 -> "On track"
            else -> "Strong momentum"
        }
    }

    private fun goalProgressStatus(financialSummary: FinancialTrackingSummary): String {
        if (!financialSummary.hasEntries) return "Needs records"
        val averageProgress = (financialSummary.targetRevenueProgress + financialSummary.targetProfitProgress) / 2f
        return when {
            averageProgress >= 0.8f -> "Strong progress"
            averageProgress >= 0.5f -> "On track"
            averageProgress >= 0.25f -> "Needs push"
            else -> "Far from target"
        }
    }

    private fun expenseRatio(financialSummary: FinancialTrackingSummary): Float {
        if (!financialSummary.hasEntries || financialSummary.totalIncome <= 0L) return 0f
        return financialSummary.totalExpenses.toFloat() / financialSummary.totalIncome.toFloat()
    }

    private companion object {
        val financeCategories = setOf(
            InsightCategory.Finance,
            InsightCategory.Expense,
            InsightCategory.ProfitMargin,
            InsightCategory.GoalProgress
        )
        val stockAttentionIssues = setOf(
            StockIssue.OftenOutOfStock,
            StockIssue.SlowMovingStock
        )
    }
}
