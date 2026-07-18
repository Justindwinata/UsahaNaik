package com.justindwinata.usahanaik.data.ai

import com.justindwinata.usahanaik.data.repository.SampleBusinessCategoryRepository
import com.justindwinata.usahanaik.domain.model.BusinessChallenge
import com.justindwinata.usahanaik.domain.model.BusinessProfile
import com.justindwinata.usahanaik.domain.model.BusinessSetupDraft
import com.justindwinata.usahanaik.domain.model.ContentGoal
import com.justindwinata.usahanaik.domain.model.ContentIdeaRequest
import com.justindwinata.usahanaik.domain.model.ContentPlatform
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
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
    fun generatesFiveToTenIdeasWithRequiredFields() {
        val result = provider.generateIdeas(request("food_beverage"))

        assertTrue(result.ideas.size in 5..10)
        assertTrue(result.ideas.all { it.hook.isNotBlank() })
        assertTrue(result.ideas.all { it.captionDraft.isNotBlank() })
        assertTrue(result.ideas.all { it.cta.isNotBlank() })
        assertTrue(result.ideas.all { it.visualSuggestion.isNotBlank() })
        assertTrue(result.promotionCampaigns.isNotEmpty())
    }

    @Test
    fun generatesSkincareIdeasWithoutMedicalClaims() {
        val result = provider.generateIdeas(request("skincare_beauty"))

        assertTrue(result.ideas.any { it.title.contains("Skincare") || it.title.contains("Ingredient") })
        assertTrue(result.ideas.any { it.safetyNote.contains("klaim medis") || it.safetyNote.contains("menyembuhkan") })
        assertUnsafeClaimsAbsent(result.ideas.joinToString(" ") { it.captionDraft + it.safetyNote })
    }

    @Test
    fun generatesFoodIdeasWithMenuAngles() {
        val result = provider.generateIdeas(request("food_beverage"))

        assertTrue(result.ideas.any { it.title.contains("Menu") || it.title.contains("Bundle") })
        assertTrue(result.ideas.any { it.safetyNote.contains("klaim kesehatan") || it.safetyNote.contains("fake scarcity") })
    }

    @Test
    fun generatesWarungIdeasWithRestockAndBundleAngles() {
        val result = provider.generateIdeas(request("warung_kelontong"))

        assertTrue(result.ideas.any { it.title.contains("Restock") })
        assertTrue(result.ideas.any { it.title.contains("Kebutuhan Harian") })
    }

    @Test
    fun generatesChallengeAwareIdeasForInconsistentContent() {
        val result = provider.generateIdeas(
            request(
                categoryId = "online_shop_reseller",
                challenges = setOf(BusinessChallenge.InconsistentContent)
            )
        )

        assertTrue(result.ideas.any { it.title.contains("3 Konten") })
    }

    @Test
    fun generatesChallengeAwareIdeasForRepeatOrder() {
        val result = provider.generateIdeas(
            request(
                categoryId = "laundry",
                challenges = setOf(BusinessChallenge.LowRepeatOrders)
            )
        )

        assertTrue(result.ideas.any { it.title.contains("Follow-Up") || it.title.contains("Repeat") })
    }

    @Test
    fun generalIdeasIncludeBusinessName() {
        val category = categories.first { it.id == "digital_service" }
        val ideas = provider.generateIdeas(category, "Studio Naik")

        assertTrue(ideas.any { it.title.contains("Studio Naik") })
    }

    private fun request(
        categoryId: String,
        challenges: Set<BusinessChallenge> = emptySet()
    ): ContentIdeaRequest {
        return ContentIdeaRequest(
            businessProfile = BusinessProfile(
                draft = BusinessSetupDraft(
                    businessName = "Usaha Test",
                    categoryId = categoryId,
                    challenges = challenges
                ),
                createdAt = 1L,
                updatedAt = 1L
            ),
            goal = ContentGoal.ProductEducation,
            platform = ContentPlatform.InstagramReels,
            ideaCount = 6
        )
    }

    private fun assertUnsafeClaimsAbsent(text: String) {
        val lowered = text.lowercase()
        assertFalse(lowered.contains("pasti sembuh"))
        assertFalse(lowered.contains("dijamin viral"))
        assertFalse(lowered.contains("dijamin laris"))
        assertFalse(lowered.contains("guaranteed"))
    }
}
