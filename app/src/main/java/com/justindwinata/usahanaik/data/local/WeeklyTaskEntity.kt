package com.justindwinata.usahanaik.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weekly_tasks")
data class WeeklyTaskEntity(
    @PrimaryKey val id: String,
    val planId: Long,
    val title: String,
    val description: String,
    val category: String,
    val estimatedTime: String,
    val difficulty: String,
    val status: String,
    val reason: String,
    val expectedOutcome: String,
    val sortOrder: Int
)
