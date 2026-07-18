package com.justindwinata.usahanaik.domain.setup

import com.justindwinata.usahanaik.domain.model.BusinessSetupDraft

data class BusinessSetupValidationResult(
    val fieldErrors: Map<BusinessSetupField, String> = emptyMap()
) {
    val isValid: Boolean = fieldErrors.isEmpty()

    fun errorFor(field: BusinessSetupField): String? = fieldErrors[field]
}

enum class BusinessSetupField {
    BusinessName,
    Category,
    MonthlyRevenue,
    MonthlyExpenses,
    EstimatedMonthlyProfit,
    ProductCount,
    Challenges,
    TargetMonthlyRevenue,
    TargetMonthlyProfit,
    MainFocus,
    AvailableTime
}

object BusinessSetupValidator {
    fun validate(draft: BusinessSetupDraft): BusinessSetupValidationResult {
        val errors = linkedMapOf<BusinessSetupField, String>()

        if (draft.businessName.isBlank()) {
            errors[BusinessSetupField.BusinessName] = "Business name is required."
        }
        if (draft.categoryId.isNullOrBlank()) {
            errors[BusinessSetupField.Category] = "Choose a business category."
        }
        validateRequiredMoney(
            value = draft.monthlyRevenue,
            field = BusinessSetupField.MonthlyRevenue,
            requiredMessage = "Monthly revenue is required.",
            invalidMessage = "Monthly revenue must be zero or higher.",
            errors = errors
        )
        validateRequiredMoney(
            value = draft.monthlyExpenses,
            field = BusinessSetupField.MonthlyExpenses,
            requiredMessage = "Monthly expenses are required.",
            invalidMessage = "Monthly expenses must be zero or higher.",
            errors = errors
        )
        validateRequiredMoney(
            value = draft.estimatedMonthlyProfit,
            field = BusinessSetupField.EstimatedMonthlyProfit,
            requiredMessage = "Estimated monthly profit is required.",
            invalidMessage = "Estimated monthly profit must be zero or higher.",
            errors = errors
        )
        validateRequiredMoney(
            value = draft.productCount,
            field = BusinessSetupField.ProductCount,
            requiredMessage = "Number of products or services is required.",
            invalidMessage = "Number of products or services must be zero or higher.",
            errors = errors
        )
        if (draft.challenges.isEmpty()) {
            errors[BusinessSetupField.Challenges] = "Select at least one business challenge."
        }
        validateRequiredMoney(
            value = draft.targetMonthlyRevenue,
            field = BusinessSetupField.TargetMonthlyRevenue,
            requiredMessage = "Target monthly revenue is required.",
            invalidMessage = "Target monthly revenue must be zero or higher.",
            errors = errors
        )
        validateRequiredMoney(
            value = draft.targetMonthlyProfit,
            field = BusinessSetupField.TargetMonthlyProfit,
            requiredMessage = "Target monthly profit is required.",
            invalidMessage = "Target monthly profit must be zero or higher.",
            errors = errors
        )
        if (draft.mainFocus == null) {
            errors[BusinessSetupField.MainFocus] = "Choose your main focus for this month."
        }
        if (draft.availableTime == null) {
            errors[BusinessSetupField.AvailableTime] = "Choose your available weekly time."
        }

        return BusinessSetupValidationResult(errors)
    }

    private fun validateRequiredMoney(
        value: String,
        field: BusinessSetupField,
        requiredMessage: String,
        invalidMessage: String,
        errors: MutableMap<BusinessSetupField, String>
    ) {
        if (value.isBlank()) {
            errors[field] = requiredMessage
            return
        }

        if (BusinessSetupCalculator.parseIndonesianNumber(value) == null) {
            errors[field] = invalidMessage
        }
    }
}
