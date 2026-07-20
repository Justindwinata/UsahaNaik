package com.justindwinata.usahanaik.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weekly_progress_snapshots")
data class WeeklyProgressSnapshotEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val weekLabel: String,
    val weekStartDate: String,
    val totalTasks: Int,
    val completedTasks: Int,
    val taskCompletionRate: Float,
    val milestoneProgress: Float,
    val weeklyIncome: Long,
    val weeklyExpenses: Long,
    val weeklyEstimatedProfit: Long,
    val profitMarginPercent: Int,
    val savedIdeasCount: Int,
    val plannedContentCount: Int,
    val postedOrDoneContentCount: Int,
    val skippedContentCount: Int,
    val businessHealthScore: Int,
    val warningInsightCount: Int,
    val criticalInsightCount: Int,
    val createdAt: Long,
    val updatedAt: Long
)
