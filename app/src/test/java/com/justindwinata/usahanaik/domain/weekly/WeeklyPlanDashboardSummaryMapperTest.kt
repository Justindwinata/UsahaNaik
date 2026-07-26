package com.justindwinata.usahanaik.domain.weekly

import com.justindwinata.usahanaik.domain.model.ActionDifficulty
import com.justindwinata.usahanaik.domain.model.ActionEstimatedTime
import com.justindwinata.usahanaik.domain.model.BusinessMilestone
import com.justindwinata.usahanaik.domain.model.InsightCategory
import com.justindwinata.usahanaik.domain.model.MilestoneStatus
import com.justindwinata.usahanaik.domain.model.WeeklyChallenge
import com.justindwinata.usahanaik.domain.model.WeeklyGrowthPlan
import com.justindwinata.usahanaik.domain.model.WeeklyPlanFocus
import com.justindwinata.usahanaik.domain.model.WeeklyTask
import com.justindwinata.usahanaik.domain.model.WeeklyTaskStatus
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class WeeklyPlanDashboardSummaryMapperTest {
    @Test
    fun mapsEmptyPlanToGenerateState() {
        val summary = WeeklyPlanDashboardSummaryMapper.from(null)

        assertFalse(summary.hasPlan)
        assertEquals("Generate Weekly Plan", summary.ctaLabel)
        assertEquals("No active weekly plan", summary.focusTitle)
        assertEquals("Generate plan", summary.progressStatusLabel)
    }

    @Test
    fun mapsActivePlanProgress() {
        val summary = WeeklyPlanDashboardSummaryMapper.from(samplePlan())

        assertTrue(summary.hasPlan)
        assertEquals("Build financial tracking discipline", summary.focusTitle)
        assertEquals("1/2 tasks", summary.taskProgressLabel)
        assertEquals("1/2 milestones", summary.milestoneProgressLabel)
        assertEquals("Review expenses", summary.nextTaskTitle)
        assertEquals("In progress", summary.progressStatusLabel)
        assertEquals(0.5f, summary.taskProgress)
        assertEquals(0.5f, summary.milestoneProgress)
    }

    @Test
    fun mapsCompletedPlanToCompletedProgressStatus() {
        val summary = WeeklyPlanDashboardSummaryMapper.from(
            samplePlan(
                taskStatuses = listOf(WeeklyTaskStatus.Completed, WeeklyTaskStatus.Completed),
                milestoneStatuses = listOf(MilestoneStatus.Completed, MilestoneStatus.Completed)
            )
        )

        assertEquals("Completed", summary.progressStatusLabel)
        assertEquals("All weekly tasks completed", summary.nextTaskTitle)
    }

    private fun samplePlan(
        taskStatuses: List<WeeklyTaskStatus> = listOf(WeeklyTaskStatus.Completed, WeeklyTaskStatus.Pending),
        milestoneStatuses: List<MilestoneStatus> = listOf(MilestoneStatus.Completed, MilestoneStatus.NotStarted)
    ): WeeklyGrowthPlan {
        return WeeklyGrowthPlan(
            title = "Weekly Plan",
            generatedDate = "2026-07-19",
            businessName = "Toko Maju",
            businessCategoryId = "food_beverage",
            businessCategoryName = "Food & Beverage",
            focus = WeeklyPlanFocus("Build financial tracking discipline", InsightCategory.Finance, "Need records"),
            target = "Complete tasks",
            priorityReason = "Need records",
            tasks = listOf(
                task("task-1", "Record sales", taskStatuses[0]),
                task("task-2", "Review expenses", taskStatuses[1])
            ),
            challenge = WeeklyChallenge("Challenge", "Description", listOf("A"), "Target", "Copy"),
            milestones = listOf(
                BusinessMilestone("milestone-1", "M1", "D", milestoneStatuses[0], progressPercentage = 100),
                BusinessMilestone("milestone-2", "M2", "D", milestoneStatuses[1], progressPercentage = 0)
            ),
            limitationsNote = "Rule-based"
        )
    }

    private fun task(id: String, title: String, status: WeeklyTaskStatus): WeeklyTask {
        return WeeklyTask(
            id = id,
            title = title,
            description = "Description",
            category = InsightCategory.Finance,
            estimatedTime = ActionEstimatedTime.UnderFifteenMinutes,
            difficulty = ActionDifficulty.Easy,
            status = status,
            reason = "Reason",
            expectedOutcome = "May help."
        )
    }
}
