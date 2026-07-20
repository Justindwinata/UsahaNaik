package com.justindwinata.usahanaik.domain.report

import com.justindwinata.usahanaik.domain.model.BusinessReport
import com.justindwinata.usahanaik.domain.model.BusinessReportPeriod
import com.justindwinata.usahanaik.domain.model.ContentPerformanceSummary
import com.justindwinata.usahanaik.domain.model.ExportReadyReport
import com.justindwinata.usahanaik.domain.model.FinancialReportSummary
import com.justindwinata.usahanaik.domain.model.ReportChartData
import com.justindwinata.usahanaik.domain.model.ReportDiagnosisSummary
import com.justindwinata.usahanaik.domain.model.ReportRetrospectiveSummary
import com.justindwinata.usahanaik.domain.model.WeeklyExecutionSummary
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class BusinessReportDashboardMapperTest {
    @Test
    fun mapsMissingReportToDashboardEmptyState() {
        val summary = BusinessReportDashboardMapper.from(null)

        assertFalse(summary.hasReport)
        assertEquals("No report yet", summary.periodLabel)
        assertTrue(summary.helper.contains("Complete business setup"))
    }

    @Test
    fun mapsBusinessReportToCompactDashboardSummary() {
        val summary = BusinessReportDashboardMapper.from(sampleReport())

        assertTrue(summary.hasReport)
        assertEquals("This month", summary.periodLabel)
        assertEquals("72/100", summary.healthScore)
        assertEquals("50%", summary.taskCompletion)
        assertEquals("40%", summary.contentExecution)
    }

    private fun sampleReport(): BusinessReport = BusinessReport(
        period = BusinessReportPeriod.ThisMonth,
        businessName = "Kedai Naik",
        categoryName = "food_beverage",
        generatedAt = "2026-07-20",
        kpis = emptyList(),
        financialSummary = FinancialReportSummary(
            totalRevenue = 4_000_000,
            totalExpenses = 2_000_000,
            estimatedProfit = 2_000_000,
            profitMarginPercent = 50,
            largestExpenseCategory = "Raw materials",
            recentEntryCount = 2,
            cautionMessage = "Estimated profit is calculated from local income minus expenses."
        ),
        revenueExpenseChart = ReportChartData("Revenue vs expenses", emptyList()),
        expenseBreakdown = emptyList(),
        weeklyExecution = WeeklyExecutionSummary(
            focusTitle = "Improve daily sales activity",
            completedTasks = 3,
            totalTasks = 6,
            taskCompletionRate = 0.5f,
            milestoneProgress = 0.5f,
            nextTaskTitle = "Review offer"
        ),
        contentPerformance = ContentPerformanceSummary(
            savedIdeasCount = 5,
            plannedIdeasCount = 2,
            favoriteIdeasCount = 1,
            doneIdeasCount = 1,
            scheduledContentCount = 5,
            postedOrDoneContentCount = 2,
            skippedContentCount = 0,
            nextScheduledContent = "Menu highlight",
            executionRate = 0.4f
        ),
        diagnosisSummary = ReportDiagnosisSummary(
            healthScore = 72,
            statusLabel = "On track",
            warningCount = 1,
            criticalCount = 0,
            topInsights = listOf("Profit is positive"),
            priorityActions = listOf("Review cost")
        ),
        retrospectiveSummary = ReportRetrospectiveSummary(
            weekLabel = "Week 30",
            keyTakeaway = "Progress improved",
            whatImproved = "Tasks completed",
            needsAttention = "Content consistency",
            nextWeekSuggestion = "Keep recording daily sales"
        ),
        insights = emptyList(),
        exportReadyReport = ExportReadyReport("UsahaNaik Business Report", "Report body"),
        isLimitedData = false
    )
}
