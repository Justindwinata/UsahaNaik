package com.justindwinata.usahanaik.ui.weekly

import com.justindwinata.usahanaik.domain.model.BusinessProfile
import com.justindwinata.usahanaik.domain.model.WeeklyGrowthPlan

data class WeeklyPlanUiState(
    val isLoading: Boolean = false,
    val isGenerating: Boolean = false,
    val profile: BusinessProfile? = null,
    val activePlan: WeeklyGrowthPlan? = null,
    val showRegenerateConfirmation: Boolean = false,
    val successMessage: String? = null,
    val errorMessage: String? = null
) {
    val emptyStateMessage: String?
        get() = when {
            profile == null -> "Complete business setup first."
            activePlan == null -> "Generate your first weekly growth plan."
            else -> null
        }
}
