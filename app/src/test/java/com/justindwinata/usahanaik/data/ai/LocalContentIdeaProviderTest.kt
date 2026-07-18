package com.justindwinata.usahanaik.data.ai

import com.justindwinata.usahanaik.data.repository.SampleBusinessCategoryRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class LocalContentIdeaProviderTest {
    private val provider = LocalContentIdeaProvider()
    private val categories = SampleBusinessCategoryRepository().getCategories()

    @Test
    fun providerReturnsDeterministicLocalIdeasWithoutExternalApi() {
        val category = categories.first { it.id == "skincare_beauty" }
        val first = provider.generateIdeas(category, "Glow Lokal")
        val second = provider.generateIdeas(category, "Glow Lokal")

        assertEquals(first, second)
        assertTrue(first.all { it.isAiAssistedPreview })
        assertTrue(first.any { it.platformSuggestion == "Instagram Reels" })
    }

    @Test
    fun generalIdeasIncludeBusinessName() {
        val category = categories.first { it.id == "digital_service" }
        val ideas = provider.generateIdeas(category, "Studio Naik")

        assertTrue(ideas.any { it.title.contains("Studio Naik") })
    }
}
