package com.justindwinata.usahanaik.domain.model

data class WeeklyRetrospective(
    val id: Long = 0L,
    val weekLabel: String,
    val generatedDate: String,
    val summaryTitle: String,
    val sections: List<RetrospectiveSection>,
    val nextWeekSuggestion: NextWeekSuggestion,
    val createdAt: Long = 0L,
    val updatedAt: Long = 0L
)

data class RetrospectiveSection(
    val title: String,
    val insights: List<RetrospectiveInsight>
)

data class RetrospectiveInsight(
    val message: String,
    val severity: InsightSeverity = InsightSeverity.Info
)

data class NextWeekSuggestion(
    val focus: String,
    val reason: String,
    val recommendedAction: String
)
