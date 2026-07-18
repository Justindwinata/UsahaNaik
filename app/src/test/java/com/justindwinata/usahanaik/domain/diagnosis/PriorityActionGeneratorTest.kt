package com.justindwinata.usahanaik.domain.diagnosis

import com.justindwinata.usahanaik.domain.model.ActionDifficulty
import com.justindwinata.usahanaik.domain.model.AvailableTime
import com.justindwinata.usahanaik.domain.model.BusinessChallenge
import com.justindwinata.usahanaik.domain.model.BusinessSetupDraft
import com.justindwinata.usahanaik.domain.model.FinancialTrackingSummary
import com.justindwinata.usahanaik.domain.model.InsightCategory
import com.justindwinata.usahanaik.domain.model.StockIssue
import org.junit.Assert.assertTrue
import org.junit.Test

class PriorityActionGeneratorTest {
    private val generator = PriorityActionGenerator()

    @Test
    fun returnsThreeToFiveActionsWithDifficultyAndEstimatedTime() {
        val actions = generator.generate(
            draft = draft(categoryId = "food_beverage"),
            financialSummary = summary()
        )

        assertTrue(actions.size in 3..5)
        assertTrue(actions.all { it.difficulty in ActionDifficulty.entries })
        assertTrue(actions.all { it.estimatedTime.label.isNotBlank() })
    }

    @Test
    fun actionsReflectHighExpenseFinancialIssue() {
        val actions = generator.generate(
            draft = draft(categoryId = "online_shop_reseller"),
            financialSummary = summary(totalIncome = 10_000_000L, totalExpenses = 8_500_000L)
        )

        assertTrue(actions.any { it.title == "Review your largest expense category" })
        assertTrue(actions.any { it.category == InsightCategory.Expense })
    }

    @Test
    fun actionsAreCategoryAwareForFoodAndBeverage() {
        val actions = generator.generate(
            draft = draft(categoryId = "food_beverage"),
            financialSummary = summary()
        )

        assertTrue(actions.any { it.title == "Calculate food cost for one key menu" })
    }

    @Test
    fun actionsAreCategoryAwareForSkincareBeauty() {
        val actions = generator.generate(
            draft = draft(categoryId = "skincare_beauty"),
            financialSummary = summary()
        )

        assertTrue(actions.any { it.title == "Follow up previous customers" })
    }

    @Test
    fun challengeAndStockActionsAreIncluded() {
        val actions = generator.generate(
            draft = draft(
                categoryId = "other_business",
                challenges = setOf(BusinessChallenge.InconsistentContent, BusinessChallenge.StockProblems),
                stockIssue = StockIssue.OftenOutOfStock
            ),
            financialSummary = summary()
        )

        assertTrue(actions.any { it.title == "Create three simple content posts" })
        assertTrue(actions.any { it.title == "Check fast-moving and slow-moving stock" })
    }

    @Test
    fun diagnosisEngineSummaryUsesGeneratedActionCount() {
        val diagnosis = BusinessDiagnosisEngine().diagnose(
            profile = com.justindwinata.usahanaik.domain.model.BusinessProfile(
                draft = draft(categoryId = "laundry"),
                createdAt = 1L,
                updatedAt = 1L
            ),
            financialSummary = summary()
        )

        assertTrue(diagnosis.priorityActions.size in 3..5)
        assertTrue(diagnosis.summary.priorityActionCount in 3..5)
        assertTrue(diagnosis.priorityActions.any { it.title == "Offer one weekly laundry package" })
    }

    private fun draft(
        categoryId: String,
        challenges: Set<BusinessChallenge> = emptySet(),
        stockIssue: StockIssue = StockIssue.NoStockIssue
    ): BusinessSetupDraft {
        return BusinessSetupDraft(
            businessName = "Toko Maju",
            categoryId = categoryId,
            challenges = challenges,
            stockIssue = stockIssue,
            availableTime = AvailableTime.ThreeToFiveHours
        )
    }

    private fun summary(
        totalIncome: Long = 10_000_000L,
        totalExpenses: Long = 5_000_000L
    ): FinancialTrackingSummary {
        return FinancialTrackingSummary(
            totalIncome = totalIncome,
            totalExpenses = totalExpenses,
            estimatedProfit = totalIncome - totalExpenses,
            profitMarginPercent = 50,
            incomeEntryCount = 2,
            expenseEntryCount = 2,
            targetRevenueProgress = 0.5f,
            targetProfitProgress = 0.5f
        )
    }
}
