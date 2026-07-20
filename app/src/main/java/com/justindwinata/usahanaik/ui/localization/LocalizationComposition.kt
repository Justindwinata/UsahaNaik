package com.justindwinata.usahanaik.ui.localization

import androidx.compose.runtime.staticCompositionLocalOf
import com.justindwinata.usahanaik.domain.localization.AppCopyProvider
import com.justindwinata.usahanaik.domain.localization.AppLanguage
import com.justindwinata.usahanaik.domain.localization.AppStrings

val LocalAppLanguage = staticCompositionLocalOf { AppLanguage.Default }

val LocalAppStrings = staticCompositionLocalOf<AppStrings> {
    AppCopyProvider.strings(AppLanguage.Default)
}
