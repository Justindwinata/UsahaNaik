package com.justindwinata.usahanaik.domain.setup

import com.justindwinata.usahanaik.domain.model.BusinessCategory
import com.justindwinata.usahanaik.domain.model.MonthlyFocus

data class CategorySetupGuidance(
    val categoryName: String,
    val focusArea: String,
    val setupHints: List<String>,
    val recommendedMonthlyFocus: MonthlyFocus,
    val sampleGoal: String
)

object BusinessCategorySetupHints {
    private val genericHints = listOf(
        "Track daily sales.",
        "Review monthly expenses.",
        "Choose one measurable target.",
        "Create simple weekly actions."
    )

    fun guidanceFor(category: BusinessCategory): CategorySetupGuidance {
        return CategorySetupGuidance(
            categoryName = category.displayName,
            focusArea = category.focusArea,
            setupHints = category.setupHints.ifEmpty { genericHints },
            recommendedMonthlyFocus = category.recommendedMonthlyFocus,
            sampleGoal = category.sampleRecommendedGoal
        )
    }
}
