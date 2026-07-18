package com.justindwinata.usahanaik.ui.setup

import com.justindwinata.usahanaik.data.repository.BusinessProfileRepository
import com.justindwinata.usahanaik.domain.model.AvailableTime
import com.justindwinata.usahanaik.domain.model.BusinessChallenge
import com.justindwinata.usahanaik.domain.model.BusinessProfile
import com.justindwinata.usahanaik.domain.model.BusinessSetupDraft
import com.justindwinata.usahanaik.domain.model.BusinessStage
import com.justindwinata.usahanaik.domain.model.CostDriver
import com.justindwinata.usahanaik.domain.model.MonthlyFocus
import com.justindwinata.usahanaik.domain.model.SellingChannel
import com.justindwinata.usahanaik.domain.model.StockIssue
import com.justindwinata.usahanaik.domain.setup.BusinessSetupField
import com.justindwinata.usahanaik.test.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class BusinessSetupViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

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

    @Test
    fun saveCompletedProfileDoesNotSaveInvalidDraft() = runTest {
        val repository = FakeBusinessProfileRepository()
        val viewModel = BusinessSetupViewModel(repository)

        viewModel.saveCompletedProfile()
        advanceUntilIdle()

        assertEquals(0, repository.saveCount)
        assertFalse(viewModel.uiState.value.isValid)
        assertTrue(viewModel.uiState.value.hasAttemptedReview)
    }

    @Test
    fun saveCompletedProfilePersistsValidDraftAndShowsSuccess() = runTest {
        val repository = FakeBusinessProfileRepository()
        val viewModel = BusinessSetupViewModel(repository)
        fillValidDraft(viewModel)

        viewModel.saveCompletedProfile()
        advanceUntilIdle()

        assertEquals(1, repository.saveCount)
        assertEquals("Toko Naik", repository.savedProfile.value?.draft?.businessName)
        assertFalse(viewModel.uiState.value.isSavingProfile)
        assertEquals(
            "Business profile saved locally on this device.",
            viewModel.uiState.value.saveSuccessMessage
        )
    }

    @Test
    fun loadSavedProfileRestoresDraftState() = runTest {
        val repository = FakeBusinessProfileRepository()
        val savedDraft = validDraft()
        repository.saveBusinessProfile(savedDraft)
        val viewModel = BusinessSetupViewModel(repository)

        viewModel.loadSavedProfile()
        advanceUntilIdle()

        assertEquals("Toko Naik", viewModel.uiState.value.draft.businessName)
        assertEquals("food_beverage", viewModel.uiState.value.draft.categoryId)
        assertEquals(savedDraft, viewModel.uiState.value.savedProfile?.draft)
        assertTrue(viewModel.uiState.value.isValid)
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

    private fun validDraft(): BusinessSetupDraft {
        val viewModel = BusinessSetupViewModel()
        fillValidDraft(viewModel)
        return viewModel.uiState.value.draft
    }

    private class FakeBusinessProfileRepository : BusinessProfileRepository {
        val savedProfile = MutableStateFlow<BusinessProfile?>(null)
        var saveCount = 0

        override suspend fun saveBusinessProfile(draft: BusinessSetupDraft): BusinessProfile {
            saveCount += 1
            val profile = BusinessProfile(draft = draft, createdAt = 100L, updatedAt = 100L)
            savedProfile.value = profile
            return profile
        }

        override suspend fun getActiveBusinessProfile(): BusinessProfile? = savedProfile.value

        override fun observeActiveBusinessProfile(): Flow<BusinessProfile?> = savedProfile

        override suspend fun deleteBusinessProfile() {
            savedProfile.value = null
        }

        override suspend fun hasBusinessProfile(): Boolean = savedProfile.value != null
    }
}
