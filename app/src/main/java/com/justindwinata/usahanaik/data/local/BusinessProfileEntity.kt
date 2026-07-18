package com.justindwinata.usahanaik.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "business_profiles")
data class BusinessProfileEntity(
    @PrimaryKey val id: Long = ACTIVE_PROFILE_ID,
    val businessName: String,
    val ownerName: String,
    val categoryId: String,
    val sellingChannel: String,
    val businessLocation: String,
    val businessStage: String,
    val startingCapital: String,
    val monthlyRevenue: String,
    val monthlyExpenses: String,
    val estimatedMonthlyProfit: String,
    val averageDailyTransactions: String,
    val averageTransactionValue: String,
    val productCount: String,
    val bestSellingProduct: String,
    val highestMarginProduct: String,
    val mainCostDriver: String,
    val stockIssue: String,
    val challenges: String,
    val targetMonthlyRevenue: String,
    val targetMonthlyProfit: String,
    val mainFocus: String,
    val availableTime: String,
    val createdAt: Long,
    val updatedAt: Long
) {
    companion object {
        const val ACTIVE_PROFILE_ID = 1L
    }
}
