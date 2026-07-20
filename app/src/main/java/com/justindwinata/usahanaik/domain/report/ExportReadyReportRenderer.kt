package com.justindwinata.usahanaik.domain.report

import com.justindwinata.usahanaik.domain.model.BusinessReport
import com.justindwinata.usahanaik.domain.model.ExportReadyReport
import com.justindwinata.usahanaik.domain.setup.BusinessSetupCalculator

class ExportReadyReportRenderer {
    fun render(report: BusinessReport): ExportReadyReport {
        val body = buildString {
            appendLine("UsahaNaik Business Report")
            appendLine("Business: ${report.businessName}")
            appendLine("Category: ${report.categoryName}")
            appendLine("Period: ${report.period.label}")
            appendLine("Generated: ${report.generatedAt}")
            appendLine()
            appendLine("1. Financial Summary")
            appendLine("Revenue: ${BusinessSetupCalculator.formatRupiah(report.financialSummary.totalRevenue)}")
            appendLine("Expenses: ${BusinessSetupCalculator.formatRupiah(report.financialSummary.totalExpenses)}")
            appendLine("Estimated Profit: ${BusinessSetupCalculator.formatRupiah(report.financialSummary.estimatedProfit)}")
            appendLine("Profit Margin: ${report.financialSummary.profitMarginPercent}%")
            appendLine("Largest Expense Category: ${report.financialSummary.largestExpenseCategory}")
            appendLine()
            appendLine("2. Business Health")
            appendLine("Score: ${report.diagnosisSummary.healthScore}/100")
            appendLine("Status: ${report.diagnosisSummary.statusLabel}")
            appendLine("Main Insight: ${report.diagnosisSummary.topInsights.firstOrNull() ?: "No diagnosis insight yet"}")
            appendLine()
            appendLine("3. Weekly Execution")
            appendLine("Focus: ${report.weeklyExecution.focusTitle}")
            appendLine("Completed Tasks: ${report.weeklyExecution.completedTasks}/${report.weeklyExecution.totalTasks}")
            appendLine("Milestone Progress: ${(report.weeklyExecution.milestoneProgress * 100).toInt()}%")
            appendLine("Next Task: ${report.weeklyExecution.nextTaskTitle}")
            appendLine()
            appendLine("4. Content Activity")
            appendLine("Saved Ideas: ${report.contentPerformance.savedIdeasCount}")
            appendLine("Planned Ideas: ${report.contentPerformance.plannedIdeasCount}")
            appendLine("Scheduled Content: ${report.contentPerformance.scheduledContentCount}")
            appendLine("Posted/Done Content: ${report.contentPerformance.postedOrDoneContentCount}")
            appendLine("Skipped Content: ${report.contentPerformance.skippedContentCount}")
            appendLine("Next Content: ${report.contentPerformance.nextScheduledContent}")
            appendLine()
            appendLine("5. Retrospective")
            appendLine("Latest Week: ${report.retrospectiveSummary.weekLabel}")
            appendLine("Key Takeaway: ${report.retrospectiveSummary.keyTakeaway}")
            appendLine("Needs Attention: ${report.retrospectiveSummary.needsAttention}")
            appendLine("Next Week Suggestion: ${report.retrospectiveSummary.nextWeekSuggestion}")
            appendLine()
            appendLine("6. Recommendations")
            report.diagnosisSummary.priorityActions.take(3).ifEmpty {
                listOf("Record more local data to generate stronger recommendations.")
            }.forEach { action ->
                appendLine("- $action")
            }
            appendLine()
            appendLine("Note:")
            appendLine(DISCLAIMER)
        }.trimEnd()

        return ExportReadyReport(
            title = "UsahaNaik Business Report - ${report.period.label}",
            body = body,
            disclaimer = DISCLAIMER
        )
    }

    companion object {
        const val DISCLAIMER = "This report is generated from local app data and is not professional financial advice or an official accounting/tax document."
    }
}
