package com.justindwinata.usahanaik.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weekly_retrospectives")
data class WeeklyRetrospectiveEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val weekLabel: String,
    val generatedDate: String,
    val summaryTitle: String,
    val sections: String,
    val nextWeekFocus: String,
    val nextWeekReason: String,
    val nextWeekRecommendedAction: String,
    val createdAt: Long,
    val updatedAt: Long
)
