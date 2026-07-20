package com.justindwinata.usahanaik.data.mapper

import com.justindwinata.usahanaik.data.local.WeeklyProgressSnapshotEntity
import com.justindwinata.usahanaik.domain.model.WeeklyProgressSnapshot

object WeeklyProgressSnapshotMapper {
    fun WeeklyProgressSnapshot.toEntity(now: Long): WeeklyProgressSnapshotEntity {
        return WeeklyProgressSnapshotEntity(
            id = id,
            weekLabel = weekLabel,
            weekStartDate = weekStartDate,
            totalTasks = totalTasks,
            completedTasks = completedTasks,
            taskCompletionRate = taskCompletionRate,
            milestoneProgress = milestoneProgress,
            weeklyIncome = weeklyIncome,
            weeklyExpenses = weeklyExpenses,
            weeklyEstimatedProfit = weeklyEstimatedProfit,
            profitMarginPercent = profitMarginPercent,
            savedIdeasCount = savedIdeasCount,
            plannedContentCount = plannedContentCount,
            postedOrDoneContentCount = postedOrDoneContentCount,
            skippedContentCount = skippedContentCount,
            businessHealthScore = businessHealthScore,
            warningInsightCount = warningInsightCount,
            criticalInsightCount = criticalInsightCount,
            createdAt = createdAt.takeIf { it > 0L } ?: now,
            updatedAt = now
        )
    }

    fun WeeklyProgressSnapshotEntity.toDomain(): WeeklyProgressSnapshot {
        return WeeklyProgressSnapshot(
            id = id,
            weekLabel = weekLabel,
            weekStartDate = weekStartDate,
            totalTasks = totalTasks,
            completedTasks = completedTasks,
            taskCompletionRate = taskCompletionRate,
            milestoneProgress = milestoneProgress,
            weeklyIncome = weeklyIncome,
            weeklyExpenses = weeklyExpenses,
            weeklyEstimatedProfit = weeklyEstimatedProfit,
            profitMarginPercent = profitMarginPercent,
            savedIdeasCount = savedIdeasCount,
            plannedContentCount = plannedContentCount,
            postedOrDoneContentCount = postedOrDoneContentCount,
            skippedContentCount = skippedContentCount,
            businessHealthScore = businessHealthScore,
            warningInsightCount = warningInsightCount,
            criticalInsightCount = criticalInsightCount,
            createdAt = createdAt,
            updatedAt = updatedAt
        )
    }
}
