package com.justindwinata.usahanaik.data.mapper

import com.justindwinata.usahanaik.data.local.BusinessReportSnapshotEntity
import com.justindwinata.usahanaik.domain.model.BusinessReportPeriod
import com.justindwinata.usahanaik.domain.model.BusinessReportSnapshot

object BusinessReportSnapshotMapper {
    fun BusinessReportSnapshot.toEntity(now: Long): BusinessReportSnapshotEntity = BusinessReportSnapshotEntity(
        id = id,
        period = period.name,
        businessName = businessName,
        generatedAt = generatedAt,
        headlineSummary = headlineSummary,
        exportReadyText = exportReadyText,
        healthScore = healthScore,
        totalRevenue = totalRevenue,
        totalExpenses = totalExpenses,
        estimatedProfit = estimatedProfit,
        taskCompletionRate = taskCompletionRate,
        contentExecutionRate = contentExecutionRate,
        createdAt = createdAt.takeIf { it > 0L } ?: now,
        updatedAt = now
    )

    fun BusinessReportSnapshotEntity.toDomain(): BusinessReportSnapshot = BusinessReportSnapshot(
        id = id,
        period = runCatching { BusinessReportPeriod.valueOf(period) }
            .getOrDefault(BusinessReportPeriod.ThisMonth),
        businessName = businessName,
        generatedAt = generatedAt,
        headlineSummary = headlineSummary,
        exportReadyText = exportReadyText,
        healthScore = healthScore,
        totalRevenue = totalRevenue,
        totalExpenses = totalExpenses,
        estimatedProfit = estimatedProfit,
        taskCompletionRate = taskCompletionRate,
        contentExecutionRate = contentExecutionRate,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}
