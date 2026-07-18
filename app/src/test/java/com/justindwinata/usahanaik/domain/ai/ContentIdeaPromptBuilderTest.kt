package com.justindwinata.usahanaik.domain.ai

import com.justindwinata.usahanaik.domain.model.BusinessProfile
import com.justindwinata.usahanaik.domain.model.BusinessSetupDraft
import com.justindwinata.usahanaik.domain.model.ContentGoal
import com.justindwinata.usahanaik.domain.model.ContentIdeaRequest
import com.justindwinata.usahanaik.domain.model.ContentPlatform
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ContentIdeaPromptBuilderTest {
    @Test
    fun promptIncludesContextSafetyRulesAndStructuredOutput() {
        val prompt = ContentIdeaPromptBuilder().buildPrompt(
            ContentIdeaRequest(
                businessProfile = BusinessProfile(
                    draft = BusinessSetupDraft(
                        businessName = "Glow Lokal",
                        categoryId = "skincare_beauty"
                    ),
                    createdAt = 1L,
                    updatedAt = 1L
                ),
                goal = ContentGoal.ProductEducation,
                platform = ContentPlatform.InstagramReels
            )
        )

        assertTrue(prompt.contains("skincare_beauty"))
        assertTrue(prompt.contains("Instagram Reels"))
        assertTrue(prompt.contains("Product education"))
        assertTrue(prompt.contains("Do not promise guaranteed sales"))
        assertTrue(prompt.contains("Return structured content ideas"))
    }

    @Test
    fun promptDoesNotIncludeApiKey() {
        val apiKey = "sk-test-secret"
        val prompt = ContentIdeaPromptBuilder().buildPrompt(ContentIdeaRequest(businessProfile = null))

        assertFalse(prompt.contains(apiKey))
        assertFalse(prompt.lowercase().contains("api key"))
    }

    @Test
    fun settingsMasksApiKey() {
        val settings = AiContentSettings(
            providerMode = AiProviderMode.AiAssistedIfConfigured,
            apiKey = "secret-123456"
        )

        assertTrue(settings.isAiConfigured)
        assertTrue(settings.maskedApiKey.endsWith("3456"))
        assertFalse(settings.maskedApiKey.contains("secret-12"))
    }
}
