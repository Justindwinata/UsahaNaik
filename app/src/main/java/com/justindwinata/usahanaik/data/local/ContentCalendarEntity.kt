package com.justindwinata.usahanaik.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "content_calendar_items")
data class ContentCalendarEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val contentIdeaId: Long,
    val title: String,
    val platform: String,
    val scheduledDate: String,
    val timeLabel: String,
    val postingNote: String,
    val status: String,
    val createdAt: Long,
    val updatedAt: Long
)
