package com.justindwinata.usahanaik.data.repository

import com.justindwinata.usahanaik.domain.model.MilestoneStatus
import com.justindwinata.usahanaik.domain.model.WeeklyGrowthPlan
import com.justindwinata.usahanaik.domain.model.WeeklyTaskStatus
import kotlinx.coroutines.flow.Flow

interface WeeklyPlanRepository {
    suspend fun savePlan(plan: WeeklyGrowthPlan): WeeklyGrowthPlan

    suspend fun getActivePlan(): WeeklyGrowthPlan?

    fun observeActivePlan(): Flow<WeeklyGrowthPlan?>

    suspend fun updateTaskStatus(taskId: String, status: WeeklyTaskStatus): WeeklyGrowthPlan?

    suspend fun updateMilestoneStatus(milestoneId: String, status: MilestoneStatus): WeeklyGrowthPlan?

    suspend fun deleteActivePlan()

    suspend fun hasActivePlan(): Boolean
}
