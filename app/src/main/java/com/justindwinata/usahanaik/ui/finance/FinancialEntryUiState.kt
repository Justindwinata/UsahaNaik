package com.justindwinata.usahanaik.ui.finance

import com.justindwinata.usahanaik.domain.model.ExpenseCategory
import com.justindwinata.usahanaik.domain.model.FinancialEntry
import com.justindwinata.usahanaik.domain.model.FinancialEntryType
import com.justindwinata.usahanaik.domain.model.FinancialTrackingSummary
import com.justindwinata.usahanaik.domain.model.IncomeCategory

data class FinancialEntryFormState(
    val type: FinancialEntryType = FinancialEntryType.Income,
    val title: String = "",
    val amount: String = "",
    val category: String = IncomeCategory.ProductSales.label,
    val date: String = "2026-07-19",
    val note: String = ""
)

data class FinancialEntryValidationResult(
    val titleError: String? = null,
    val amountError: String? = null,
    val categoryError: String? = null,
    val dateError: String? = null
) {
    val isValid: Boolean = listOf(titleError, amountError, categoryError, dateError).all { it == null }
}

data class FinancialEntryUiState(
    val form: FinancialEntryFormState = FinancialEntryFormState(),
    val validationResult: FinancialEntryValidationResult = FinancialEntryValidationResult(),
    val hasAttemptedSave: Boolean = false,
    val isSaving: Boolean = false,
    val isDeleting: Boolean = false,
    val entries: List<FinancialEntry> = emptyList(),
    val summary: FinancialTrackingSummary = FinancialTrackingSummary(),
    val successMessage: String? = null,
    val errorMessage: String? = null,
    val pendingDeleteEntryId: Long? = null
) {
    val categoriesForSelectedType: List<String>
        get() = if (form.type == FinancialEntryType.Income) {
            IncomeCategory.entries.map { it.label }
        } else {
            ExpenseCategory.entries.map { it.label }
        }

    fun visibleTitleError(): String? = validationResult.titleError.takeIf { hasAttemptedSave }

    fun visibleAmountError(): String? = validationResult.amountError.takeIf { hasAttemptedSave }

    fun visibleCategoryError(): String? = validationResult.categoryError.takeIf { hasAttemptedSave }

    fun visibleDateError(): String? = validationResult.dateError.takeIf { hasAttemptedSave }
}
