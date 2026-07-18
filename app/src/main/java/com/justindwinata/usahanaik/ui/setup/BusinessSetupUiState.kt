package com.justindwinata.usahanaik.ui.setup

import com.justindwinata.usahanaik.domain.model.BusinessSetupDraft
import com.justindwinata.usahanaik.domain.model.BusinessProfile
import com.justindwinata.usahanaik.domain.setup.BusinessSetupField
import com.justindwinata.usahanaik.domain.setup.BusinessSetupValidationResult
import com.justindwinata.usahanaik.domain.setup.BusinessSetupValidator

data class BusinessSetupUiState(
    val draft: BusinessSetupDraft = BusinessSetupDraft(),
    val validationResult: BusinessSetupValidationResult = BusinessSetupValidator.validate(draft),
    val hasAttemptedReview: Boolean = false,
    val isReviewVisible: Boolean = false,
    val isLoadingSavedProfile: Boolean = false,
    val isSavingProfile: Boolean = false,
    val isDeletingProfile: Boolean = false,
    val savedProfile: BusinessProfile? = null,
    val saveSuccessMessage: String? = null,
    val saveErrorMessage: String? = null,
    val deleteSuccessMessage: String? = null,
    val deleteErrorMessage: String? = null
) {
    val isValid: Boolean = validationResult.isValid

    val completedSectionCount: Int
        get() = listOf(
            isIdentityComplete,
            isFinancialComplete,
            isProductComplete,
            isChallengesComplete,
            isGoalsComplete
        ).count { it }

    val totalSectionCount: Int = 5

    val setupProgress: Float
        get() = completedSectionCount / totalSectionCount.toFloat()

    val isIdentityComplete: Boolean
        get() = draft.businessName.isNotBlank() &&
            !draft.categoryId.isNullOrBlank() &&
            draft.sellingChannel != null &&
            draft.businessStage != null

    val isFinancialComplete: Boolean
        get() = validationResult.errorFor(BusinessSetupField.MonthlyRevenue) == null &&
            validationResult.errorFor(BusinessSetupField.MonthlyExpenses) == null &&
            validationResult.errorFor(BusinessSetupField.EstimatedMonthlyProfit) == null &&
            draft.monthlyRevenue.isNotBlank() &&
            draft.monthlyExpenses.isNotBlank() &&
            draft.estimatedMonthlyProfit.isNotBlank()

    val isProductComplete: Boolean
        get() = validationResult.errorFor(BusinessSetupField.ProductCount) == null &&
            draft.productCount.isNotBlank() &&
            draft.bestSellingProduct.isNotBlank() &&
            draft.mainCostDriver != null &&
            draft.stockIssue != null

    val isChallengesComplete: Boolean
        get() = draft.challenges.isNotEmpty()

    val isGoalsComplete: Boolean
        get() = validationResult.errorFor(BusinessSetupField.TargetMonthlyRevenue) == null &&
            validationResult.errorFor(BusinessSetupField.TargetMonthlyProfit) == null &&
            draft.targetMonthlyRevenue.isNotBlank() &&
            draft.targetMonthlyProfit.isNotBlank() &&
            draft.mainFocus != null &&
            draft.availableTime != null

    fun visibleError(field: BusinessSetupField): String? {
        if (!hasAttemptedReview) return null
        return validationResult.errorFor(field)
    }

    companion object {
        fun fromDraft(
            draft: BusinessSetupDraft,
            hasAttemptedReview: Boolean = false,
            isReviewVisible: Boolean = false,
            savedProfile: BusinessProfile? = null
        ): BusinessSetupUiState = BusinessSetupUiState(
            draft = draft,
            validationResult = BusinessSetupValidator.validate(draft),
            hasAttemptedReview = hasAttemptedReview,
            isReviewVisible = isReviewVisible,
            savedProfile = savedProfile
        )
    }
}
