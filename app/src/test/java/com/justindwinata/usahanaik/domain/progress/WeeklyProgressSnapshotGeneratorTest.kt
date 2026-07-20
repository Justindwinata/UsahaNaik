package com.justindwinata.usahanaik.domain.progress

import com.justindwinata.usahanaik.domain.model.ActionDifficulty
import com.justindwinata.usahanaik.domain.model.ActionEstimatedTime
import com.justindwinata.usahanaik.domain.model.BusinessDiagnosis
import com.justindwinata.usahanaik.domain.model.BusinessInsight
import com.justindwinata.usahanaik.domain.model.BusinessRiskSignal
import com.justindwinata.usahanaik.domain.model.ContentCalendarSummary
import com.justindwinata.usahanaik.domain.model.DashboardInsightSummary
import com.justindwinata.usahanaik.domain.model.DiagnosisHealthScore
import com.justindwinata.usahanaik.domain.model.FinancialTrackingSummary
import com.justindwinata.usahanaik.domain.model.InsightCategory
import com.justindwinata.usahanaik.domain.model.InsightSeverity
import com.justindwinata.usahanaik.domain.model.MilestoneStatus
import com.justindwinata.usahanaik.domain.model.PriorityAction
import com.justindwinata.usahanaik.domain.model.WeeklyChallenge
import com.justindwinata.usahanaik.domain.model.WeeklyGrowthPlan
import com.justindwinata.usahanaik.domain.model.WeeklyPlanFocus
import com.justindwinata.usahanaik.domain.model.WeeklyTask
import com.justindwinata.usahanaik.domain.model.WeeklyTaskStatus
import com.justindwinata.usahanaik.domain.model.BusinessMilestone
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class WeeklyProgressSnapshotGeneratorTest {
    private val generator = WeeklyProgressSnapshotGenerator()

    @Test
    fun generatesSnapshotFromPlanFinanceContentAndDiagnosis() {
        val snapshot = generator.generate(
            weekLabel = "Week 30",
            weekStartDate = "2026-07-20",
            activePlan = samplePlan(),
            financialSummary = FinancialTrackingSummary(
                totalIncome = 1_000_000L,
                totalExpenses = 400_000L,
                estimatedProfit = 600_000L,
                profitMarginPercent = 60
            ),
            contentCalendarSummary = ContentCalendarSummary(
                totalScheduled = 4,
                plannedCount = 1,
                postedCount = 2,
                skippedCount = 1,
                doneCount = 0,
                nextScheduledTitle = "Promo",
                nextScheduledDate = "2026-07-21",
                executionRate = 0.5f
            ),
            savedIdeasCount = 6,
            diagnosis = sampleDiagnosis()
        )

        assertEquals(2, snapshot.totalTasks)
        assertEquals(1, snapshot.completedTasks)
        assertEquals(0.5f, snapshot.taskCompletionRate)
        assertEquals(1_000_000L, snapshot.weeklyIncome)
        assertEquals(2, snapshot.postedOrDoneContentCount)
        assertEquals(72, snapshot.businessHealthScore)
        assertEquals(1, snapshot.warningInsightCount)
        assertEquals(1, snapshot.criticalInsightCount)
        assertTrue(snapshot.hasProgressData)
    }

    @Test
    fun summarizesHistoryWithLatestSnapshotAndTrendPoints() {
        val older = emptySnapshot("Week 29", "2026-07-13", 0.25f, 60)
        val latest = emptySnapshot("Week 30", "2026-07-20", 0.75f, 72)

        val summary = generator.summarizeHistory(listOf(older, latest))

        assertEquals(latest, summary.latestSnapshot)
        assertEquals(2, summary.trendPoints.size)
        assertEquals(0.5f, summary.averageTaskCompletionRate)
    }

    private fun emptySnapshot(
        weekLabel: String,
        weekStartDate: String,
        completion: Float,
        healthScore: Int
    ) = generator.generate(
        weekLabel = weekLabel,
        weekStartDate = weekStartDate,
        activePlan = null,
        financialSummary = FinancialTrackingSummary(),
        contentCalendarSummary = ContentCalendarSummary(0, 0, 0, 0, 0, "-", "-", 0f),
        savedIdeasCount = 0,
        diagnosis = null
    ).copy(taskCompletionRate = completion, businessHealthScore = healthScore)

    private fun samplePlan(): WeeklyGrowthPlan {
        return WeeklyGrowthPlan(
            title = "Week 30 Plan",
            generatedDate = "2026-07-20",
            businessName = "Toko Maju",
            businessCategoryId = "food_beverage",
            businessCategoryName = "Food & Beverage",
            focus = WeeklyPlanFocus("Improve sales", InsightCategory.Sales, "Low sales challenge"),
            target = "Record and review sales activity",
            priorityReason = "Sales activity needs attention.",
            tasks = listOf(
                WeeklyTask(
                    id = "task-1",
                    title = "Record sales",
                    description = "Record daily sales.",
                    category = InsightCategory.Finance,
                    estimatedTime = ActionEstimatedTime.FifteenToThirtyMinutes,
                    difficulty = ActionDifficulty.Easy,
                    status = WeeklyTaskStatus.Completed,
                    reason = "Improve records.",
                    expectedOutcome = "May help clarify daily sales."
                ),
                WeeklyTask(
                    id = "task-2",
                    title = "Create promo",
                    description = "Create one promo post.",
                    category = InsightCategory.Content,
                    estimatedTime = ActionEstimatedTime.ThirtyToSixtyMinutes,
                    difficulty = ActionDifficulty.Medium,
                    reason = "Improve consistency.",
                    expectedOutcome = "May help content rhythm."
                )
            ),
            challenge = WeeklyChallenge(
                title = "7-day tracking",
                description = "Track activity.",
                checklistItems = listOf("Record transactions"),
                completionTarget = "7 days",
                motivationalCopy = "Keep it practical."
            ),
            milestones = listOf(
                BusinessMilestone(
                    id = "milestone-1",
                    title = "Track sales",
                    description = "Complete tracking milestone.",
                    status = MilestoneStatus.Completed,
                    progressPercentage = 100
                )
            ),
            limitationsNote = "Rule-based planning suggestions."
        )
    }

    private fun sampleDiagnosis(): BusinessDiagnosis {
        return BusinessDiagnosis(
            healthScore = DiagnosisHealthScore(72, "On track", "Rule-based score."),
            breakdown = emptyList(),
            insights = listOf(
                BusinessInsight("High expenses", "Review cost.", InsightCategory.Expense, InsightSeverity.Warning)
            ),
            riskSignals = listOf(
                BusinessRiskSignal("Negative signal", "Needs attention.", InsightCategory.Finance, InsightSeverity.Critical)
            ),
            priorityActions = listOf(
                PriorityAction(
                    title = "Review cost",
                    description = "Check spending.",
                    category = InsightCategory.Expense,
                    difficulty = ActionDifficulty.Easy,
                    estimatedTime = ActionEstimatedTime.FifteenToThirtyMinutes,
                    reason = "Expense signal.",
                    expectedOutcome = "May help margin visibility."
                )
            ),
            summary = DashboardInsightSummary(
                financeInsightCount = 1,
                warningCount = 1,
                priorityActionCount = 1,
                goalProgressStatus = "Building"
            )
        )
    }
}
