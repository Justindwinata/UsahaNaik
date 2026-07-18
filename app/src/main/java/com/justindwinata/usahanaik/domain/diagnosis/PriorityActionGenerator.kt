package com.justindwinata.usahanaik.domain.diagnosis

import com.justindwinata.usahanaik.domain.model.ActionDifficulty
import com.justindwinata.usahanaik.domain.model.ActionEstimatedTime
import com.justindwinata.usahanaik.domain.model.BusinessChallenge
import com.justindwinata.usahanaik.domain.model.BusinessSetupDraft
import com.justindwinata.usahanaik.domain.model.FinancialTrackingSummary
import com.justindwinata.usahanaik.domain.model.InsightCategory
import com.justindwinata.usahanaik.domain.model.PriorityAction
import com.justindwinata.usahanaik.domain.model.StockIssue

class PriorityActionGenerator {
    fun generate(
        draft: BusinessSetupDraft,
        financialSummary: FinancialTrackingSummary
    ): List<PriorityAction> {
        val actions = mutableListOf<PriorityAction>()

        if (!financialSummary.hasEntries || BusinessChallenge.PoorFinancialRecords in draft.challenges) {
            actions += PriorityAction(
                title = "Record today's income and expenses",
                description = "Add every sale and expense from today before closing the business day.",
                category = InsightCategory.Finance,
                difficulty = ActionDifficulty.Easy,
                estimatedTime = ActionEstimatedTime.UnderFifteenMinutes,
                reason = "Financial clarity is the first signal for useful dashboard insights.",
                expectedOutcome = "May help make weekly decisions easier to review."
            )
        }

        if (financialSummary.hasEntries && financialSummary.totalExpenses > financialSummary.totalIncome * 0.7f) {
            actions += PriorityAction(
                title = "Review your largest expense category",
                description = "Check whether this cost can be reduced, delayed, or negotiated this week.",
                category = InsightCategory.Expense,
                difficulty = ActionDifficulty.Medium,
                estimatedTime = ActionEstimatedTime.FifteenToThirtyMinutes,
                reason = "Expenses are taking a large portion of recorded revenue.",
                expectedOutcome = "May help improve profit margin if unnecessary costs are reduced."
            )
        }

        if (financialSummary.hasEntries && financialSummary.estimatedProfit < 0) {
            actions += PriorityAction(
                title = "Pause non-essential spending",
                description = "List costs that can wait until recorded income is more stable.",
                category = InsightCategory.ProfitMargin,
                difficulty = ActionDifficulty.Medium,
                estimatedTime = ActionEstimatedTime.ThirtyToSixtyMinutes,
                reason = "Recorded expenses are currently higher than income.",
                expectedOutcome = "May reduce short-term pressure while sales activity is improved."
            )
        }

        if (BusinessChallenge.LowSales in draft.challenges || financialSummary.targetRevenueProgress < 0.4f) {
            actions += PriorityAction(
                title = "Push one best-selling offer",
                description = "Promote the product or service that is easiest to sell and explain to customers.",
                category = InsightCategory.Sales,
                difficulty = ActionDifficulty.Medium,
                estimatedTime = ActionEstimatedTime.ThirtyToSixtyMinutes,
                reason = "Sales progress needs focused action before adding more experiments.",
                expectedOutcome = "May help increase recorded revenue if the offer reaches interested customers."
            )
        }

        if (BusinessChallenge.InconsistentContent in draft.challenges) {
            actions += PriorityAction(
                title = "Create three simple content posts",
                description = "Prepare one education post, one testimonial post, and one promotion post.",
                category = InsightCategory.Content,
                difficulty = ActionDifficulty.Easy,
                estimatedTime = ActionEstimatedTime.ThirtyToSixtyMinutes,
                reason = "You selected inconsistent content as a business challenge.",
                expectedOutcome = "May help customers understand the offer more consistently."
            )
        }

        if (BusinessChallenge.StockProblems in draft.challenges || draft.stockIssue in stockAttentionIssues) {
            actions += PriorityAction(
                title = "Check fast-moving and slow-moving stock",
                description = "Mark which products sell quickly and which items have not moved recently.",
                category = InsightCategory.Stock,
                difficulty = ActionDifficulty.Medium,
                estimatedTime = ActionEstimatedTime.ThirtyToSixtyMinutes,
                reason = "Stock issues can reduce sales or trap cash in slow-moving items.",
                expectedOutcome = "May help improve restock decisions and product focus."
            )
        }

        actions += categoryActions[draft.categoryId].orEmpty()
        actions += fallbackActions

        return actions.distinctBy { it.title }.take(5).let { selected ->
            if (selected.size >= 3) selected else (selected + fallbackActions).distinctBy { it.title }.take(3)
        }
    }

    private companion object {
        val stockAttentionIssues = setOf(
            StockIssue.OftenOutOfStock,
            StockIssue.SlowMovingStock
        )

        val categoryActions = mapOf(
            "food_beverage" to listOf(
                PriorityAction(
                    title = "Calculate food cost for one key menu",
                    description = "Choose one popular menu and calculate ingredient cost versus selling price.",
                    category = InsightCategory.Expense,
                    difficulty = ActionDifficulty.Medium,
                    estimatedTime = ActionEstimatedTime.ThirtyToSixtyMinutes,
                    reason = "Food and beverage margins often depend on raw material control.",
                    expectedOutcome = "May help identify whether the menu price or portion needs review."
                ),
                PriorityAction(
                    title = "Create one bundle menu offer",
                    description = "Pair a best-selling menu with a complementary item for this week.",
                    category = InsightCategory.Marketing,
                    difficulty = ActionDifficulty.Easy,
                    estimatedTime = ActionEstimatedTime.FifteenToThirtyMinutes,
                    reason = "Bundles can make menu promotion easier for customers to understand.",
                    expectedOutcome = "May help improve average transaction value."
                )
            ),
            "warung_kelontong" to listOf(
                PriorityAction(
                    title = "Separate business and personal cash",
                    description = "Use a dedicated cash box or account note for business money this week.",
                    category = InsightCategory.Finance,
                    difficulty = ActionDifficulty.Easy,
                    estimatedTime = ActionEstimatedTime.FifteenToThirtyMinutes,
                    reason = "Warung decisions are easier when daily business cash is visible.",
                    expectedOutcome = "May help improve financial records and restock planning."
                ),
                PriorityAction(
                    title = "Review slow-moving items",
                    description = "Write down items that have not sold well and avoid adding more stock first.",
                    category = InsightCategory.Stock,
                    difficulty = ActionDifficulty.Medium,
                    estimatedTime = ActionEstimatedTime.ThirtyToSixtyMinutes,
                    reason = "Slow-moving stock can reduce available cash for fast-moving products.",
                    expectedOutcome = "May help improve stock turnover."
                )
            ),
            "skincare_beauty" to listOf(
                PriorityAction(
                    title = "Follow up previous customers",
                    description = "Message recent buyers with a simple product usage check-in.",
                    category = InsightCategory.CustomerRetention,
                    difficulty = ActionDifficulty.Easy,
                    estimatedTime = ActionEstimatedTime.FifteenToThirtyMinutes,
                    reason = "Skincare and beauty businesses often benefit from repeat orders and trust.",
                    expectedOutcome = "May help increase repeat purchase opportunities."
                ),
                PriorityAction(
                    title = "Collect one testimonial",
                    description = "Ask one satisfied customer for short feedback that can be posted later.",
                    category = InsightCategory.Marketing,
                    difficulty = ActionDifficulty.Easy,
                    estimatedTime = ActionEstimatedTime.UnderFifteenMinutes,
                    reason = "Testimonials can reduce customer hesitation for beauty products.",
                    expectedOutcome = "May help strengthen product credibility."
                )
            ),
            "online_shop_reseller" to listOf(
                PriorityAction(
                    title = "Check marketplace fees",
                    description = "Review one platform fee or admin cost and compare it with product margin.",
                    category = InsightCategory.Expense,
                    difficulty = ActionDifficulty.Medium,
                    estimatedTime = ActionEstimatedTime.FifteenToThirtyMinutes,
                    reason = "Online shops can lose margin through fees that are easy to overlook.",
                    expectedOutcome = "May help prioritize higher-margin products."
                ),
                PriorityAction(
                    title = "Create a weekly promo calendar",
                    description = "Choose one product focus and one promo angle for the next seven days.",
                    category = InsightCategory.Marketing,
                    difficulty = ActionDifficulty.Medium,
                    estimatedTime = ActionEstimatedTime.ThirtyToSixtyMinutes,
                    reason = "A simple promo calendar supports more consistent selling activity.",
                    expectedOutcome = "May help reduce last-minute content decisions."
                )
            ),
            "laundry" to listOf(
                PriorityAction(
                    title = "Offer one weekly laundry package",
                    description = "Create a simple weekly package for repeat customers.",
                    category = InsightCategory.CustomerRetention,
                    difficulty = ActionDifficulty.Medium,
                    estimatedTime = ActionEstimatedTime.ThirtyToSixtyMinutes,
                    reason = "Laundry businesses depend on repeat usage and operational rhythm.",
                    expectedOutcome = "May help improve recurring orders."
                ),
                PriorityAction(
                    title = "Review turnaround delay",
                    description = "Identify one step that slows down order completion this week.",
                    category = InsightCategory.Operations,
                    difficulty = ActionDifficulty.Medium,
                    estimatedTime = ActionEstimatedTime.ThirtyToSixtyMinutes,
                    reason = "Turnaround time affects service quality and repeat customers.",
                    expectedOutcome = "May help improve customer satisfaction."
                )
            )
        )

        val fallbackActions = listOf(
            PriorityAction(
                title = "Review this week's business numbers",
                description = "Compare income, expenses, and estimated profit before choosing the next action.",
                category = InsightCategory.Finance,
                difficulty = ActionDifficulty.Easy,
                estimatedTime = ActionEstimatedTime.FifteenToThirtyMinutes,
                reason = "A weekly review keeps business decisions tied to recorded data.",
                expectedOutcome = "May help clarify which area needs attention first."
            ),
            PriorityAction(
                title = "Choose one main focus for the week",
                description = "Pick one focus: sales, expenses, content, stock, or repeat orders.",
                category = InsightCategory.Operations,
                difficulty = ActionDifficulty.Easy,
                estimatedTime = ActionEstimatedTime.UnderFifteenMinutes,
                reason = "A single focus is easier to execute with limited UMKM time.",
                expectedOutcome = "May help reduce scattered decisions."
            ),
            PriorityAction(
                title = "List three customer questions",
                description = "Write down the questions customers ask most often and turn one into content.",
                category = InsightCategory.Content,
                difficulty = ActionDifficulty.Easy,
                estimatedTime = ActionEstimatedTime.FifteenToThirtyMinutes,
                reason = "Customer questions are practical material for education content.",
                expectedOutcome = "May help customers understand your offer better."
            )
        )
    }
}
