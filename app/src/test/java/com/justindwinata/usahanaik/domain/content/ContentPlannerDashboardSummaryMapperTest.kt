package com.justindwinata.usahanaik.domain.content

import com.justindwinata.usahanaik.domain.model.ContentGoal
import com.justindwinata.usahanaik.domain.model.ContentIdea
import com.justindwinata.usahanaik.domain.model.ContentIdeaStatus
import com.justindwinata.usahanaik.domain.model.ContentPlatform
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ContentPlannerDashboardSummaryMapperTest {
    @Test
    fun mapsEmptyIdeasToGenerateCta() {
        val summary = ContentPlannerDashboardSummaryMapper.from(emptyList())

        assertFalse(summary.hasIdeas)
        assertEquals(0, summary.savedCount)
        assertEquals("Generate Content Ideas", summary.ctaLabel)
        assertEquals("Generate Content Ideas", summary.nextIdeaTitle)
        assertEquals("Needs ideas", summary.statusLabel)
        assertEquals("Create ideas from your business profile.", summary.nextIdeaHelper)
    }

    @Test
    fun mapsSavedPlannedDoneAndFavoriteCounts() {
        val ideas = listOf(
            sampleIdea(status = ContentIdeaStatus.Draft, favorite = true),
            sampleIdea(title = "Planned promo", status = ContentIdeaStatus.Planned),
            sampleIdea(title = "Done education", status = ContentIdeaStatus.Done)
        )

        val summary = ContentPlannerDashboardSummaryMapper.from(ideas)

        assertTrue(summary.hasIdeas)
        assertEquals(3, summary.savedCount)
        assertEquals(1, summary.plannedCount)
        assertEquals(1, summary.doneCount)
        assertEquals(1, summary.favoriteCount)
        assertEquals("Planned promo", summary.nextIdeaTitle)
        assertEquals("Plan ready", summary.statusLabel)
        assertEquals("Ready to schedule or post.", summary.nextIdeaHelper)
        assertEquals("Open Content Planner", summary.ctaLabel)
    }

    @Test
    fun mapsAllDoneIdeasToCompletedStatus() {
        val summary = ContentPlannerDashboardSummaryMapper.from(
            listOf(sampleIdea(title = "Done idea", status = ContentIdeaStatus.Done))
        )

        assertTrue(summary.hasIdeas)
        assertEquals("Completed", summary.statusLabel)
        assertEquals("Already marked done.", summary.nextIdeaHelper)
    }

    private fun sampleIdea(
        title: String = "Draft idea",
        status: ContentIdeaStatus,
        favorite: Boolean = false
    ): ContentIdea {
        return ContentIdea(
            title = title,
            category = "Food & Beverage",
            platformSuggestion = "Instagram Reels",
            angle = "Show a practical menu story.",
            cta = "Order through WhatsApp today.",
            platform = ContentPlatform.InstagramReels,
            goal = ContentGoal.PromotionCampaign,
            status = status,
            isFavorite = favorite
        )
    }
}
