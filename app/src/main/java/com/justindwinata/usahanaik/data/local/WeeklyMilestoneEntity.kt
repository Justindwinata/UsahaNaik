package com.justindwinata.usahanaik.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weekly_milestones")
data class WeeklyMilestoneEntity(
    @PrimaryKey val id: String,
    val planId: Long,
    val title: String,
    val description: String,
    val status: String,
    val relatedTaskIds: String,
    val progressPercentage: Int,
    val sortOrder: Int
)
