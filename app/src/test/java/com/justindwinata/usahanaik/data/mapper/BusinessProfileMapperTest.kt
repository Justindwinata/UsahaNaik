package com.justindwinata.usahanaik.data.mapper

import com.justindwinata.usahanaik.data.mapper.BusinessProfileMapper.toDomain
import com.justindwinata.usahanaik.data.mapper.BusinessProfileMapper.toEntity
import com.justindwinata.usahanaik.domain.model.AvailableTime
import com.justindwinata.usahanaik.domain.model.BusinessChallenge
import com.justindwinata.usahanaik.domain.model.BusinessSetupDraft
import com.justindwinata.usahanaik.domain.model.BusinessStage
import com.justindwinata.usahanaik.domain.model.CostDriver
import com.justindwinata.usahanaik.domain.model.MonthlyFocus
import com.justindwinata.usahanaik.domain.model.SellingChannel
import com.justindwinata.usahanaik.domain.model.StockIssue
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class BusinessProfileMapperTest {
    @Test
    fun mapsDraftToEntityAndBackWithEnumsAndChallenges() {
        val draft = validDraft()
        val entity = draft.toEntity(createdAt = 100L, updatedAt = 200L)
        val profile = entity.toDomain()

        assertEquals("Warung Maju", entity.businessName)
        assertTrue(entity.challenges.contains(BusinessChallenge.LowSales.name))
        assertEquals(draft, profile.draft)
        assertEquals(100L, profile.createdAt)
        assertEquals(200L, profile.updatedAt)
    }

    @Test
    fun unknownEnumValuesAreIgnoredSafely() {
        val entity = validDraft()
            .toEntity(createdAt = 100L, updatedAt = 200L)
            .copy(
                sellingChannel = "BROKEN",
                challenges = "${BusinessChallenge.LowSales.name}|BROKEN"
            )

        val draft = entity.toDomain().draft

        assertEquals(null, draft.sellingChannel)
        assertEquals(setOf(BusinessChallenge.LowSales), draft.challenges)
    }

    private fun validDraft(): BusinessSetupDraft = BusinessSetupDraft(
        businessName = "Warung Maju",
        ownerName = "Justin",
        categoryId = "warung_kelontong",
        sellingChannel = SellingChannel.MixedOnlineOffline,
        businessLocation = "Jakarta",
        businessStage = BusinessStage.RunningMoreThanOneYear,
        startingCapital = "Rp 5.000.000",
        monthlyRevenue = "Rp 10.000.000",
        monthlyExpenses = "Rp 7.000.000",
        estimatedMonthlyProfit = "Rp 3.000.000",
        averageDailyTransactions = "20",
        averageTransactionValue = "Rp 50.000",
        productCount = "40",
        bestSellingProduct = "Beras 5kg",
        highestMarginProduct = "Paket sembako",
        mainCostDriver = CostDriver.RawMaterials,
        stockIssue = StockIssue.SlowMovingStock,
        challenges = setOf(BusinessChallenge.LowSales, BusinessChallenge.PoorFinancialRecords),
        targetMonthlyRevenue = "Rp 12.000.000",
        targetMonthlyProfit = "Rp 4.500.000",
        mainFocus = MonthlyFocus.OrganizeFinance,
        availableTime = AvailableTime.ThreeToFiveHours
    )
}
