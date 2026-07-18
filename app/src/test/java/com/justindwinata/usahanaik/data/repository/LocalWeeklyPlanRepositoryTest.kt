package com.justindwinata.usahanaik.data.repository

import com.justindwinata.usahanaik.data.local.WeeklyGrowthPlanEntity
import com.justindwinata.usahanaik.data.local.WeeklyMilestoneEntity
import com.justindwinata.usahanaik.data.local.WeeklyPlanDao
import com.justindwinata.usahanaik.data.local.WeeklyTaskEntity
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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class LocalWeeklyPlanRepositoryTest {
    @Test
    fun savesAndGetsActivePlan() = runTest {
        val repository = LocalWeeklyPlanRepository(FakeWeeklyPlanDao(), nowProvider = { 100L })

        repository.savePlan(samplePlan("Plan A"))
        val saved = repository.getActivePlan()

        assertEquals("Plan A", saved!!.title)
        assertEquals(2, saved.tasks.size)
        assertTrue(repository.hasActivePlan())
    }

    @Test
    fun replacesActivePlan() = runTest {
        val repository = LocalWeeklyPlanRepository(FakeWeeklyPlanDao(), nowProvider = { 100L })

        repository.savePlan(samplePlan("Plan A"))
        repository.savePlan(samplePlan("Plan B"))

        assertEquals("Plan B", repository.getActivePlan()!!.title)
        assertEquals(2, repository.getActivePlan()!!.tasks.size)
    }

    @Test
    fun updatesTaskCompletionAndDerivesMilestoneProgress() = runTest {
        val repository = LocalWeeklyPlanRepository(FakeWeeklyPlanDao(), nowProvider = { 100L })
        repository.savePlan(samplePlan("Plan A"))

        val updated = repository.updateTaskStatus("task-1", WeeklyTaskStatus.Completed)

        assertEquals(WeeklyTaskStatus.Completed, updated!!.tasks.first().status)
        assertEquals(MilestoneStatus.InProgress, updated.milestones.first().status)
        assertEquals(50, updated.milestones.first().progressPercentage)
    }

    @Test
    fun milestoneCompletesWhenRelatedTasksComplete() = runTest {
        val repository = LocalWeeklyPlanRepository(FakeWeeklyPlanDao(), nowProvider = { 100L })
        repository.savePlan(samplePlan("Plan A"))

        repository.updateTaskStatus("task-1", WeeklyTaskStatus.Completed)
        val updated = repository.updateTaskStatus("task-2", WeeklyTaskStatus.Completed)

        assertEquals(MilestoneStatus.Completed, updated!!.milestones.first().status)
        assertEquals(100, updated.milestones.first().progressPercentage)
    }

    @Test
    fun deletesActivePlan() = runTest {
        val repository = LocalWeeklyPlanRepository(FakeWeeklyPlanDao(), nowProvider = { 100L })
        repository.savePlan(samplePlan("Plan A"))

        repository.deleteActivePlan()

        assertFalse(repository.hasActivePlan())
        assertNull(repository.getActivePlan())
    }

    private fun samplePlan(title: String): WeeklyGrowthPlan {
        return WeeklyGrowthPlan(
            title = title,
            generatedDate = "2026-07-19",
            businessName = "Toko Maju",
            businessCategoryId = "food_beverage",
            businessCategoryName = "Food & Beverage",
            focus = WeeklyPlanFocus("Build financial tracking discipline", InsightCategory.Finance, "Need records"),
            target = "Complete tasks",
            priorityReason = "Need records",
            tasks = listOf(
                task("task-1"),
                task("task-2")
            ),
            challenge = WeeklyChallenge(
                title = "Challenge",
                description = "Description",
                checklistItems = listOf("A", "B"),
                completionTarget = "Target",
                motivationalCopy = "Copy"
            ),
            milestones = listOf(
                BusinessMilestone(
                    id = "milestone-1",
                    title = "Milestone",
                    description = "Description",
                    relatedTaskIds = listOf("task-1", "task-2")
                )
            ),
            limitationsNote = "Rule-based"
        )
    }

    private fun task(id: String): WeeklyTask {
        return WeeklyTask(
            id = id,
            title = "Task $id",
            description = "Description",
            category = InsightCategory.Finance,
            estimatedTime = ActionEstimatedTime.UnderFifteenMinutes,
            difficulty = ActionDifficulty.Easy,
            reason = "Reason",
            expectedOutcome = "May help."
        )
    }

    private class FakeWeeklyPlanDao : WeeklyPlanDao {
        private val plan = MutableStateFlow<WeeklyGrowthPlanEntity?>(null)
        private val tasks = MutableStateFlow<List<WeeklyTaskEntity>>(emptyList())
        private val milestones = MutableStateFlow<List<WeeklyMilestoneEntity>>(emptyList())

        override suspend fun upsertPlan(plan: WeeklyGrowthPlanEntity) {
            this.plan.value = plan
        }

        override suspend fun upsertTasks(tasks: List<WeeklyTaskEntity>) {
            this.tasks.value = tasks
        }

        override suspend fun upsertMilestones(milestones: List<WeeklyMilestoneEntity>) {
            this.milestones.value = milestones
        }

        override suspend fun getPlan(id: Long): WeeklyGrowthPlanEntity? = plan.value?.takeIf { it.id == id }

        override fun observePlan(id: Long): Flow<WeeklyGrowthPlanEntity?> = plan

        override suspend fun getTasks(planId: Long): List<WeeklyTaskEntity> {
            return tasks.value.filter { it.planId == planId }.sortedBy { it.sortOrder }
        }

        override suspend fun getMilestones(planId: Long): List<WeeklyMilestoneEntity> {
            return milestones.value.filter { it.planId == planId }.sortedBy { it.sortOrder }
        }

        override suspend fun updateTaskStatus(taskId: String, status: String) {
            tasks.value = tasks.value.map { if (it.id == taskId) it.copy(status = status) else it }
        }

        override suspend fun updateMilestoneStatus(milestoneId: String, status: String, progressPercentage: Int) {
            milestones.value = milestones.value.map {
                if (it.id == milestoneId) it.copy(status = status, progressPercentage = progressPercentage) else it
            }
        }

        override suspend fun hasPlan(id: Long): Boolean = plan.value?.id == id

        override suspend fun deleteTasks(planId: Long) {
            tasks.value = tasks.value.filterNot { it.planId == planId }
        }

        override suspend fun deleteMilestones(planId: Long) {
            milestones.value = milestones.value.filterNot { it.planId == planId }
        }

        override suspend fun deletePlan(id: Long) {
            if (plan.value?.id == id) plan.value = null
        }
    }
}
