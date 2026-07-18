package com.justindwinata.usahanaik.data.mapper

import com.justindwinata.usahanaik.data.mapper.WeeklyPlanMapper.toEntity
import com.justindwinata.usahanaik.data.mapper.WeeklyPlanMapper.toPlanEntity
import com.justindwinata.usahanaik.domain.model.ActionDifficulty
import com.justindwinata.usahanaik.domain.model.ActionEstimatedTime
import com.justindwinata.usahanaik.domain.model.BusinessMilestone
import com.justindwinata.usahanaik.domain.model.InsightCategory
import com.justindwinata.usahanaik.domain.model.WeeklyChallenge
import com.justindwinata.usahanaik.domain.model.WeeklyGrowthPlan
import com.justindwinata.usahanaik.domain.model.WeeklyPlanFocus
import com.justindwinata.usahanaik.domain.model.WeeklyTask
import org.junit.Assert.assertEquals
import org.junit.Test

class WeeklyPlanMapperTest {
    @Test
    fun mapsPlanDomainToEntitiesAndBack() {
        val plan = samplePlan()
        val planEntity = plan.toPlanEntity(now = 200L)
        val taskEntities = plan.tasks.mapIndexed { index, task -> task.toEntity(planEntity.id, index) }
        val milestoneEntities = plan.milestones.mapIndexed { index, milestone -> milestone.toEntity(planEntity.id, index) }

        val mapped = WeeklyPlanMapper.toDomain(planEntity, taskEntities, milestoneEntities)

        assertEquals("Weekly Plan", mapped.title)
        assertEquals("Food & Beverage", mapped.businessCategoryName)
        assertEquals("Build financial tracking discipline", mapped.focus.title)
        assertEquals(listOf("Check 1", "Check 2"), mapped.challenge.checklistItems)
        assertEquals("task-1", mapped.tasks.first().id)
        assertEquals(listOf("task-1"), mapped.milestones.first().relatedTaskIds)
    }

    private fun samplePlan(): WeeklyGrowthPlan {
        return WeeklyGrowthPlan(
            title = "Weekly Plan",
            generatedDate = "2026-07-19",
            businessName = "Toko Maju",
            businessCategoryId = "food_beverage",
            businessCategoryName = "Food & Beverage",
            focus = WeeklyPlanFocus(
                title = "Build financial tracking discipline",
                category = InsightCategory.Finance,
                reason = "Need records"
            ),
            target = "Complete 5 tasks",
            priorityReason = "Need records",
            tasks = listOf(
                WeeklyTask(
                    id = "task-1",
                    title = "Record sales",
                    description = "Record sales",
                    category = InsightCategory.Finance,
                    estimatedTime = ActionEstimatedTime.UnderFifteenMinutes,
                    difficulty = ActionDifficulty.Easy,
                    reason = "Need records",
                    expectedOutcome = "May improve clarity."
                )
            ),
            challenge = WeeklyChallenge(
                title = "Challenge",
                description = "Description",
                checklistItems = listOf("Check 1", "Check 2"),
                completionTarget = "2 checks",
                motivationalCopy = "Keep going"
            ),
            milestones = listOf(
                BusinessMilestone(
                    id = "milestone-1",
                    title = "Milestone",
                    description = "Description",
                    relatedTaskIds = listOf("task-1")
                )
            ),
            limitationsNote = "Rule-based"
        )
    }
}
