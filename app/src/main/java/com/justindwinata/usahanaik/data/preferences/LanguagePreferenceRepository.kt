package com.justindwinata.usahanaik.data.preferences

import android.content.Context
import com.justindwinata.usahanaik.domain.localization.AppLanguage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

interface LanguagePreferenceRepository {
    val selectedLanguage: Flow<AppLanguage>

    fun currentLanguage(): AppLanguage

    suspend fun setLanguage(language: AppLanguage)
}

class SharedPreferencesLanguagePreferenceRepository(
    context: Context
) : LanguagePreferenceRepository {
    private val preferences = context.applicationContext.getSharedPreferences(
        PREFERENCES_NAME,
        Context.MODE_PRIVATE
    )
    private val languageState = MutableStateFlow(
        AppLanguage.fromCode(preferences.getString(KEY_LANGUAGE, null))
    )

    override val selectedLanguage: Flow<AppLanguage> = languageState.asStateFlow()

    override fun currentLanguage(): AppLanguage = languageState.value

    override suspend fun setLanguage(language: AppLanguage) {
        preferences.edit().putString(KEY_LANGUAGE, language.code).apply()
        languageState.value = language
    }

    companion object {
        private const val PREFERENCES_NAME = "usahanaik_preferences"
        private const val KEY_LANGUAGE = "selected_language"
    }
}
