package com.justindwinata.usahanaik.domain.model

data class WeeklyGrowthPlan(
    val id: Long = 0L,
    val title: String,
    val generatedDate: String,
    val businessName: String,
    val businessCategoryId: String?,
    val businessCategoryName: String,
    val focus: WeeklyPlanFocus,
    val target: String,
    val priorityReason: String,
    val tasks: List<WeeklyTask>,
    val challenge: WeeklyChallenge,
    val milestones: List<BusinessMilestone>,
    val status: WeeklyPlanStatus = WeeklyPlanStatus.Active,
    val limitationsNote: String,
    val createdAt: Long = 0L,
    val updatedAt: Long = 0L
) {
    val progressSummary: WeeklyProgressSummary
        get() = WeeklyProgressSummary.from(tasks, milestones)
}

data class WeeklyPlanFocus(
    val title: String,
    val category: InsightCategory,
    val reason: String
)

enum class WeeklyPlanStatus(val label: String) {
    Active("Active"),
    Completed("Completed"),
    Archived("Archived")
}

data class WeeklyTask(
    val id: String,
    val title: String,
    val description: String,
    val category: InsightCategory,
    val estimatedTime: ActionEstimatedTime,
    val difficulty: ActionDifficulty,
    val status: WeeklyTaskStatus = WeeklyTaskStatus.Pending,
    val reason: String,
    val expectedOutcome: String
)

enum class WeeklyTaskStatus(val label: String) {
    Pending("Pending"),
    Completed("Completed")
}

data class WeeklyChallenge(
    val title: String,
    val description: String,
    val checklistItems: List<String>,
    val completionTarget: String,
    val motivationalCopy: String
)

data class BusinessMilestone(
    val id: String,
    val title: String,
    val description: String,
    val status: MilestoneStatus = MilestoneStatus.NotStarted,
    val relatedTaskIds: List<String> = emptyList(),
    val progressPercentage: Int = 0
)

enum class MilestoneStatus(val label: String) {
    NotStarted("Not started"),
    InProgress("In progress"),
    Completed("Completed")
}

data class WeeklyProgressSummary(
    val completedTasks: Int,
    val totalTasks: Int,
    val taskProgress: Float,
    val completedMilestones: Int,
    val totalMilestones: Int,
    val milestoneProgress: Float,
    val nextTask: WeeklyTask?
) {
    companion object {
        fun from(
            tasks: List<WeeklyTask>,
            milestones: List<BusinessMilestone>
        ): WeeklyProgressSummary {
            val completedTasks = tasks.count { it.status == WeeklyTaskStatus.Completed }
            val completedMilestones = milestones.count { it.status == MilestoneStatus.Completed }
            return WeeklyProgressSummary(
                completedTasks = completedTasks,
                totalTasks = tasks.size,
                taskProgress = progress(completedTasks, tasks.size),
                completedMilestones = completedMilestones,
                totalMilestones = milestones.size,
                milestoneProgress = progress(completedMilestones, milestones.size),
                nextTask = tasks.firstOrNull { it.status != WeeklyTaskStatus.Completed }
            )
        }

        private fun progress(completed: Int, total: Int): Float {
            return if (total <= 0) 0f else (completed.toFloat() / total.toFloat()).coerceIn(0f, 1f)
        }
    }
}
