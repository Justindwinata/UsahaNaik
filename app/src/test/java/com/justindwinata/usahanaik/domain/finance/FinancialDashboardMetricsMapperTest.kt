package com.justindwinata.usahanaik.domain.finance

import com.justindwinata.usahanaik.domain.model.ExpenseItem
import com.justindwinata.usahanaik.domain.model.FinancialEntry
import com.justindwinata.usahanaik.domain.model.FinancialEntryType
import com.justindwinata.usahanaik.domain.model.FinancialSummary
import com.justindwinata.usahanaik.domain.model.FinancialTrackingSummary
import com.justindwinata.usahanaik.domain.model.FinancialTrend
import com.justindwinata.usahanaik.domain.model.FinancialTrendPoint
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class FinancialDashboardMetricsMapperTest {
    private val fallbackSummary = FinancialSummary(
        monthlyRevenue = "Rp8.000.000",
        monthlyExpenses = "Rp5.000.000",
        estimatedProfit = "Rp3.000.000",
        profitMargin = "37%",
        expenseBreakdown = listOf(ExpenseItem("Stock", 50)),
        reportSummary = "Baseline"
    )
    private val fallbackTrend = FinancialTrend(
        revenuePoints = listOf(2f, 3f, 4f),
        expensePoints = listOf(1f, 2f, 2f)
    )

    @Test
    fun usesFallbackWhenNoFinancialEntriesExist() {
        val metrics = FinancialDashboardMetricsMapper.from(
            summary = FinancialTrackingSummary(),
            fallbackSummary = fallbackSummary,
            fallbackTrend = fallbackTrend
        )

        assertFalse(metrics.hasEntries)
        assertEquals("Rp8.000.000", metrics.monthlyRevenue)
        assertEquals(fallbackTrend.revenuePoints, metrics.revenueTrendPoints)
        assertEquals("Start recording income and expenses to make your dashboard more accurate.", metrics.reportSummary)
    }

    @Test
    fun mapsPersistedFinancialSummaryForDashboardCards() {
        val summary = FinancialTrackingSummary(
            totalIncome = 12_500_000L,
            totalExpenses = 7_000_000L,
            estimatedProfit = 5_500_000L,
            profitMarginPercent = 44,
            largestExpenseCategory = "Raw materials",
            incomeEntryCount = 2,
            expenseEntryCount = 1,
            targetRevenueProgress = 0.83f,
            targetProfitProgress = 1f,
            recentEntries = listOf(
                FinancialEntry(
                    type = FinancialEntryType.Income,
                    title = "Online sales",
                    amount = 12_500_000L,
                    category = "Online sales",
                    date = "2026-07-19"
                )
            ),
            trendPoints = listOf(
                FinancialTrendPoint("19 Jul", income = 12_500_000L, expenses = 7_000_000L)
            )
        )

        val metrics = FinancialDashboardMetricsMapper.from(
            summary = summary,
            fallbackSummary = fallbackSummary,
            fallbackTrend = fallbackTrend
        )

        assertTrue(metrics.hasEntries)
        assertEquals("Rp12.500.000", metrics.monthlyRevenue)
        assertEquals("Rp7.000.000", metrics.monthlyExpenses)
        assertEquals("Rp5.500.000", metrics.estimatedProfit)
        assertEquals("44%", metrics.profitMargin)
        assertEquals("Raw materials", metrics.largestExpenseCategory)
        assertEquals(listOf(12.5f), metrics.revenueTrendPoints)
        assertEquals(listOf(7f), metrics.expenseTrendPoints)
    }
}
