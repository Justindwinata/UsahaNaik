package com.justindwinata.usahanaik.domain.weekly

import com.justindwinata.usahanaik.domain.diagnosis.BusinessDiagnosisEngine
import com.justindwinata.usahanaik.domain.model.AvailableTime
import com.justindwinata.usahanaik.domain.model.BusinessChallenge
import com.justindwinata.usahanaik.domain.model.BusinessProfile
import com.justindwinata.usahanaik.domain.model.BusinessSetupDraft
import com.justindwinata.usahanaik.domain.model.FinancialTrackingSummary
import com.justindwinata.usahanaik.domain.model.InsightCategory
import com.justindwinata.usahanaik.domain.model.StockIssue
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class WeeklyPlanGeneratorTest {
    private val generator = WeeklyPlanGenerator()
    private val diagnosisEngine = BusinessDiagnosisEngine()

    @Test
    fun generatesPlanForNoFinancialRecords() {
        val plan = generate(
            draft = draft(categoryId = "food_beverage"),
            summary = FinancialTrackingSummary()
        )

        assertEquals("Build financial tracking discipline", plan.focus.title)
        assertEquals(InsightCategory.Finance, plan.focus.category)
        assertTrue(plan.tasks.size in 5..7)
        assertTrue(plan.milestones.size in 3..5)
        assertTrue(plan.challenge.title.contains("Tracking"))
    }

    @Test
    fun generatesFinanceFocusedPlanForPoorRecords() {
        val plan = generate(
            draft = draft(
                categoryId = "warung_kelontong",
                challenges = setOf(BusinessChallenge.PoorFinancialRecords)
            )
        )

        assertEquals("Build financial tracking discipline", plan.focus.title)
        assertTrue(plan.tasks.any { it.title.contains("Catat semua pemasukan") })
    }

    @Test
    fun generatesExpenseFocusedPlanForHighExpenses() {
        val plan = generate(
            draft = draft(categoryId = "online_shop_reseller"),
            summary = summary(totalIncome = 10_000_000L, totalExpenses = 8_000_000L)
        )

        assertEquals("Control operating costs", plan.focus.title)
        assertTrue(plan.tasks.any { it.category == InsightCategory.Expense })
        assertTrue(plan.challenge.title.contains("Expense"))
    }

    @Test
    fun generatesSalesFocusedPlanForLowSales() {
        val plan = generate(
            draft = draft(
                categoryId = "food_beverage",
                challenges = setOf(BusinessChallenge.LowSales)
            )
        )

        assertEquals("Improve daily sales activity", plan.focus.title)
        assertTrue(plan.tasks.any { it.category == InsightCategory.Sales })
    }

    @Test
    fun generatesContentFocusedPlanForInconsistentContent() {
        val plan = generate(
            draft = draft(
                categoryId = "online_shop_reseller",
                challenges = setOf(BusinessChallenge.InconsistentContent)
            )
        )

        assertEquals("Create consistent promotional content", plan.focus.title)
        assertTrue(plan.challenge.title.contains("Content"))
        assertTrue(plan.tasks.any { it.title.contains("3 konten") || it.title.contains("3 posting") })
    }

    @Test
    fun generatesSkincareCategoryTasks() {
        val plan = generate(draft = draft(categoryId = "skincare_beauty"))

        assertTrue(plan.tasks.any { it.title == "Kumpulkan satu testimoni pelanggan" })
    }

    @Test
    fun generatesWarungCategoryTasks() {
        val plan = generate(draft = draft(categoryId = "warung_kelontong"))

        assertTrue(plan.tasks.any { it.title == "Pisahkan uang usaha dan pribadi minggu ini" })
    }

    @Test
    fun adjustsTaskCountByAvailableWeeklyTime() {
        val lowTimePlan = generate(
            draft = draft(categoryId = "food_beverage", availableTime = AvailableTime.UnderThreeHours)
        )
        val highTimePlan = generate(
            draft = draft(categoryId = "food_beverage", availableTime = AvailableTime.MoreThanTenHours)
        )

        assertEquals(5, lowTimePlan.tasks.size)
        assertEquals(7, highTimePlan.tasks.size)
    }

    @Test
    fun generatesStockFocusedPlanForStockIssue() {
        val plan = generate(
            draft = draft(
                categoryId = "other_business",
                stockIssue = StockIssue.SlowMovingStock
            )
        )

        assertEquals("Improve stock control and fast-moving product tracking", plan.focus.title)
        assertTrue(plan.tasks.any { it.category == InsightCategory.Stock })
    }

    private fun generate(
        draft: BusinessSetupDraft,
        summary: FinancialTrackingSummary = summary()
    ) = generator.generate(
        profile = BusinessProfile(draft = draft, createdAt = 1L, updatedAt = 1L),
        financialSummary = summary,
        diagnosis = diagnosisEngine.diagnose(
            BusinessProfile(draft = draft, createdAt = 1L, updatedAt = 1L),
            summary
        )
    )

    private fun draft(
        categoryId: String,
        challenges: Set<BusinessChallenge> = emptySet(),
        stockIssue: StockIssue = StockIssue.NoStockIssue,
        availableTime: AvailableTime = AvailableTime.SixToTenHours
    ): BusinessSetupDraft {
        return BusinessSetupDraft(
            businessName = "Toko Maju",
            categoryId = categoryId,
            challenges = challenges,
            stockIssue = stockIssue,
            targetMonthlyRevenue = "Rp 12.000.000",
            targetMonthlyProfit = "Rp 4.000.000",
            availableTime = availableTime
        )
    }

    private fun summary(
        totalIncome: Long = 8_000_000L,
        totalExpenses: Long = 4_000_000L
    ): FinancialTrackingSummary {
        return FinancialTrackingSummary(
            totalIncome = totalIncome,
            totalExpenses = totalExpenses,
            estimatedProfit = totalIncome - totalExpenses,
            profitMarginPercent = if (totalIncome == 0L) 0 else (((totalIncome - totalExpenses).toDouble() / totalIncome) * 100).toInt(),
            largestExpenseCategory = "Raw materials",
            incomeEntryCount = 2,
            expenseEntryCount = 2,
            targetRevenueProgress = 0.6f,
            targetProfitProgress = 0.5f
        )
    }
}
