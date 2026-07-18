package com.justindwinata.usahanaik.data.repository

import com.justindwinata.usahanaik.data.local.BusinessProfileDao
import com.justindwinata.usahanaik.data.local.BusinessProfileEntity
import com.justindwinata.usahanaik.domain.model.AvailableTime
import com.justindwinata.usahanaik.domain.model.BusinessChallenge
import com.justindwinata.usahanaik.domain.model.BusinessSetupDraft
import com.justindwinata.usahanaik.domain.model.BusinessStage
import com.justindwinata.usahanaik.domain.model.CostDriver
import com.justindwinata.usahanaik.domain.model.MonthlyFocus
import com.justindwinata.usahanaik.domain.model.SellingChannel
import com.justindwinata.usahanaik.domain.model.StockIssue
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class LocalBusinessProfileRepositoryTest {
    @Test
    fun savesAndRetrievesActiveBusinessProfile() = runTest {
        val repository = LocalBusinessProfileRepository(FakeBusinessProfileDao(), nowProvider = { 100L })

        repository.saveBusinessProfile(validDraft())
        val profile = repository.getActiveBusinessProfile()

        assertEquals("Warung Maju", profile?.draft?.businessName)
        assertEquals(100L, profile?.createdAt)
        assertEquals(100L, profile?.updatedAt)
        assertTrue(repository.hasBusinessProfile())
    }

    @Test
    fun overwritesExistingProfileAndKeepsCreatedAt() = runTest {
        var now = 100L
        val repository = LocalBusinessProfileRepository(FakeBusinessProfileDao(), nowProvider = { now })

        repository.saveBusinessProfile(validDraft())
        now = 250L
        repository.saveBusinessProfile(validDraft().copy(businessName = "Toko Baru"))
        val profile = repository.getActiveBusinessProfile()

        assertEquals("Toko Baru", profile?.draft?.businessName)
        assertEquals(100L, profile?.createdAt)
        assertEquals(250L, profile?.updatedAt)
    }

    @Test
    fun deletesProfile() = runTest {
        val repository = LocalBusinessProfileRepository(FakeBusinessProfileDao(), nowProvider = { 100L })

        repository.saveBusinessProfile(validDraft())
        repository.deleteBusinessProfile()

        assertFalse(repository.hasBusinessProfile())
        assertEquals(null, repository.getActiveBusinessProfile())
    }

    private fun validDraft(): BusinessSetupDraft = BusinessSetupDraft(
        businessName = "Warung Maju",
        categoryId = "warung_kelontong",
        sellingChannel = SellingChannel.WhatsApp,
        businessStage = BusinessStage.RunningMoreThanOneYear,
        monthlyRevenue = "Rp 10.000.000",
        monthlyExpenses = "Rp 7.000.000",
        estimatedMonthlyProfit = "Rp 3.000.000",
        productCount = "40",
        bestSellingProduct = "Beras 5kg",
        highestMarginProduct = "Paket sembako",
        mainCostDriver = CostDriver.RawMaterials,
        stockIssue = StockIssue.NoStockIssue,
        challenges = setOf(BusinessChallenge.PoorFinancialRecords),
        targetMonthlyRevenue = "Rp 12.000.000",
        targetMonthlyProfit = "Rp 4.500.000",
        mainFocus = MonthlyFocus.OrganizeFinance,
        availableTime = AvailableTime.ThreeToFiveHours
    )

    private class FakeBusinessProfileDao : BusinessProfileDao {
        private val state = MutableStateFlow<BusinessProfileEntity?>(null)

        override suspend fun upsertProfile(profile: BusinessProfileEntity) {
            state.value = profile
        }

        override suspend fun getProfile(id: Long): BusinessProfileEntity? = state.value?.takeIf { it.id == id }

        override fun observeProfile(id: Long): Flow<BusinessProfileEntity?> = state

        override suspend fun hasProfile(id: Long): Boolean = state.value?.id == id

        override suspend fun deleteProfile(id: Long) {
            if (state.value?.id == id) {
                state.value = null
            }
        }
    }
}
