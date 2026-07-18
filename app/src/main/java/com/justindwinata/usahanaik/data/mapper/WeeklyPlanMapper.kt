package com.justindwinata.usahanaik.data.mapper

import com.justindwinata.usahanaik.data.local.WeeklyGrowthPlanEntity
import com.justindwinata.usahanaik.data.local.WeeklyMilestoneEntity
import com.justindwinata.usahanaik.data.local.WeeklyTaskEntity
import com.justindwinata.usahanaik.domain.model.ActionDifficulty
import com.justindwinata.usahanaik.domain.model.ActionEstimatedTime
import com.justindwinata.usahanaik.domain.model.BusinessMilestone
import com.justindwinata.usahanaik.domain.model.InsightCategory
import com.justindwinata.usahanaik.domain.model.MilestoneStatus
import com.justindwinata.usahanaik.domain.model.WeeklyChallenge
import com.justindwinata.usahanaik.domain.model.WeeklyGrowthPlan
import com.justindwinata.usahanaik.domain.model.WeeklyPlanFocus
import com.justindwinata.usahanaik.domain.model.WeeklyPlanStatus
import com.justindwinata.usahanaik.domain.model.WeeklyTask
import com.justindwinata.usahanaik.domain.model.WeeklyTaskStatus

object WeeklyPlanMapper {
    private const val LIST_SEPARATOR = "||"

    fun WeeklyGrowthPlan.toPlanEntity(now: Long): WeeklyGrowthPlanEntity {
        return WeeklyGrowthPlanEntity(
            id = WeeklyGrowthPlanEntity.ACTIVE_PLAN_ID,
            title = title,
            generatedDate = generatedDate,
            businessName = businessName,
            businessCategoryId = businessCategoryId,
            businessCategoryName = businessCategoryName,
            focusTitle = focus.title,
            focusCategory = focus.category.name,
            focusReason = focus.reason,
            target = target,
            priorityReason = priorityReason,
            challengeTitle = challenge.title,
            challengeDescription = challenge.description,
            challengeChecklistItems = challenge.checklistItems.joinToString(LIST_SEPARATOR),
            challengeCompletionTarget = challenge.completionTarget,
            challengeMotivationalCopy = challenge.motivationalCopy,
            status = status.name,
            limitationsNote = limitationsNote,
            createdAt = createdAt.takeIf { it > 0L } ?: now,
            updatedAt = now
        )
    }

    fun WeeklyTask.toEntity(planId: Long, sortOrder: Int): WeeklyTaskEntity {
        return WeeklyTaskEntity(
            id = id,
            planId = planId,
            title = title,
            description = description,
            category = category.name,
            estimatedTime = estimatedTime.name,
            difficulty = difficulty.name,
            status = status.name,
            reason = reason,
            expectedOutcome = expectedOutcome,
            sortOrder = sortOrder
        )
    }

    fun BusinessMilestone.toEntity(planId: Long, sortOrder: Int): WeeklyMilestoneEntity {
        return WeeklyMilestoneEntity(
            id = id,
            planId = planId,
            title = title,
            description = description,
            status = status.name,
            relatedTaskIds = relatedTaskIds.joinToString(LIST_SEPARATOR),
            progressPercentage = progressPercentage,
            sortOrder = sortOrder
        )
    }

    fun toDomain(
        plan: WeeklyGrowthPlanEntity,
        tasks: List<WeeklyTaskEntity>,
        milestones: List<WeeklyMilestoneEntity>
    ): WeeklyGrowthPlan {
        return WeeklyGrowthPlan(
            id = plan.id,
            title = plan.title,
            generatedDate = plan.generatedDate,
            businessName = plan.businessName,
            businessCategoryId = plan.businessCategoryId,
            businessCategoryName = plan.businessCategoryName,
            focus = WeeklyPlanFocus(
                title = plan.focusTitle,
                category = enumValueOrDefault(plan.focusCategory, InsightCategory.Operations),
                reason = plan.focusReason
            ),
            target = plan.target,
            priorityReason = plan.priorityReason,
            tasks = tasks.map { it.toDomain() },
            challenge = WeeklyChallenge(
                title = plan.challengeTitle,
                description = plan.challengeDescription,
                checklistItems = splitList(plan.challengeChecklistItems),
                completionTarget = plan.challengeCompletionTarget,
                motivationalCopy = plan.challengeMotivationalCopy
            ),
            milestones = milestones.map { it.toDomain() },
            status = enumValueOrDefault(plan.status, WeeklyPlanStatus.Active),
            limitationsNote = plan.limitationsNote,
            createdAt = plan.createdAt,
            updatedAt = plan.updatedAt
        )
    }

    private fun WeeklyTaskEntity.toDomain(): WeeklyTask {
        return WeeklyTask(
            id = id,
            title = title,
            description = description,
            category = enumValueOrDefault(category, InsightCategory.Operations),
            estimatedTime = enumValueOrDefault(estimatedTime, ActionEstimatedTime.FifteenToThirtyMinutes),
            difficulty = enumValueOrDefault(difficulty, ActionDifficulty.Easy),
            status = enumValueOrDefault(status, WeeklyTaskStatus.Pending),
            reason = reason,
            expectedOutcome = expectedOutcome
        )
    }

    private fun WeeklyMilestoneEntity.toDomain(): BusinessMilestone {
        return BusinessMilestone(
            id = id,
            title = title,
            description = description,
            status = enumValueOrDefault(status, MilestoneStatus.NotStarted),
            relatedTaskIds = splitList(relatedTaskIds),
            progressPercentage = progressPercentage
        )
    }

    private inline fun <reified T : Enum<T>> enumValueOrDefault(value: String, default: T): T {
        return runCatching { enumValueOf<T>(value) }.getOrDefault(default)
    }

    private fun splitList(value: String): List<String> {
        return value.split(LIST_SEPARATOR).filter { it.isNotBlank() }
    }
}
