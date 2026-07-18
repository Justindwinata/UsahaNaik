package com.justindwinata.usahanaik.ui.setup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.justindwinata.usahanaik.data.repository.BusinessProfileRepository
import com.justindwinata.usahanaik.domain.model.AvailableTime
import com.justindwinata.usahanaik.domain.model.BusinessChallenge
import com.justindwinata.usahanaik.domain.model.BusinessSetupDraft
import com.justindwinata.usahanaik.domain.model.BusinessStage
import com.justindwinata.usahanaik.domain.model.CostDriver
import com.justindwinata.usahanaik.domain.model.MonthlyFocus
import com.justindwinata.usahanaik.domain.model.SellingChannel
import com.justindwinata.usahanaik.domain.model.StockIssue
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class BusinessSetupViewModel(
    private val businessProfileRepository: BusinessProfileRepository? = null
) : ViewModel() {
    private val _uiState = MutableStateFlow(BusinessSetupUiState())
    val uiState: StateFlow<BusinessSetupUiState> = _uiState.asStateFlow()

    fun selectCategory(categoryId: String) = updateDraft { copy(categoryId = categoryId) }

    fun updateBusinessName(value: String) = updateDraft { copy(businessName = value) }

    fun updateOwnerName(value: String) = updateDraft { copy(ownerName = value) }

    fun updateSellingChannel(value: SellingChannel) = updateDraft { copy(sellingChannel = value) }

    fun updateBusinessLocation(value: String) = updateDraft { copy(businessLocation = value) }

    fun updateBusinessStage(value: BusinessStage) = updateDraft { copy(businessStage = value) }

    fun updateStartingCapital(value: String) = updateDraft { copy(startingCapital = value) }

    fun updateMonthlyRevenue(value: String) = updateDraft { copy(monthlyRevenue = value) }

    fun updateMonthlyExpenses(value: String) = updateDraft { copy(monthlyExpenses = value) }

    fun updateEstimatedMonthlyProfit(value: String) = updateDraft { copy(estimatedMonthlyProfit = value) }

    fun updateAverageDailyTransactions(value: String) = updateDraft { copy(averageDailyTransactions = value) }

    fun updateAverageTransactionValue(value: String) = updateDraft { copy(averageTransactionValue = value) }

    fun updateProductCount(value: String) = updateDraft { copy(productCount = value) }

    fun updateBestSellingProduct(value: String) = updateDraft { copy(bestSellingProduct = value) }

    fun updateHighestMarginProduct(value: String) = updateDraft { copy(highestMarginProduct = value) }

    fun updateMainCostDriver(value: CostDriver) = updateDraft { copy(mainCostDriver = value) }

    fun updateStockIssue(value: StockIssue) = updateDraft { copy(stockIssue = value) }

    fun toggleChallenge(challenge: BusinessChallenge) {
        updateDraft {
            val nextChallenges = if (challenge in challenges) {
                challenges - challenge
            } else {
                challenges + challenge
            }
            copy(challenges = nextChallenges)
        }
    }

    fun updateTargetMonthlyRevenue(value: String) = updateDraft { copy(targetMonthlyRevenue = value) }

    fun updateTargetMonthlyProfit(value: String) = updateDraft { copy(targetMonthlyProfit = value) }

    fun updateMainFocus(value: MonthlyFocus) = updateDraft { copy(mainFocus = value) }

    fun updateAvailableTime(value: AvailableTime) = updateDraft { copy(availableTime = value) }

    fun requestReview(): Boolean {
        val nextState = BusinessSetupUiState.fromDraft(
            draft = _uiState.value.draft,
            hasAttemptedReview = true,
            isReviewVisible = _uiState.value.isValid
        )
        _uiState.value = nextState
        return nextState.isReviewVisible
    }

    fun hideReview() {
        _uiState.value = _uiState.value.copy(isReviewVisible = false)
    }

    fun loadSavedProfile() {
        val repository = businessProfileRepository ?: return
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoadingSavedProfile = true)
            runCatching {
                repository.getActiveBusinessProfile()
            }.onSuccess { profile ->
                _uiState.value = if (profile == null) {
                    _uiState.value.copy(isLoadingSavedProfile = false)
                } else {
                    BusinessSetupUiState.fromDraft(
                        draft = profile.draft,
                        savedProfile = profile
                    ).copy(isLoadingSavedProfile = false)
                }
            }.onFailure { error ->
                _uiState.value = _uiState.value.copy(
                    isLoadingSavedProfile = false,
                    saveErrorMessage = error.message ?: "Failed to load saved business profile."
                )
            }
        }
    }

    fun saveCompletedProfile(onSaved: () -> Unit = {}) {
        val reviewAllowed = requestReview()
        if (!reviewAllowed) return

        val repository = businessProfileRepository
        if (repository == null) {
            _uiState.value = _uiState.value.copy(
                saveErrorMessage = "Local profile storage is not available yet."
            )
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isSavingProfile = true,
                saveSuccessMessage = null,
                saveErrorMessage = null
            )
            runCatching {
                repository.saveBusinessProfile(_uiState.value.draft)
            }.onSuccess { profile ->
                _uiState.value = _uiState.value.copy(
                    isSavingProfile = false,
                    savedProfile = profile,
                    saveSuccessMessage = "Business profile saved locally on this device.",
                    saveErrorMessage = null
                )
                onSaved()
            }.onFailure { error ->
                _uiState.value = _uiState.value.copy(
                    isSavingProfile = false,
                    saveErrorMessage = error.message ?: "Failed to save business profile."
                )
            }
        }
    }

    fun resetDraft() {
        _uiState.value = BusinessSetupUiState()
    }

    fun deleteSavedProfile() {
        val repository = businessProfileRepository
        if (repository == null) {
            _uiState.value = _uiState.value.copy(
                deleteErrorMessage = "Local profile storage is not available yet."
            )
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isDeletingProfile = true,
                deleteSuccessMessage = null,
                deleteErrorMessage = null
            )
            runCatching {
                repository.deleteBusinessProfile()
            }.onSuccess {
                _uiState.value = BusinessSetupUiState(
                    deleteSuccessMessage = "Local business profile deleted from this device."
                )
            }.onFailure { error ->
                _uiState.value = _uiState.value.copy(
                    isDeletingProfile = false,
                    deleteErrorMessage = error.message ?: "Failed to delete local business profile."
                )
            }
        }
    }

    private fun updateDraft(reducer: BusinessSetupDraft.() -> BusinessSetupDraft) {
        val current = _uiState.value
        val nextDraft = current.draft.reducer()
        _uiState.value = BusinessSetupUiState.fromDraft(
            draft = nextDraft,
            hasAttemptedReview = current.hasAttemptedReview,
            isReviewVisible = false,
            savedProfile = current.savedProfile
        )
    }
}

class BusinessSetupViewModelFactory(
    private val businessProfileRepository: BusinessProfileRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BusinessSetupViewModel::class.java)) {
            return BusinessSetupViewModel(businessProfileRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}
