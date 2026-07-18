package com.justindwinata.usahanaik.domain.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ContentIdeaModelTest {
    @Test
    fun contentIdeaKeepsPreviewCompatibleFields() {
        val idea = ContentIdea(
            title = "3 tips merawat produk",
            category = "Educational content",
            platformSuggestion = "Instagram Reels",
            angle = "Educate customers with simple tips.",
            cta = "Save this post."
        )

        assertEquals("Educational content", idea.category)
        assertEquals("Instagram Reels", idea.platformSuggestion)
        assertEquals("Educate customers with simple tips.", idea.captionDraft)
        assertEquals(ContentIdeaStatus.Draft, idea.status)
        assertEquals(ContentGenerationSource.Local, idea.source)
    }

    @Test
    fun contentIdeaResultTracksFallbackAndSource() {
        val result = ContentIdeaResult(
            ideas = listOf(
                ContentIdea(
                    title = "Daily story",
                    category = "Daily story update",
                    platformSuggestion = "WhatsApp Story",
                    angle = "Show today's offer.",
                    cta = "Reply to order."
                )
            ),
            source = ContentGenerationSource.Fallback,
            usedFallback = true
        )

        assertTrue(result.usedFallback)
        assertEquals(ContentGenerationSource.Fallback, result.source)
        assertFalse(result.ideas.first().isFavorite)
    }

    @Test
    fun promotionCampaignCarriesNonGuaranteedSafetyNote() {
        val campaign = PromotionCampaign(
            title = "Bundle Mingguan",
            objective = "Promote a simple bundle.",
            recommendedContentSequence = listOf("Story teaser", "Feed post", "Follow-up broadcast"),
            expectedOutcome = "May help customers understand the bundle."
        )

        assertTrue(campaign.expectedOutcome.contains("May help"))
        assertTrue(campaign.safetyNote.contains("Avoid misleading"))
    }
}
