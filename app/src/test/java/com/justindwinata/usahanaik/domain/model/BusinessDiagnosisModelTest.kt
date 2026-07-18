package com.justindwinata.usahanaik.domain.model

import org.junit.Assert.assertEquals
import org.junit.Test

class BusinessDiagnosisModelTest {
    @Test
    fun scoreBreakdownProgressIsClamped() {
        assertEquals(
            1f,
            DiagnosisScoreBreakdown(
                component = DiagnosisScoreComponent.Profitability,
                score = 24,
                maxScore = 20,
                explanation = "Strong margin"
            ).progress
        )
        assertEquals(
            0f,
            DiagnosisScoreBreakdown(
                component = DiagnosisScoreComponent.ExpenseControl,
                score = -4,
                maxScore = 20,
                explanation = "High expenses"
            ).progress
        )
    }

    @Test
    fun diagnosisSummaryCarriesCountsAndEmptyState() {
        val summary = DashboardInsightSummary(
            financeInsightCount = 2,
            warningCount = 1,
            priorityActionCount = 4,
            goalProgressStatus = "Building foundation",
            emptyStateMessage = "Complete setup first."
        )

        assertEquals(2, summary.financeInsightCount)
        assertEquals(1, summary.warningCount)
        assertEquals(4, summary.priorityActionCount)
        assertEquals("Complete setup first.", summary.emptyStateMessage)
    }
}
