package com.justindwinata.usahanaik.domain.model

data class ContentIdea(
    val id: Long = 0L,
    val title: String,
    val category: String,
    val platformSuggestion: String,
    val angle: String,
    val cta: String,
    val type: ContentIdeaType = ContentIdeaType.Educational,
    val platform: ContentPlatform = ContentPlatform.InstagramReels,
    val goal: ContentGoal = ContentGoal.ProductEducation,
    val captionDraft: String = angle,
    val hook: String = title,
    val visualSuggestion: String = "Show the product, process, or customer context clearly.",
    val recommendedPostingNote: String = "Review and adjust before posting.",
    val relatedBusinessChallenge: BusinessChallenge? = null,
    val source: ContentGenerationSource = ContentGenerationSource.Local,
    val safetyNote: String = "Generated ideas are suggestions. Review and adjust before posting.",
    val status: ContentIdeaStatus = ContentIdeaStatus.Draft,
    val isFavorite: Boolean = false,
    val isAiAssistedPreview: Boolean = true,
    val createdAt: Long = 0L,
    val updatedAt: Long = 0L
)

enum class ContentIdeaType(val label: String) {
    Educational("Educational content"),
    Promotional("Promotional content"),
    BehindTheScenes("Behind the scenes"),
    Testimonial("Testimonial content"),
    ProductComparison("Product comparison"),
    ProblemSolution("Problem-solution content"),
    BundleCampaign("Bundle campaign"),
    SeasonalCampaign("Seasonal campaign"),
    CustomerFaq("Customer FAQ"),
    DailyStoryUpdate("Daily story update")
}

enum class ContentPlatform(val label: String) {
    InstagramReels("Instagram Reels"),
    InstagramFeed("Instagram Feed"),
    TikTok("TikTok"),
    WhatsAppStory("WhatsApp Story"),
    WhatsAppBroadcast("WhatsApp Broadcast"),
    FacebookPost("Facebook Post"),
    MarketplaceDescription("Marketplace Description"),
    OfflinePosterFlyer("Offline Poster/Flyer")
}

enum class ContentGoal(val label: String) {
    IncreaseAwareness("Increase awareness"),
    ProductEducation("Product education"),
    PromotionCampaign("Promotion campaign"),
    RepeatOrder("Repeat order"),
    CustomerTestimonial("Customer testimonial"),
    BuildTrust("Build trust"),
    LaunchNewProduct("Launch new product"),
    ClearSlowMovingStock("Clear slow-moving stock"),
    AnnounceBundleOffer("Announce bundle offer"),
    ImproveContentConsistency("Improve content consistency")
}

enum class ContentIdeaStatus(val label: String) {
    Draft("Draft"),
    Planned("Planned"),
    Done("Done")
}

enum class ContentIdeaFilter(val label: String) {
    All("All"),
    Draft("Draft"),
    Planned("Planned"),
    Done("Done"),
    Favorite("Favorite")
}

enum class ContentGenerationSource(val label: String) {
    Local("Local ideas"),
    Ai("AI-assisted"),
    Fallback("Fallback ideas")
}

data class PromotionCampaign(
    val title: String,
    val objective: String,
    val recommendedContentSequence: List<String>,
    val expectedOutcome: String,
    val safetyNote: String = "Promotion suggestions should be reviewed before posting. Avoid misleading scarcity or guaranteed results."
)

data class ContentCalendarItem(
    val dateLabel: String,
    val ideaTitle: String,
    val platform: ContentPlatform,
    val status: ContentIdeaStatus
)

data class ContentIdeaRequest(
    val businessProfile: BusinessProfile?,
    val diagnosis: BusinessDiagnosis? = null,
    val weeklyPlan: WeeklyGrowthPlan? = null,
    val financialSummary: FinancialTrackingSummary = FinancialTrackingSummary(),
    val goal: ContentGoal = ContentGoal.ProductEducation,
    val platform: ContentPlatform = ContentPlatform.InstagramReels,
    val type: ContentIdeaType? = null,
    val ideaCount: Int = 6
)

data class ContentIdeaResult(
    val ideas: List<ContentIdea>,
    val promotionCampaigns: List<PromotionCampaign> = emptyList(),
    val source: ContentGenerationSource,
    val usedFallback: Boolean = false,
    val message: String = "Generated ideas are suggestions. Review and adjust before posting."
)
