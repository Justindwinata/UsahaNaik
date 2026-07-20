package com.justindwinata.usahanaik.ui.progress

import com.justindwinata.usahanaik.domain.model.WeeklyProgressHistorySummary
import com.justindwinata.usahanaik.domain.model.WeeklyProgressSnapshot
import com.justindwinata.usahanaik.domain.model.WeeklyRetrospective

data class WeeklyRetrospectiveUiState(
    val isLoading: Boolean = false,
    val isGenerating: Boolean = false,
    val currentSnapshot: WeeklyProgressSnapshot? = null,
    val latestRetrospective: WeeklyRetrospective? = null,
    val history: List<WeeklyRetrospective> = emptyList(),
    val progressHistorySummary: WeeklyProgressHistorySummary = WeeklyProgressHistorySummary(
        latestSnapshot = null,
        trendPoints = emptyList(),
        averageTaskCompletionRate = 0f
    ),
    val successMessage: String? = null,
    val errorMessage: String? = null
) {
    val emptyStateMessage: String?
        get() = if (latestRetrospective == null) "Generate this week's retrospective from your saved local progress." else null
}
