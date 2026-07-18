package com.justindwinata.usahanaik.domain.finance

import com.justindwinata.usahanaik.domain.model.FinancialEntry
import com.justindwinata.usahanaik.domain.model.FinancialEntryType
import com.justindwinata.usahanaik.domain.model.FinancialTrackingSummary
import com.justindwinata.usahanaik.domain.model.FinancialTrendPoint
import com.justindwinata.usahanaik.domain.setup.BusinessSetupCalculator
import kotlin.math.roundToInt

object FinancialCalculator {
    fun buildSummary(
        entries: List<FinancialEntry>,
        targetMonthlyRevenue: String? = null,
        targetMonthlyProfit: String? = null
    ): FinancialTrackingSummary {
        val incomeEntries = entries.filter { it.type == FinancialEntryType.Income }
        val expenseEntries = entries.filter { it.type == FinancialEntryType.Expense }
        val totalIncome = incomeEntries.sumOf { it.amount }
        val totalExpenses = expenseEntries.sumOf { it.amount }
        val estimatedProfit = totalIncome - totalExpenses
        val profitMargin = if (totalIncome == 0L) {
            0
        } else {
            ((estimatedProfit.toDouble() / totalIncome.toDouble()) * 100).roundToInt()
        }
        val targetRevenue = targetMonthlyRevenue?.let(BusinessSetupCalculator::parseIndonesianNumber) ?: 0L
        val targetProfit = targetMonthlyProfit?.let(BusinessSetupCalculator::parseIndonesianNumber) ?: 0L

        return FinancialTrackingSummary(
            totalIncome = totalIncome,
            totalExpenses = totalExpenses,
            estimatedProfit = estimatedProfit,
            profitMarginPercent = profitMargin,
            averageDailyIncome = if (incomeEntries.isEmpty()) 0L else totalIncome / 30L,
            largestExpenseCategory = largestExpenseCategory(expenseEntries),
            incomeEntryCount = incomeEntries.size,
            expenseEntryCount = expenseEntries.size,
            targetRevenueProgress = progress(totalIncome, targetRevenue),
            targetProfitProgress = progress(estimatedProfit, targetProfit),
            recentEntries = entries.sortedByDescending { it.date }.take(5),
            trendPoints = buildTrendPoints(entries)
        )
    }

    fun progress(current: Long, target: Long): Float {
        if (target <= 0L) return 0f
        return (current.toFloat() / target.toFloat()).coerceIn(0f, 1f)
    }

    fun parsePositiveAmount(input: String): Long? {
        val parsed = BusinessSetupCalculator.parseIndonesianNumber(input) ?: return null
        return parsed.takeIf { it > 0L }
    }

    private fun largestExpenseCategory(expenseEntries: List<FinancialEntry>): String {
        return expenseEntries
            .groupBy { it.category }
            .mapValues { (_, entries) -> entries.sumOf { it.amount } }
            .maxByOrNull { it.value }
            ?.key ?: "-"
    }

    private fun buildTrendPoints(entries: List<FinancialEntry>): List<FinancialTrendPoint> {
        return entries
            .groupBy { it.date }
            .toSortedMap()
            .map { (date, dayEntries) ->
                FinancialTrendPoint(
                    label = date.takeLast(5),
                    income = dayEntries.filter { it.type == FinancialEntryType.Income }.sumOf { it.amount },
                    expenses = dayEntries.filter { it.type == FinancialEntryType.Expense }.sumOf { it.amount }
                )
            }
            .takeLast(7)
    }
}
