package com.justindwinata.usahanaik.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "financial_entries")
data class FinancialEntryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val type: String,
    val title: String,
    val amount: Long,
    val category: String,
    val date: String,
    val note: String,
    val createdAt: Long,
    val updatedAt: Long
)
