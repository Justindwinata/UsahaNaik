package com.justindwinata.usahanaik.ui.setup

import com.justindwinata.usahanaik.domain.model.AvailableTime
import com.justindwinata.usahanaik.domain.model.BusinessChallenge
import com.justindwinata.usahanaik.domain.model.BusinessStage
import com.justindwinata.usahanaik.domain.model.CostDriver
import com.justindwinata.usahanaik.domain.model.MonthlyFocus
import com.justindwinata.usahanaik.domain.model.SellingChannel
import com.justindwinata.usahanaik.domain.model.StockIssue
import com.justindwinata.usahanaik.domain.setup.BusinessSetupField
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class BusinessSetupViewModelTest {
    @Test
    fun defaultStateIsInvalidAndNotReviewVisible() {
        val viewModel = BusinessSetupViewModel()

        assertFalse(viewModel.uiState.value.isValid)
        assertFalse(viewModel.uiState.value.isReviewVisible)
        assertEquals(0, viewModel.uiState.value.completedSectionCount)
    }

    @Test
    fun fieldUpdatesCreateImmutableDraftState() {
        val viewModel = BusinessSetupViewModel()
        val initialDraft = viewModel.uiState.value.draft

        viewModel.updateBusinessName("Toko Naik")
        viewModel.selectCategory("food_beverage")

        assertEquals("", initialDraft.businessName)
        assertEquals("Toko Naik", viewModel.uiState.value.draft.businessName)
        assertEquals("food_beverage", viewModel.uiState.value.draft.categoryId)
    }

    @Test
    fun challengeToggleAddsAndRemovesChallenge() {
        val viewModel = BusinessSetupViewModel()

        viewModel.toggleChallenge(BusinessChallenge.LowSales)
        assertTrue(BusinessChallenge.LowSales in viewModel.uiState.value.draft.challenges)

        viewModel.toggleChallenge(BusinessChallenge.LowSales)
        assertFalse(BusinessChallenge.LowSales in viewModel.uiState.value.draft.challenges)
    }

    @Test
    fun requestReviewShowsErrorsWhenInvalid() {
        val viewModel = BusinessSetupViewModel()

        val allowed = viewModel.requestReview()

        assertFalse(allowed)
        assertTrue(viewModel.uiState.value.hasAttemptedReview)
        assertEquals(
            "Business name is required.",
            viewModel.uiState.value.visibleError(BusinessSetupField.BusinessName)
        )
    }

    @Test
    fun validStateCanOpenReview() {
        val viewModel = BusinessSetupViewModel()
        fillValidDraft(viewModel)

        val allowed = viewModel.requestReview()

        assertTrue(allowed)
        assertTrue(viewModel.uiState.value.isValid)
        assertTrue(viewModel.uiState.value.isReviewVisible)
        assertEquals(5, viewModel.uiState.value.completedSectionCount)
    }

    @Test
    fun resetDraftReturnsToDefaultState() {
        val viewModel = BusinessSetupViewModel()
        fillValidDraft(viewModel)

        viewModel.resetDraft()

        assertTrue(viewModel.uiState.value.draft.isEmpty)
        assertFalse(viewModel.uiState.value.isValid)
    }

    private fun fillValidDraft(viewModel: BusinessSetupViewModel) {
        viewModel.updateBusinessName("Toko Naik")
        viewModel.selectCategory("food_beverage")
        viewModel.updateSellingChannel(SellingChannel.WhatsApp)
        viewModel.updateBusinessStage(BusinessStage.RunningMoreThanOneYear)
        viewModel.updateMonthlyRevenue("Rp 10.000.000")
        viewModel.updateMonthlyExpenses("Rp 7.000.000")
        viewModel.updateEstimatedMonthlyProfit("Rp 3.000.000")
        viewModel.updateProductCount("12")
        viewModel.updateBestSellingProduct("Paket hemat")
        viewModel.updateMainCostDriver(CostDriver.RawMaterials)
        viewModel.updateStockIssue(StockIssue.NoStockIssue)
        viewModel.toggleChallenge(BusinessChallenge.InconsistentContent)
        viewModel.updateTargetMonthlyRevenue("Rp 12.000.000")
        viewModel.updateTargetMonthlyProfit("Rp 4.000.000")
        viewModel.updateMainFocus(MonthlyFocus.ImproveSales)
        viewModel.updateAvailableTime(AvailableTime.ThreeToFiveHours)
    }
}
