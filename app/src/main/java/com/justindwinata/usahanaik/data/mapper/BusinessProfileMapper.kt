package com.justindwinata.usahanaik.data.mapper

import com.justindwinata.usahanaik.data.local.BusinessProfileEntity
import com.justindwinata.usahanaik.domain.model.AvailableTime
import com.justindwinata.usahanaik.domain.model.BusinessChallenge
import com.justindwinata.usahanaik.domain.model.BusinessProfile
import com.justindwinata.usahanaik.domain.model.BusinessSetupDraft
import com.justindwinata.usahanaik.domain.model.BusinessStage
import com.justindwinata.usahanaik.domain.model.CostDriver
import com.justindwinata.usahanaik.domain.model.MonthlyFocus
import com.justindwinata.usahanaik.domain.model.SellingChannel
import com.justindwinata.usahanaik.domain.model.StockIssue

object BusinessProfileMapper {
    private const val LIST_SEPARATOR = "|"

    fun BusinessSetupDraft.toEntity(
        createdAt: Long,
        updatedAt: Long
    ): BusinessProfileEntity = BusinessProfileEntity(
        businessName = businessName,
        ownerName = ownerName,
        categoryId = categoryId.orEmpty(),
        sellingChannel = sellingChannel?.name.orEmpty(),
        businessLocation = businessLocation,
        businessStage = businessStage?.name.orEmpty(),
        startingCapital = startingCapital,
        monthlyRevenue = monthlyRevenue,
        monthlyExpenses = monthlyExpenses,
        estimatedMonthlyProfit = estimatedMonthlyProfit,
        averageDailyTransactions = averageDailyTransactions,
        averageTransactionValue = averageTransactionValue,
        productCount = productCount,
        bestSellingProduct = bestSellingProduct,
        highestMarginProduct = highestMarginProduct,
        mainCostDriver = mainCostDriver?.name.orEmpty(),
        stockIssue = stockIssue?.name.orEmpty(),
        challenges = challenges.map { it.name }.sorted().joinToString(LIST_SEPARATOR),
        targetMonthlyRevenue = targetMonthlyRevenue,
        targetMonthlyProfit = targetMonthlyProfit,
        mainFocus = mainFocus?.name.orEmpty(),
        availableTime = availableTime?.name.orEmpty(),
        createdAt = createdAt,
        updatedAt = updatedAt
    )

    fun BusinessProfileEntity.toDomain(): BusinessProfile = BusinessProfile(
        draft = BusinessSetupDraft(
            businessName = businessName,
            ownerName = ownerName,
            categoryId = categoryId.ifBlank { null },
            sellingChannel = enumValueOrNull<SellingChannel>(sellingChannel),
            businessLocation = businessLocation,
            businessStage = enumValueOrNull<BusinessStage>(businessStage),
            startingCapital = startingCapital,
            monthlyRevenue = monthlyRevenue,
            monthlyExpenses = monthlyExpenses,
            estimatedMonthlyProfit = estimatedMonthlyProfit,
            averageDailyTransactions = averageDailyTransactions,
            averageTransactionValue = averageTransactionValue,
            productCount = productCount,
            bestSellingProduct = bestSellingProduct,
            highestMarginProduct = highestMarginProduct,
            mainCostDriver = enumValueOrNull<CostDriver>(mainCostDriver),
            stockIssue = enumValueOrNull<StockIssue>(stockIssue),
            challenges = challenges.toChallenges(),
            targetMonthlyRevenue = targetMonthlyRevenue,
            targetMonthlyProfit = targetMonthlyProfit,
            mainFocus = enumValueOrNull<MonthlyFocus>(mainFocus),
            availableTime = enumValueOrNull<AvailableTime>(availableTime)
        ),
        createdAt = createdAt,
        updatedAt = updatedAt
    )

    private fun String.toChallenges(): Set<BusinessChallenge> {
        if (isBlank()) return emptySet()
        return split(LIST_SEPARATOR)
            .mapNotNull { enumValueOrNull<BusinessChallenge>(it) }
            .toSet()
    }

    private inline fun <reified T : Enum<T>> enumValueOrNull(value: String): T? {
        if (value.isBlank()) return null
        return enumValues<T>().firstOrNull { it.name == value }
    }
}
