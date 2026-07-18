package com.justindwinata.usahanaik.ui.dashboard

import com.justindwinata.usahanaik.domain.model.BusinessDiagnosis
import com.justindwinata.usahanaik.domain.model.BusinessProfile
import com.justindwinata.usahanaik.domain.model.FinancialTrackingSummary

data class DashboardInsightsUiState(
    val isLoading: Boolean = false,
    val profile: BusinessProfile? = null,
    val financialSummary: FinancialTrackingSummary = FinancialTrackingSummary(),
    val diagnosis: BusinessDiagnosis? = null,
    val emptyStateMessage: String? = null,
    val errorMessage: String? = null
)
