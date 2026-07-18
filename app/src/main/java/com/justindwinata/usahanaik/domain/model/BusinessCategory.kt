package com.justindwinata.usahanaik.domain.model

data class BusinessCategory(
    val id: String,
    val displayName: String,
    val description: String,
    val focusArea: String,
    val sampleRecommendedGoal: String,
    val setupHints: List<String> = emptyList(),
    val recommendedMonthlyFocus: MonthlyFocus = MonthlyFocus.OrganizeFinance
)
