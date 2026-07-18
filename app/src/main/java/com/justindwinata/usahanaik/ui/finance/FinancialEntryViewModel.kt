package com.justindwinata.usahanaik.ui.finance

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.justindwinata.usahanaik.data.repository.FinancialEntryRepository
import com.justindwinata.usahanaik.domain.finance.FinancialCalculator
import com.justindwinata.usahanaik.domain.model.ExpenseCategory
import com.justindwinata.usahanaik.domain.model.FinancialEntry
import com.justindwinata.usahanaik.domain.model.FinancialEntryType
import com.justindwinata.usahanaik.domain.model.IncomeCategory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FinancialEntryViewModel(
    private val repository: FinancialEntryRepository,
    private val monthPrefix: String = "2026-07"
) : ViewModel() {
    private val _uiState = MutableStateFlow(FinancialEntryUiState())
    val uiState: StateFlow<FinancialEntryUiState> = _uiState.asStateFlow()

    init {
        refresh()
    }

    fun updateType(type: FinancialEntryType) {
        val category = if (type == FinancialEntryType.Income) {
            IncomeCategory.ProductSales.label
        } else {
            ExpenseCategory.RawMaterials.label
        }
        updateForm { copy(type = type, category = category) }
    }

    fun updateTitle(value: String) = updateForm { copy(title = value) }

    fun updateAmount(value: String) = updateForm { copy(amount = value) }

    fun updateCategory(value: String) = updateForm { copy(category = value) }

    fun updateDate(value: String) = updateForm { copy(date = value) }

    fun updateNote(value: String) = updateForm { copy(note = value) }

    fun saveEntry() {
        val validation = validate(_uiState.value.form)
        _uiState.value = _uiState.value.copy(
            validationResult = validation,
            hasAttemptedSave = true,
            successMessage = null,
            errorMessage = null
        )
        if (!validation.isValid) return

        viewModelScope.launch {
            val form = _uiState.value.form
            val amount = FinancialCalculator.parsePositiveAmount(form.amount) ?: return@launch
            _uiState.value = _uiState.value.copy(isSaving = true)
            runCatching {
                repository.addEntry(
                    FinancialEntry(
                        type = form.type,
                        title = form.title.trim(),
                        amount = amount,
                        category = form.category,
                        date = form.date.trim(),
                        note = form.note.trim()
                    )
                )
            }.onSuccess {
                _uiState.value = _uiState.value.copy(
                    form = FinancialEntryFormState(type = form.type),
                    isSaving = false,
                    hasAttemptedSave = false,
                    successMessage = "Financial entry saved locally.",
                    errorMessage = null
                )
                refresh()
            }.onFailure { error ->
                _uiState.value = _uiState.value.copy(
                    isSaving = false,
                    errorMessage = error.message ?: "Failed to save financial entry."
                )
            }
        }
    }

    fun requestDeleteEntry(id: Long) {
        _uiState.value = _uiState.value.copy(pendingDeleteEntryId = id)
    }

    fun cancelDelete() {
        _uiState.value = _uiState.value.copy(pendingDeleteEntryId = null)
    }

    fun confirmDeleteEntry() {
        val id = _uiState.value.pendingDeleteEntryId ?: return
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isDeleting = true)
            runCatching {
                repository.deleteEntry(id)
            }.onSuccess {
                _uiState.value = _uiState.value.copy(
                    isDeleting = false,
                    pendingDeleteEntryId = null,
                    successMessage = "Financial entry deleted."
                )
                refresh()
            }.onFailure { error ->
                _uiState.value = _uiState.value.copy(
                    isDeleting = false,
                    errorMessage = error.message ?: "Failed to delete financial entry."
                )
            }
        }
    }

    fun refresh(
        targetMonthlyRevenue: String? = null,
        targetMonthlyProfit: String? = null
    ) {
        viewModelScope.launch {
            val entries = repository.listEntriesForMonth(monthPrefix)
            val summary = repository.getFinancialSummary(
                monthPrefix = monthPrefix,
                targetMonthlyRevenue = targetMonthlyRevenue,
                targetMonthlyProfit = targetMonthlyProfit
            )
            _uiState.value = _uiState.value.copy(entries = entries, summary = summary)
        }
    }

    private fun updateForm(reducer: FinancialEntryFormState.() -> FinancialEntryFormState) {
        val next = _uiState.value.form.reducer()
        _uiState.value = _uiState.value.copy(
            form = next,
            validationResult = validate(next),
            successMessage = null,
            errorMessage = null
        )
    }

    private fun validate(form: FinancialEntryFormState): FinancialEntryValidationResult {
        return FinancialEntryValidationResult(
            titleError = if (form.title.isBlank()) "Title is required." else null,
            amountError = if (FinancialCalculator.parsePositiveAmount(form.amount) == null) {
                "Amount must be greater than zero."
            } else {
                null
            },
            categoryError = if (form.category.isBlank()) "Choose a category." else null,
            dateError = if (form.date.isBlank()) "Date is required." else null
        )
    }
}

class FinancialEntryViewModelFactory(
    private val repository: FinancialEntryRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FinancialEntryViewModel::class.java)) {
            return FinancialEntryViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}
