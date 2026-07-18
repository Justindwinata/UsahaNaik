package com.justindwinata.usahanaik.domain.finance

import com.justindwinata.usahanaik.domain.model.FinancialSummary
import com.justindwinata.usahanaik.domain.model.FinancialTrackingSummary
import com.justindwinata.usahanaik.domain.model.FinancialTrend
import com.justindwinata.usahanaik.domain.setup.BusinessSetupCalculator

data class FinancialDashboardMetrics(
    val monthlyRevenue: String,
    val monthlyExpenses: String,
    val estimatedProfit: String,
    val profitMargin: String,
    val revenueTrendPoints: List<Float>,
    val expenseTrendPoints: List<Float>,
    val largestExpenseCategory: String,
    val targetRevenueProgress: Float,
    val targetProfitProgress: Float,
    val reportSummary: String,
    val hasEntries: Boolean
)

object FinancialDashboardMetricsMapper {
    fun from(
        summary: FinancialTrackingSummary,
        fallbackSummary: FinancialSummary,
        fallbackTrend: FinancialTrend
    ): FinancialDashboardMetrics {
        if (!summary.hasEntries) {
            return FinancialDashboardMetrics(
                monthlyRevenue = fallbackSummary.monthlyRevenue,
                monthlyExpenses = fallbackSummary.monthlyExpenses,
                estimatedProfit = fallbackSummary.estimatedProfit,
                profitMargin = fallbackSummary.profitMargin,
                revenueTrendPoints = fallbackTrend.revenuePoints,
                expenseTrendPoints = fallbackTrend.expensePoints,
                largestExpenseCategory = "-",
                targetRevenueProgress = 0f,
                targetProfitProgress = 0f,
                reportSummary = "Start recording income and expenses to make your dashboard more accurate.",
                hasEntries = false
            )
        }

        val trendPoints = summary.trendPoints.ifEmpty {
            listOf(
                com.justindwinata.usahanaik.domain.model.FinancialTrendPoint(
                    label = "Today",
                    income = summary.totalIncome,
                    expenses = summary.totalExpenses
                )
            )
        }

        return FinancialDashboardMetrics(
            monthlyRevenue = BusinessSetupCalculator.formatRupiah(summary.totalIncome),
            monthlyExpenses = BusinessSetupCalculator.formatRupiah(summary.totalExpenses),
            estimatedProfit = BusinessSetupCalculator.formatRupiah(summary.estimatedProfit),
            profitMargin = "${summary.profitMarginPercent}%",
            revenueTrendPoints = trendPoints.map { it.income.toChartPoint() },
            expenseTrendPoints = trendPoints.map { it.expenses.toChartPoint() },
            largestExpenseCategory = summary.largestExpenseCategory,
            targetRevenueProgress = summary.targetRevenueProgress,
            targetProfitProgress = summary.targetProfitProgress,
            reportSummary = "Based on ${summary.incomeEntryCount} income entries and ${summary.expenseEntryCount} expense entries saved locally.",
            hasEntries = true
        )
    }

    private fun Long.toChartPoint(): Float = (this / 1_000_000f).coerceAtLeast(0f)
}
