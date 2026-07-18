package com.justindwinata.usahanaik.ui.content

import com.justindwinata.usahanaik.domain.model.ContentGoal
import com.justindwinata.usahanaik.domain.model.ContentIdea
import com.justindwinata.usahanaik.domain.model.ContentIdeaFilter
import com.justindwinata.usahanaik.domain.model.ContentIdeaType
import com.justindwinata.usahanaik.domain.model.ContentPlatform
import com.justindwinata.usahanaik.domain.model.ContentGenerationSource
import com.justindwinata.usahanaik.domain.model.PromotionCampaign

data class ContentGenerationFormState(
    val platform: ContentPlatform = ContentPlatform.InstagramReels,
    val goal: ContentGoal = ContentGoal.ProductEducation,
    val type: ContentIdeaType? = null,
    val ideaCount: Int = 6
)

data class ContentPlannerUiState(
    val isLoading: Boolean = false,
    val isGenerating: Boolean = false,
    val hasProfile: Boolean = false,
    val form: ContentGenerationFormState = ContentGenerationFormState(),
    val generatedIdeas: List<ContentIdea> = emptyList(),
    val savedIdeas: List<ContentIdea> = emptyList(),
    val promotionCampaigns: List<PromotionCampaign> = emptyList(),
    val filter: ContentIdeaFilter = ContentIdeaFilter.All,
    val generationSource: ContentGenerationSource = ContentGenerationSource.Local,
    val usedFallback: Boolean = false,
    val successMessage: String? = null,
    val errorMessage: String? = null
) {
    val emptyStateMessage: String?
        get() = if (!hasProfile) "Complete business setup first." else null
}
