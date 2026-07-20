package com.justindwinata.usahanaik.ui.localization

import com.justindwinata.usahanaik.data.preferences.LanguagePreferenceRepository
import com.justindwinata.usahanaik.domain.localization.AppLanguage
import com.justindwinata.usahanaik.test.MainDispatcherRule
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class LanguageViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun startsWithPersistedLanguage() = runTest {
        val repository = FakeLanguagePreferenceRepository(AppLanguage.English)
        val viewModel = LanguageViewModel(repository)

        assertEquals(AppLanguage.English, viewModel.uiState.value.selectedLanguage)
    }

    @Test
    @OptIn(ExperimentalCoroutinesApi::class)
    fun selectedLanguagePersistsThroughRepository() = runTest {
        val repository = FakeLanguagePreferenceRepository()
        val viewModel = LanguageViewModel(repository)

        viewModel.selectLanguage(AppLanguage.English)
        advanceUntilIdle()

        assertEquals(AppLanguage.English, repository.currentLanguage())
        assertEquals(AppLanguage.English, viewModel.uiState.value.selectedLanguage)
    }

    private class FakeLanguagePreferenceRepository(
        initialLanguage: AppLanguage = AppLanguage.Default
    ) : LanguagePreferenceRepository {
        private val state = MutableStateFlow(initialLanguage)

        override val selectedLanguage: Flow<AppLanguage> = state

        override fun currentLanguage(): AppLanguage = state.value

        override suspend fun setLanguage(language: AppLanguage) {
            state.value = language
        }
    }
}
