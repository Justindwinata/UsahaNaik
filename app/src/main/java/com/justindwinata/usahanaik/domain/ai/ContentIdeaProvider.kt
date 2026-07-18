package com.justindwinata.usahanaik.domain.ai

import com.justindwinata.usahanaik.domain.model.BusinessCategory
import com.justindwinata.usahanaik.domain.model.ContentIdea

interface ContentIdeaProvider {
    fun generateIdeas(
        category: BusinessCategory,
        businessName: String
    ): List<ContentIdea>
}
