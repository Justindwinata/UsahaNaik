package com.justindwinata.usahanaik.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "business_report_snapshots")
data class BusinessReportSnapshotEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val period: String,
    val businessName: String,
    val generatedAt: String,
    val headlineSummary: String,
    val exportReadyText: String,
    val healthScore: Int,
    val totalRevenue: Long,
    val totalExpenses: Long,
    val estimatedProfit: Long,
    val taskCompletionRate: Float,
    val contentExecutionRate: Float,
    val createdAt: Long,
    val updatedAt: Long
)
