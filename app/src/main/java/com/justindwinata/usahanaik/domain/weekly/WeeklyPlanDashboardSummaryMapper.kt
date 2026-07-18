package com.justindwinata.usahanaik.domain.weekly

import com.justindwinata.usahanaik.domain.model.WeeklyGrowthPlan

data class WeeklyPlanDashboardSummary(
    val hasPlan: Boolean,
    val focusTitle: String,
    val taskProgressLabel: String,
    val milestoneProgressLabel: String,
    val taskProgress: Float,
    val milestoneProgress: Float,
    val nextTaskTitle: String,
    val ctaLabel: String
)

object WeeklyPlanDashboardSummaryMapper {
    fun from(plan: WeeklyGrowthPlan?): WeeklyPlanDashboardSummary {
        if (plan == null) {
            return WeeklyPlanDashboardSummary(
                hasPlan = false,
                focusTitle = "No active weekly plan",
                taskProgressLabel = "0/0 tasks",
                milestoneProgressLabel = "0/0 milestones",
                taskProgress = 0f,
                milestoneProgress = 0f,
                nextTaskTitle = "Generate a weekly growth plan to see your next task.",
                ctaLabel = "Generate Weekly Plan"
            )
        }

        val progress = plan.progressSummary
        return WeeklyPlanDashboardSummary(
            hasPlan = true,
            focusTitle = plan.focus.title,
            taskProgressLabel = "${progress.completedTasks}/${progress.totalTasks} tasks",
            milestoneProgressLabel = "${progress.completedMilestones}/${progress.totalMilestones} milestones",
            taskProgress = progress.taskProgress,
            milestoneProgress = progress.milestoneProgress,
            nextTaskTitle = progress.nextTask?.title ?: "All weekly tasks completed",
            ctaLabel = "Open Weekly Plan"
        )
    }
}
