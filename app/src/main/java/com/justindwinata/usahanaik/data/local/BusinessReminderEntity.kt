package com.justindwinata.usahanaik.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "business_reminders")
data class BusinessReminderEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val title: String,
    val description: String,
    val type: String,
    val frequency: String,
    val scheduledDay: String,
    val scheduledDate: String,
    val timeLabel: String,
    val status: String,
    val relatedEntityId: Long?,
    val createdAt: Long,
    val updatedAt: Long
)
