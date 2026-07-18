package com.justindwinata.usahanaik.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weekly_growth_plans")
data class WeeklyGrowthPlanEntity(
    @PrimaryKey val id: Long = ACTIVE_PLAN_ID,
    val title: String,
    val generatedDate: String,
    val businessName: String,
    val businessCategoryId: String?,
    val businessCategoryName: String,
    val focusTitle: String,
    val focusCategory: String,
    val focusReason: String,
    val target: String,
    val priorityReason: String,
    val challengeTitle: String,
    val challengeDescription: String,
    val challengeChecklistItems: String,
    val challengeCompletionTarget: String,
    val challengeMotivationalCopy: String,
    val status: String,
    val limitationsNote: String,
    val createdAt: Long,
    val updatedAt: Long
) {
    companion object {
        const val ACTIVE_PLAN_ID = 1L
    }
}
