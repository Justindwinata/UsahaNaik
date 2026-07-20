package com.justindwinata.usahanaik.ui.localization

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.justindwinata.usahanaik.data.preferences.LanguagePreferenceRepository
import com.justindwinata.usahanaik.domain.localization.AppLanguage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LanguageViewModel(
    private val repository: LanguagePreferenceRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(
        LanguageUiState(selectedLanguage = repository.currentLanguage())
    )
    val uiState: StateFlow<LanguageUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.selectedLanguage.collectLatest { language ->
                _uiState.update { it.copy(selectedLanguage = language) }
            }
        }
    }

    fun selectLanguage(language: AppLanguage) {
        viewModelScope.launch {
            repository.setLanguage(language)
        }
    }
}

class LanguageViewModelFactory(
    private val repository: LanguagePreferenceRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return LanguageViewModel(repository) as T
    }
}
