package com.justindwinata.usahanaik.domain.ai

import com.justindwinata.usahanaik.domain.model.BusinessCategory
import com.justindwinata.usahanaik.domain.model.ContentIdea
import com.justindwinata.usahanaik.domain.model.ContentIdeaRequest
import com.justindwinata.usahanaik.domain.model.ContentIdeaResult

interface ContentIdeaProvider {
    fun generateIdeas(
        category: BusinessCategory,
        businessName: String
    ): List<ContentIdea>

    fun generateIdeas(request: ContentIdeaRequest): ContentIdeaResult
}
