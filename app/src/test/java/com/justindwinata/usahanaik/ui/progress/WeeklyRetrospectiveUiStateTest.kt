package com.justindwinata.usahanaik.ui.progress

import com.justindwinata.usahanaik.domain.model.WeeklyProgressHistorySummary
import com.justindwinata.usahanaik.domain.model.WeeklyProgressSnapshot
import org.junit.Assert.assertEquals
import org.junit.Test

class WeeklyRetrospectiveUiStateTest {
    @Test
    fun visibleSnapshotPrefersCurrentGeneratedSnapshot() {
        val savedSnapshot = sampleSnapshot("Saved")
        val currentSnapshot = sampleSnapshot("Current")

        val state = WeeklyRetrospectiveUiState(
            currentSnapshot = currentSnapshot,
            progressHistorySummary = WeeklyProgressHistorySummary(
                latestSnapshot = savedSnapshot,
                trendPoints = emptyList(),
                averageTaskCompletionRate = 0.4f
            )
        )

        assertEquals("Current", state.visibleSnapshot?.weekLabel)
    }

    @Test
    fun visibleSnapshotFallsBackToLatestSavedSnapshot() {
        val savedSnapshot = sampleSnapshot("Saved")
        val state = WeeklyRetrospectiveUiState(
            progressHistorySummary = WeeklyProgressHistorySummary(
                latestSnapshot = savedSnapshot,
                trendPoints = emptyList(),
                averageTaskCompletionRate = 0.4f
            )
        )

        assertEquals("Saved", state.visibleSnapshot?.weekLabel)
    }

    private fun sampleSnapshot(weekLabel: String): WeeklyProgressSnapshot {
        return WeeklyProgressSnapshot(
            weekLabel = weekLabel,
            weekStartDate = "2026-07-20",
            totalTasks = 5,
            completedTasks = 2,
            taskCompletionRate = 0.4f,
            milestoneProgress = 0.5f,
            weeklyIncome = 1_000_000L,
            weeklyExpenses = 400_000L,
            weeklyEstimatedProfit = 600_000L,
            profitMarginPercent = 60,
            savedIdeasCount = 3,
            plannedContentCount = 2,
            postedOrDoneContentCount = 1,
            skippedContentCount = 0,
            businessHealthScore = 72,
            warningInsightCount = 1,
            criticalInsightCount = 0
        )
    }
}
