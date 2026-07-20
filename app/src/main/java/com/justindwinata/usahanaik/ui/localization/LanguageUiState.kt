package com.justindwinata.usahanaik.ui.localization

import com.justindwinata.usahanaik.domain.localization.AppLanguage

data class LanguageUiState(
    val selectedLanguage: AppLanguage = AppLanguage.Default,
    val availableLanguages: List<AppLanguage> = AppLanguage.entries
)
