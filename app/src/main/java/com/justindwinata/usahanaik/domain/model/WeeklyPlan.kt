package com.justindwinata.usahanaik.domain.model

data class WeeklyPlan(
    val title: String,
    val target: String,
    val priorityActions: List<String>,
    val dailyTasks: List<BusinessTask>,
    val challenge: String,
    val milestoneProgress: Float
)
