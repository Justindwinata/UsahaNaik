package com.justindwinata.usahanaik.domain.finance

import com.justindwinata.usahanaik.domain.model.FinancialEntry
import com.justindwinata.usahanaik.domain.model.FinancialEntryType
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class FinancialCalculatorTest {
    @Test
    fun emptyEntriesReturnZeroSummary() {
        val summary = FinancialCalculator.buildSummary(emptyList())

        assertEquals(0L, summary.totalIncome)
        assertEquals(0L, summary.totalExpenses)
        assertEquals(0L, summary.estimatedProfit)
        assertEquals(0, summary.profitMarginPercent)
        assertEquals("-", summary.largestExpenseCategory)
        assertFalse(summary.hasEntries)
    }

    @Test
    fun calculatesIncomeExpenseProfitAndMargin() {
        val summary = FinancialCalculator.buildSummary(sampleEntries())

        assertEquals(1_500_000L, summary.totalIncome)
        assertEquals(650_000L, summary.totalExpenses)
        assertEquals(850_000L, summary.estimatedProfit)
        assertEquals(57, summary.profitMarginPercent)
        assertEquals(50_000L, summary.averageDailyIncome)
        assertTrue(summary.hasEntries)
    }

    @Test
    fun calculatesTargetProgressAndLargestExpenseCategory() {
        val summary = FinancialCalculator.buildSummary(
            entries = sampleEntries(),
            targetMonthlyRevenue = "Rp 3.000.000",
            targetMonthlyProfit = "Rp 1.000.000"
        )

        assertEquals(0.5f, summary.targetRevenueProgress)
        assertEquals(0.85f, summary.targetProfitProgress)
        assertEquals("Raw materials", summary.largestExpenseCategory)
    }

    @Test
    fun parsesPositiveAmountsOnly() {
        assertEquals(1_500_000L, FinancialCalculator.parsePositiveAmount("1,5 juta"))
        assertEquals(null, FinancialCalculator.parsePositiveAmount("0"))
        assertEquals(null, FinancialCalculator.parsePositiveAmount("-1"))
    }

    @Test
    fun buildsTrendPointsByDate() {
        val summary = FinancialCalculator.buildSummary(sampleEntries())

        assertEquals(2, summary.trendPoints.size)
        assertEquals("07-18", summary.trendPoints.first().label)
        assertEquals(1_000_000L, summary.trendPoints.first().income)
    }

    private fun sampleEntries(): List<FinancialEntry> = listOf(
        FinancialEntry(
            id = 1,
            type = FinancialEntryType.Income,
            title = "Sales",
            amount = 1_000_000,
            category = "Product sales",
            date = "2026-07-18"
        ),
        FinancialEntry(
            id = 2,
            type = FinancialEntryType.Income,
            title = "Repeat order",
            amount = 500_000,
            category = "Repeat order",
            date = "2026-07-19"
        ),
        FinancialEntry(
            id = 3,
            type = FinancialEntryType.Expense,
            title = "Stock",
            amount = 450_000,
            category = "Raw materials",
            date = "2026-07-19"
        ),
        FinancialEntry(
            id = 4,
            type = FinancialEntryType.Expense,
            title = "Ads",
            amount = 200_000,
            category = "Ads / promotion",
            date = "2026-07-19"
        )
    )
}
