package com.justindwinata.usahanaik.domain.diagnosis

import com.justindwinata.usahanaik.domain.model.AvailableTime
import com.justindwinata.usahanaik.domain.model.BusinessChallenge
import com.justindwinata.usahanaik.domain.model.BusinessProfile
import com.justindwinata.usahanaik.domain.model.BusinessSetupDraft
import com.justindwinata.usahanaik.domain.model.CostDriver
import com.justindwinata.usahanaik.domain.model.FinancialTrackingSummary
import com.justindwinata.usahanaik.domain.model.InsightCategory
import com.justindwinata.usahanaik.domain.model.InsightSeverity
import com.justindwinata.usahanaik.domain.model.MonthlyFocus
import com.justindwinata.usahanaik.domain.model.StockIssue
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class BusinessDiagnosisEngineTest {
    private val engine = BusinessDiagnosisEngine()

    @Test
    fun generatesNoDataInsightWhenThereAreNoFinancialEntries() {
        val diagnosis = engine.diagnose(profile = sampleProfile(), financialSummary = FinancialTrackingSummary())

        assertEquals("Needs records", diagnosis.summary.goalProgressStatus)
        assertTrue(diagnosis.summary.emptyStateMessage!!.contains("record income/expenses"))
        assertTrue(diagnosis.insights.any { it.title == "Financial records are still empty" })
        assertTrue(diagnosis.riskSignals.any { it.title == "No financial entries yet" })
    }

    @Test
    fun positiveProfitImprovesScoreAndCreatesPositiveInsight() {
        val diagnosis = engine.diagnose(
            profile = sampleProfile(),
            financialSummary = summary(
                totalIncome = 10_000_000L,
                totalExpenses = 6_000_000L,
                estimatedProfit = 4_000_000L,
                profitMarginPercent = 40,
                targetRevenueProgress = 0.75f,
                targetProfitProgress = 0.8f
            )
        )

        assertTrue(diagnosis.healthScore.score >= 70)
        assertTrue(diagnosis.insights.any { it.title == "Profit is currently positive" && it.severity == InsightSeverity.Positive })
        assertEquals("On track", diagnosis.summary.goalProgressStatus)
    }

    @Test
    fun negativeProfitCreatesCriticalInsightAndRisk() {
        val diagnosis = engine.diagnose(
            profile = sampleProfile(),
            financialSummary = summary(
                totalIncome = 4_000_000L,
                totalExpenses = 6_000_000L,
                estimatedProfit = -2_000_000L,
                profitMarginPercent = -50
            )
        )

        assertTrue(diagnosis.healthScore.score < 55)
        assertTrue(diagnosis.insights.any { it.title == "Recorded profit is negative" && it.severity == InsightSeverity.Critical })
        assertTrue(diagnosis.riskSignals.any { it.title == "Negative estimated profit" })
    }

    @Test
    fun highExpenseRatioCreatesWarningInsight() {
        val diagnosis = engine.diagnose(
            profile = sampleProfile(),
            financialSummary = summary(
                totalIncome = 10_000_000L,
                totalExpenses = 8_200_000L,
                estimatedProfit = 1_800_000L,
                profitMarginPercent = 18,
                largestExpenseCategory = "Raw materials"
            )
        )

        assertTrue(diagnosis.insights.any { it.category == InsightCategory.Expense && it.severity == InsightSeverity.Warning })
        assertTrue(diagnosis.riskSignals.any { it.title == "High expense ratio" })
    }

    @Test
    fun lowTargetProgressCreatesGoalProgressSignals() {
        val diagnosis = engine.diagnose(
            profile = sampleProfile(),
            financialSummary = summary(
                targetRevenueProgress = 0.2f,
                targetProfitProgress = 0.3f
            )
        )

        assertEquals("Needs push", diagnosis.summary.goalProgressStatus)
        assertTrue(diagnosis.riskSignals.any { it.title == "Low revenue target progress" })
        assertTrue(diagnosis.insights.any { it.title == "Profit target is still far from goal" })
    }

    @Test
    fun selectedChallengesCreateRelevantInsights() {
        val diagnosis = engine.diagnose(
            profile = sampleProfile(
                challenges = setOf(
                    BusinessChallenge.PoorFinancialRecords,
                    BusinessChallenge.LowSales,
                    BusinessChallenge.InconsistentContent
                )
            ),
            financialSummary = summary()
        )

        assertTrue(diagnosis.insights.any { it.category == InsightCategory.Finance })
        assertTrue(diagnosis.insights.any { it.category == InsightCategory.Sales })
        assertTrue(diagnosis.insights.any { it.category == InsightCategory.Content })
        assertTrue(diagnosis.riskSignals.any { it.title == "Poor financial records selected" })
    }

    @Test
    fun stockIssueCreatesStockInsight() {
        val diagnosis = engine.diagnose(
            profile = sampleProfile(stockIssue = StockIssue.SlowMovingStock),
            financialSummary = summary()
        )

        assertTrue(diagnosis.insights.any { it.category == InsightCategory.Stock })
    }

    private fun sampleProfile(
        challenges: Set<BusinessChallenge> = emptySet(),
        stockIssue: StockIssue = StockIssue.NoStockIssue
    ): BusinessProfile {
        return BusinessProfile(
            draft = BusinessSetupDraft(
                businessName = "Toko Maju",
                categoryId = "food_beverage",
                mainCostDriver = CostDriver.RawMaterials,
                stockIssue = stockIssue,
                challenges = challenges,
                mainFocus = MonthlyFocus.ImproveSales,
                availableTime = AvailableTime.SixToTenHours
            ),
            createdAt = 1L,
            updatedAt = 1L
        )
    }

    private fun summary(
        totalIncome: Long = 10_000_000L,
        totalExpenses: Long = 5_000_000L,
        estimatedProfit: Long = 5_000_000L,
        profitMarginPercent: Int = 50,
        largestExpenseCategory: String = "Raw materials",
        targetRevenueProgress: Float = 0.5f,
        targetProfitProgress: Float = 0.5f
    ): FinancialTrackingSummary {
        return FinancialTrackingSummary(
            totalIncome = totalIncome,
            totalExpenses = totalExpenses,
            estimatedProfit = estimatedProfit,
            profitMarginPercent = profitMarginPercent,
            largestExpenseCategory = largestExpenseCategory,
            incomeEntryCount = 2,
            expenseEntryCount = 2,
            targetRevenueProgress = targetRevenueProgress,
            targetProfitProgress = targetProfitProgress
        )
    }
}
