package com.justindwinata.usahanaik.data.ai

import com.justindwinata.usahanaik.domain.ai.AiContentSettings
import com.justindwinata.usahanaik.domain.ai.ContentIdeaProvider
import com.justindwinata.usahanaik.domain.ai.ContentIdeaPromptBuilder
import com.justindwinata.usahanaik.domain.model.BusinessCategory
import com.justindwinata.usahanaik.domain.model.ContentGenerationSource
import com.justindwinata.usahanaik.domain.model.ContentIdea
import com.justindwinata.usahanaik.domain.model.ContentIdeaRequest
import com.justindwinata.usahanaik.domain.model.ContentIdeaResult

class ConfigurableAiContentIdeaProvider(
    private val settingsProvider: () -> AiContentSettings,
    private val promptBuilder: ContentIdeaPromptBuilder = ContentIdeaPromptBuilder()
) : ContentIdeaProvider {
    override fun generateIdeas(
        category: BusinessCategory,
        businessName: String
    ): List<ContentIdea> {
        throw UnsupportedOperationException("Use request-based generation for configurable AI ideas.")
    }

    override fun generateIdeas(request: ContentIdeaRequest): ContentIdeaResult {
        val settings = settingsProvider()
        if (!settings.isAiConfigured) {
            error("AI provider is not configured.")
        }
        promptBuilder.buildPrompt(request)
        error("Remote AI generation is planned but not implemented in this demo build.")
    }
}

class FallbackContentIdeaProvider(
    private val primaryProvider: ContentIdeaProvider,
    private val fallbackProvider: ContentIdeaProvider
) : ContentIdeaProvider {
    override fun generateIdeas(
        category: BusinessCategory,
        businessName: String
    ): List<ContentIdea> = fallbackProvider.generateIdeas(category, businessName)

    override fun generateIdeas(request: ContentIdeaRequest): ContentIdeaResult {
        return runCatching {
            primaryProvider.generateIdeas(request)
        }.getOrElse {
            val fallback = fallbackProvider.generateIdeas(request)
            fallback.copy(
                ideas = fallback.ideas.map { idea -> idea.copy(source = ContentGenerationSource.Fallback) },
                source = ContentGenerationSource.Fallback,
                usedFallback = true,
                message = "AI-assisted generation was unavailable. Local fallback ideas were generated."
            )
        }
    }
}
