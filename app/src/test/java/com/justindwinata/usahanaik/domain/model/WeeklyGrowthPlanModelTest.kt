package com.justindwinata.usahanaik.domain.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class WeeklyGrowthPlanModelTest {
    @Test
    fun progressSummaryCountsCompletedTasksAndMilestones() {
        val tasks = listOf(
            task("task-1", WeeklyTaskStatus.Completed),
            task("task-2", WeeklyTaskStatus.Pending),
            task("task-3", WeeklyTaskStatus.Pending)
        )
        val milestones = listOf(
            milestone("milestone-1", MilestoneStatus.Completed),
            milestone("milestone-2", MilestoneStatus.NotStarted)
        )

        val summary = WeeklyProgressSummary.from(tasks, milestones)

        assertEquals(1, summary.completedTasks)
        assertEquals(3, summary.totalTasks)
        assertEquals(0.33333334f, summary.taskProgress)
        assertEquals(1, summary.completedMilestones)
        assertEquals(2, summary.totalMilestones)
        assertEquals(0.5f, summary.milestoneProgress)
        assertEquals("task-2", summary.nextTask!!.id)
    }

    @Test
    fun progressSummaryHandlesEmptyLists() {
        val summary = WeeklyProgressSummary.from(emptyList(), emptyList())

        assertEquals(0, summary.completedTasks)
        assertEquals(0, summary.totalTasks)
        assertEquals(0f, summary.taskProgress)
        assertEquals(0f, summary.milestoneProgress)
        assertNull(summary.nextTask)
    }

    private fun task(id: String, status: WeeklyTaskStatus): WeeklyTask {
        return WeeklyTask(
            id = id,
            title = "Task $id",
            description = "Description",
            category = InsightCategory.Operations,
            estimatedTime = ActionEstimatedTime.FifteenToThirtyMinutes,
            difficulty = ActionDifficulty.Easy,
            status = status,
            reason = "Reason",
            expectedOutcome = "May help with execution."
        )
    }

    private fun milestone(id: String, status: MilestoneStatus): BusinessMilestone {
        return BusinessMilestone(
            id = id,
            title = "Milestone $id",
            description = "Description",
            status = status,
            progressPercentage = if (status == MilestoneStatus.Completed) 100 else 0
        )
    }
}
