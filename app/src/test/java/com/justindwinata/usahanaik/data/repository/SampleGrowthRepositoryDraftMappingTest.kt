package com.justindwinata.usahanaik.data.repository

import com.justindwinata.usahanaik.domain.model.AvailableTime
import com.justindwinata.usahanaik.domain.model.BusinessChallenge
import com.justindwinata.usahanaik.domain.model.BusinessSetupDraft
import com.justindwinata.usahanaik.domain.model.CostDriver
import com.justindwinata.usahanaik.domain.model.MonthlyFocus
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class SampleGrowthRepositoryDraftMappingTest {
    private val repository = SampleGrowthRepository()

    @Test
    fun dashboardUsesDraftBusinessIdentityAndFinancialValues() {
        val dashboard = repository.getDashboardPreview(validDraft())

        assertEquals("Warung Maju", dashboard.summary.businessName)
        assertEquals("Warung / Toko Kelontong", dashboard.summary.categoryName)
        assertEquals("Rp10.000.000", dashboard.financialSummary.monthlyRevenue)
        assertEquals("Rp7.000.000", dashboard.financialSummary.monthlyExpenses)
        assertEquals("Rp3.000.000", dashboard.financialSummary.estimatedProfit)
        assertEquals("30%", dashboard.financialSummary.profitMargin)
        assertTrue(dashboard.financialSummary.reportSummary.contains("Rp2.000.000"))
    }

    @Test
    fun dashboardTasksReflectSelectedChallenges() {
        val dashboard = repository.getDashboardPreview(
            validDraft().copy(
                challenges = setOf(
                    BusinessChallenge.PoorFinancialRecords,
                    BusinessChallenge.InconsistentContent,
                    BusinessChallenge.StockProblems
                )
            )
        )

        assertTrue(dashboard.tasks.any { it.title.contains("finansial", ignoreCase = true) })
        assertTrue(dashboard.tasks.any { it.title.contains("konten", ignoreCase = true) })
        assertTrue(dashboard.tasks.any { it.title.contains("stok", ignoreCase = true) })
    }

    private fun validDraft(): BusinessSetupDraft = BusinessSetupDraft(
        businessName = "Warung Maju",
        categoryId = "warung_kelontong",
        monthlyRevenue = "Rp 10.000.000",
        monthlyExpenses = "Rp 7.000.000",
        estimatedMonthlyProfit = "Rp 3.000.000",
        productCount = "40",
        bestSellingProduct = "Beras 5kg",
        highestMarginProduct = "Paket sembako",
        mainCostDriver = CostDriver.RawMaterials,
        challenges = setOf(BusinessChallenge.PoorFinancialRecords),
        targetMonthlyRevenue = "Rp 12.000.000",
        targetMonthlyProfit = "Rp 4.500.000",
        mainFocus = MonthlyFocus.OrganizeFinance,
        availableTime = AvailableTime.ThreeToFiveHours
    )
}
