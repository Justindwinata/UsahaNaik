package com.justindwinata.usahanaik.ui.setup

import androidx.lifecycle.ViewModel
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

class BusinessSetupViewModel : ViewModel() {
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

    fun resetDraft() {
        _uiState.value = BusinessSetupUiState()
    }

    private fun updateDraft(reducer: BusinessSetupDraft.() -> BusinessSetupDraft) {
        val current = _uiState.value
        val nextDraft = current.draft.reducer()
        _uiState.value = BusinessSetupUiState.fromDraft(
            draft = nextDraft,
            hasAttemptedReview = current.hasAttemptedReview,
            isReviewVisible = false
        )
    }
}
