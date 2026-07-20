package com.justindwinata.usahanaik.ui.demo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.justindwinata.usahanaik.data.demo.DemoDataSeeder
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DemoDataViewModel(
    private val demoDataSeeder: DemoDataSeeder
) : ViewModel() {
    private val _uiState = MutableStateFlow(DemoDataUiState())
    val uiState: StateFlow<DemoDataUiState> = _uiState.asStateFlow()

    fun loadDemoData() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoadingDemoData = true, successMessage = null, errorMessage = null)
            runCatching { demoDataSeeder.loadDemoData() }
                .onSuccess { result ->
                    _uiState.value = DemoDataUiState(
                        lastResult = result,
                        successMessage = "Demo data loaded. Open Dashboard to explore UsahaNaik."
                    )
                }
                .onFailure { error ->
                    _uiState.value = DemoDataUiState(
                        errorMessage = error.message ?: "Failed to load demo data."
                    )
                }
        }
    }

    fun clearDemoData() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isClearingDemoData = true, successMessage = null, errorMessage = null)
            runCatching { demoDataSeeder.clearDemoData() }
                .onSuccess {
                    _uiState.value = DemoDataUiState(successMessage = "Demo data cleared from this device.")
                }
                .onFailure { error ->
                    _uiState.value = DemoDataUiState(
                        errorMessage = error.message ?: "Failed to clear demo data."
                    )
                }
        }
    }
}

class DemoDataViewModelFactory(
    private val demoDataSeeder: DemoDataSeeder
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DemoDataViewModel::class.java)) {
            return DemoDataViewModel(demoDataSeeder) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}
