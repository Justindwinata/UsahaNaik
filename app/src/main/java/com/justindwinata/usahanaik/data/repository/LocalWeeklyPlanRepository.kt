package com.justindwinata.usahanaik.data.repository

import com.justindwinata.usahanaik.data.local.WeeklyGrowthPlanEntity
import com.justindwinata.usahanaik.data.local.WeeklyPlanDao
import com.justindwinata.usahanaik.data.mapper.WeeklyPlanMapper
import com.justindwinata.usahanaik.data.mapper.WeeklyPlanMapper.toEntity
import com.justindwinata.usahanaik.data.mapper.WeeklyPlanMapper.toPlanEntity
import com.justindwinata.usahanaik.domain.model.MilestoneStatus
import com.justindwinata.usahanaik.domain.model.WeeklyGrowthPlan
import com.justindwinata.usahanaik.domain.model.WeeklyTaskStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LocalWeeklyPlanRepository(
    private val dao: WeeklyPlanDao,
    private val nowProvider: () -> Long = { System.currentTimeMillis() }
) : WeeklyPlanRepository {
    override suspend fun savePlan(plan: WeeklyGrowthPlan): WeeklyGrowthPlan {
        val now = nowProvider()
        val planEntity = plan.toPlanEntity(now)
        dao.replaceActivePlan(
            plan = planEntity,
            tasks = plan.tasks.mapIndexed { index, task -> task.toEntity(planEntity.id, index) },
            milestones = plan.milestones.mapIndexed { index, milestone -> milestone.toEntity(planEntity.id, index) }
        )
        return checkNotNull(getActivePlan())
    }

    override suspend fun getActivePlan(): WeeklyGrowthPlan? {
        val plan = dao.getPlan() ?: return null
        return WeeklyPlanMapper.toDomain(
            plan = plan,
            tasks = dao.getTasks(plan.id),
            milestones = dao.getMilestones(plan.id)
        )
    }

    override fun observeActivePlan(): Flow<WeeklyGrowthPlan?> {
        return dao.observePlan().map { plan ->
            if (plan == null) {
                null
            } else {
                WeeklyPlanMapper.toDomain(
                    plan = plan,
                    tasks = dao.getTasks(plan.id),
                    milestones = dao.getMilestones(plan.id)
                )
            }
        }
    }

    override suspend fun updateTaskStatus(
        taskId: String,
        status: WeeklyTaskStatus
    ): WeeklyGrowthPlan? {
        dao.updateTaskStatus(taskId, status.name)
        refreshMilestoneProgress()
        return getActivePlan()
    }

    override suspend fun updateMilestoneStatus(
        milestoneId: String,
        status: MilestoneStatus
    ): WeeklyGrowthPlan? {
        val progress = if (status == MilestoneStatus.Completed) 100 else 0
        dao.updateMilestoneStatus(milestoneId, status.name, progress)
        return getActivePlan()
    }

    override suspend fun deleteActivePlan() {
        dao.deleteActivePlan()
    }

    override suspend fun hasActivePlan(): Boolean = dao.hasPlan()

    private suspend fun refreshMilestoneProgress() {
        val tasks = dao.getTasks()
        val completedTaskIds = tasks
            .filter { it.status == WeeklyTaskStatus.Completed.name }
            .map { it.id }
            .toSet()

        dao.getMilestones().forEach { milestone ->
            val relatedTaskIds = milestone.relatedTaskIds
                .split("||")
                .filter { it.isNotBlank() }
            if (relatedTaskIds.isNotEmpty()) {
                val completed = relatedTaskIds.count { it in completedTaskIds }
                val progress = ((completed.toFloat() / relatedTaskIds.size.toFloat()) * 100).toInt()
                val status = when {
                    progress >= 100 -> MilestoneStatus.Completed
                    progress > 0 -> MilestoneStatus.InProgress
                    else -> MilestoneStatus.NotStarted
                }
                dao.updateMilestoneStatus(milestone.id, status.name, progress)
            }
        }
    }
}
