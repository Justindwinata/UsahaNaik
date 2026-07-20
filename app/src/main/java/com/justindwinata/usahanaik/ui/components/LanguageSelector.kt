package com.justindwinata.usahanaik.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import com.justindwinata.usahanaik.domain.localization.AppLanguage
import com.justindwinata.usahanaik.ui.theme.AppSpacing
import com.justindwinata.usahanaik.ui.theme.CoralPrimary
import com.justindwinata.usahanaik.ui.theme.CoralSoft
import com.justindwinata.usahanaik.ui.theme.InkMuted
import com.justindwinata.usahanaik.ui.theme.SurfaceWarm

@Composable
fun LanguageSelector(
    selectedLanguage: AppLanguage,
    onLanguageSelected: (AppLanguage) -> Unit,
    modifier: Modifier = Modifier,
    languages: List<AppLanguage> = AppLanguage.entries
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(AppSpacing.sm)
    ) {
        languages.forEach { language ->
            val selected = language == selectedLanguage
            FilterChip(
                modifier = Modifier
                    .weight(1f)
                    .semantics {
                        contentDescription = "Select ${language.displayName}"
                    },
                selected = selected,
                onClick = { onLanguageSelected(language) },
                label = {
                    Text(
                        text = language.nativeName,
                        style = MaterialTheme.typography.labelLarge
                    )
                },
                colors = FilterChipDefaults.filterChipColors(
                    containerColor = SurfaceWarm,
                    labelColor = InkMuted,
                    selectedContainerColor = CoralSoft,
                    selectedLabelColor = CoralPrimary
                )
            )
        }
    }
}
