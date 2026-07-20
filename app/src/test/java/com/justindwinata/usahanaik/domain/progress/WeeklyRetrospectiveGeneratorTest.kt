package com.justindwinata.usahanaik.domain.progress

import com.justindwinata.usahanaik.domain.model.ActionDifficulty
import com.justindwinata.usahanaik.domain.model.ActionEstimatedTime
import com.justindwinata.usahanaik.domain.model.BusinessMilestone
import com.justindwinata.usahanaik.domain.model.InsightCategory
import com.justindwinata.usahanaik.domain.model.InsightSeverity
import com.justindwinata.usahanaik.domain.model.MilestoneStatus
import com.justindwinata.usahanaik.domain.model.WeeklyChallenge
import com.justindwinata.usahanaik.domain.model.WeeklyGrowthPlan
import com.justindwinata.usahanaik.domain.model.WeeklyPlanFocus
import com.justindwinata.usahanaik.domain.model.WeeklyProgressSnapshot
import com.justindwinata.usahanaik.domain.model.WeeklyTask
import com.justindwinata.usahanaik.domain.model.WeeklyTaskStatus
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class WeeklyRetrospectiveGeneratorTest {
    private val generator = WeeklyRetrospectiveGenerator()

    @Test
    fun generatesCompletedAndMissedTaskSections() {
        val retrospective = generator.generate(
            weekLabel = "Week 30",
            generatedDate = "2026-07-20",
            snapshot = sampleSnapshot(),
            activePlan = samplePlan()
        )

        assertTrue(retrospective.sections.first { it.title == "Completed tasks" }.insights.any {
            it.message == "Record sales"
        })
        assertTrue(retrospective.sections.first { it.title == "Missed tasks" }.insights.any {
            it.message == "Post promotion"
        })
    }

    @Test
    fun generatesFinanceAttentionForNegativeProfit() {
        val retrospective = generator.generate(
            weekLabel = "Week 30",
            generatedDate = "2026-07-20",
            snapshot = sampleSnapshot(profit = -200_000L),
            activePlan = samplePlan()
        )

        val attention = retrospective.sections.first { it.title == "What still needs attention" }
        assertTrue(attention.insights.any { it.severity == InsightSeverity.Critical })
        assertEquals("Review expenses before scaling activity", retrospective.nextWeekSuggestion.focus)
    }

    @Test
    fun generatesContentExecutionSummaryForPostedItems() {
        val retrospective = generator.generate(
            weekLabel = "Week 30",
            generatedDate = "2026-07-20",
            snapshot = sampleSnapshot(postedContent = 3),
            activePlan = samplePlan()
        )

        val content = retrospective.sections.first { it.title == "Content execution summary" }
        assertTrue(content.insights.first().message.contains("3 scheduled content items"))
    }

    @Test
    fun suggestsPostingSavedIdeasWhenNoContentWasPosted() {
        val retrospective = generator.generate(
            weekLabel = "Week 30",
            generatedDate = "2026-07-20",
            snapshot = sampleSnapshot(postedContent = 0, savedIdeas = 5),
            activePlan = samplePlan(allTasksCompleted = true)
        )

        assertEquals("Turn saved ideas into posted content", retrospective.nextWeekSuggestion.focus)
    }

    private fun sampleSnapshot(
        profit: Long = 500_000L,
        postedContent: Int = 1,
        savedIdeas: Int = 4
    ): WeeklyProgressSnapshot {
        return WeeklyProgressSnapshot(
            weekLabel = "Week 30",
            weekStartDate = "2026-07-20",
            totalTasks = 2,
            completedTasks = 1,
            taskCompletionRate = 0.5f,
            milestoneProgress = 0.5f,
            weeklyIncome = 1_000_000L,
            weeklyExpenses = 500_000L,
            weeklyEstimatedProfit = profit,
            profitMarginPercent = 50,
            savedIdeasCount = savedIdeas,
            plannedContentCount = 1,
            postedOrDoneContentCount = postedContent,
            skippedContentCount = 0,
            businessHealthScore = 70,
            warningInsightCount = 1,
            criticalInsightCount = if (profit < 0L) 1 else 0
        )
    }

    private fun samplePlan(allTasksCompleted: Boolean = false): WeeklyGrowthPlan {
        return WeeklyGrowthPlan(
            title = "Week 30 Plan",
            generatedDate = "2026-07-20",
            businessName = "Toko Maju",
            businessCategoryId = "food_beverage",
            businessCategoryName = "Food & Beverage",
            focus = WeeklyPlanFocus("Improve consistency", InsightCategory.Content, "Content rhythm"),
            target = "Post consistently",
            priorityReason = "Build execution rhythm.",
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
                    expectedOutcome = "May help visibility."
                ),
                WeeklyTask(
                    id = "task-2",
                    title = "Post promotion",
                    description = "Post one promotion.",
                    category = InsightCategory.Content,
                    estimatedTime = ActionEstimatedTime.ThirtyToSixtyMinutes,
                    difficulty = ActionDifficulty.Medium,
                    status = if (allTasksCompleted) WeeklyTaskStatus.Completed else WeeklyTaskStatus.Pending,
                    reason = "Improve content consistency.",
                    expectedOutcome = "May help content rhythm."
                )
            ),
            challenge = WeeklyChallenge(
                title = "3 posts",
                description = "Post three content items.",
                checklistItems = listOf("Create", "Review", "Post"),
                completionTarget = "3 posts",
                motivationalCopy = "Keep it simple."
            ),
            milestones = listOf(
                BusinessMilestone("milestone-1", "Post content", "Post one content item.", MilestoneStatus.InProgress)
            ),
            limitationsNote = "Rule-based suggestion."
        )
    }
}
