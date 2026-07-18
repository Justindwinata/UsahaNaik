package com.justindwinata.usahanaik.data.ai

import com.justindwinata.usahanaik.domain.ai.ContentIdeaProvider
import com.justindwinata.usahanaik.domain.model.BusinessCategory
import com.justindwinata.usahanaik.domain.model.ContentGenerationSource
import com.justindwinata.usahanaik.domain.model.ContentIdea
import com.justindwinata.usahanaik.domain.model.ContentIdeaRequest
import com.justindwinata.usahanaik.domain.model.ContentIdeaResult
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class FallbackContentIdeaProviderTest {
    @Test
    fun fallsBackToLocalProviderWhenPrimaryFails() {
        val provider = FallbackContentIdeaProvider(
            primaryProvider = FailingProvider(),
            fallbackProvider = LocalContentIdeaProvider()
        )

        val result = provider.generateIdeas(ContentIdeaRequest(businessProfile = null))

        assertTrue(result.usedFallback)
        assertEquals(ContentGenerationSource.Fallback, result.source)
        assertTrue(result.ideas.isNotEmpty())
        assertTrue(result.ideas.all { it.source == ContentGenerationSource.Fallback })
    }

    private class FailingProvider : ContentIdeaProvider {
        override fun generateIdeas(category: BusinessCategory, businessName: String): List<ContentIdea> {
            error("not used")
        }

        override fun generateIdeas(request: ContentIdeaRequest): ContentIdeaResult {
            error("AI failed")
        }
    }
}
