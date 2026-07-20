package com.justindwinata.usahanaik.domain.report

import com.justindwinata.usahanaik.domain.model.BusinessReport
import java.text.NumberFormat
import java.util.Locale

data class BusinessReportDashboardSummary(
    val hasReport: Boolean,
    val periodLabel: String,
    val estimatedProfit: String,
    val healthScore: String,
    val taskCompletion: String,
    val contentExecution: String,
    val helper: String
)

object BusinessReportDashboardMapper {
    fun from(report: BusinessReport?): BusinessReportDashboardSummary {
        if (report == null || report.businessName == "Complete business setup first") {
            return BusinessReportDashboardSummary(
                hasReport = false,
                periodLabel = "No report yet",
                estimatedProfit = "-",
                healthScore = "-",
                taskCompletion = "-",
                contentExecution = "-",
                helper = "Complete business setup and add activity data to generate a business report."
            )
        }

        return BusinessReportDashboardSummary(
            hasReport = true,
            periodLabel = report.period.label,
            estimatedProfit = formatCurrency(report.financialSummary.estimatedProfit),
            healthScore = "${report.diagnosisSummary.healthScore}/100",
            taskCompletion = formatPercent(report.weeklyExecution.taskCompletionRate),
            contentExecution = formatPercent(report.contentPerformance.executionRate),
            helper = if (report.isLimitedData) {
                "Add more financial, task, and content activity to make this report richer."
            } else {
                "Report generated from local finance, plan, content, and retrospective data."
            }
        )
    }

    private fun formatCurrency(value: Long): String {
        val formatter = NumberFormat.getCurrencyInstance(Locale("id", "ID")).apply {
            maximumFractionDigits = 0
        }
        return formatter.format(value)
    }

    private fun formatPercent(value: Float): String = "${(value.coerceIn(0f, 1f) * 100).toInt()}%"
}
