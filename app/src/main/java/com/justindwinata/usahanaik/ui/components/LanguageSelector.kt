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
import com.justindwinata.usahanaik.ui.localization.LocalAppStrings
import com.justindwinata.usahanaik.ui.theme.AppSpacing
import com.justindwinata.usahanaik.ui.theme.OnSurface
import com.justindwinata.usahanaik.ui.theme.OnSurfaceVariant
import com.justindwinata.usahanaik.ui.theme.Secondary
import com.justindwinata.usahanaik.ui.theme.SecondaryFixed
import com.justindwinata.usahanaik.ui.theme.SurfaceContainerLowest
import androidx.compose.foundation.shape.CircleShape

@Composable
fun LanguageSelector(
    selectedLanguage: AppLanguage,
    onLanguageSelected: (AppLanguage) -> Unit,
    modifier: Modifier = Modifier,
    languages: List<AppLanguage> = AppLanguage.entries
) {
    val strings = LocalAppStrings.current
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
                        contentDescription = "${strings.selectLanguagePrefix}: ${language.nativeName}"
                    },
                selected = selected,
                onClick = { onLanguageSelected(language) },
                label = {
                    Text(
                        text = language.nativeName,
                        style = MaterialTheme.typography.labelLarge,
                        color = if (selected) OnSurface else OnSurfaceVariant
                    )
                },
                colors = FilterChipDefaults.filterChipColors(
                    containerColor = if (selected) SecondaryFixed else SurfaceContainerLowest,
                    labelColor = if (selected) OnSurface else OnSurfaceVariant,
                    selectedContainerColor = SecondaryFixed,
                    selectedLabelColor = OnSurface
                ),
                shape = CircleShape
            )
        }
    }
}