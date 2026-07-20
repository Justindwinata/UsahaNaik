package com.justindwinata.usahanaik.domain.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class BusinessReportModelTest {
    @Test
    fun reportPeriodLabelsAreOwnerFriendly() {
        assertEquals("This week", BusinessReportPeriod.ThisWeek.label)
        assertEquals("This month", BusinessReportPeriod.ThisMonth.label)
        assertEquals("Last 30 days", BusinessReportPeriod.LastThirtyDays.label)
        assertEquals("All local data", BusinessReportPeriod.AllTime.label)
    }

    @Test
    fun exportReadyReportIncludesSafeDefaultDisclaimer() {
        val export = ExportReadyReport(
            title = "UsahaNaik Business Report",
            body = "Business: Toko Maju"
        )

        assertTrue(export.disclaimer.contains("local app data"))
        assertTrue(export.disclaimer.contains("not professional financial advice"))
        assertTrue(export.disclaimer.contains("official accounting/tax document"))
    }

    @Test
    fun businessReportSnapshotStoresCoreExportMetrics() {
        val snapshot = BusinessReportSnapshot(
            period = BusinessReportPeriod.ThisMonth,
            businessName = "Toko Maju",
            generatedAt = "2026-07-20",
            headlineSummary = "Estimated profit is positive.",
            exportReadyText = "Report body",
            healthScore = 72,
            totalRevenue = 10_000_000L,
            totalExpenses = 7_000_000L,
            estimatedProfit = 3_000_000L,
            taskCompletionRate = 0.6f,
            contentExecutionRate = 0.5f
        )

        assertEquals(3_000_000L, snapshot.estimatedProfit)
        assertEquals(0.6f, snapshot.taskCompletionRate)
    }
}
