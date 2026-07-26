package com.justindwinata.usahanaik.domain.content

import com.justindwinata.usahanaik.domain.model.ContentIdea
import com.justindwinata.usahanaik.domain.model.ContentIdeaStatus

data class ContentPlannerDashboardSummary(
    val savedCount: Int,
    val plannedCount: Int,
    val doneCount: Int,
    val favoriteCount: Int,
    val nextIdeaTitle: String,
    val nextIdeaPlatform: String,
    val statusLabel: String,
    val nextIdeaHelper: String,
    val ctaLabel: String,
    val hasIdeas: Boolean
)

object ContentPlannerDashboardSummaryMapper {
    fun from(ideas: List<ContentIdea>): ContentPlannerDashboardSummary {
        val nextIdea = ideas.firstOrNull { it.status == ContentIdeaStatus.Planned }
            ?: ideas.firstOrNull { it.status == ContentIdeaStatus.Draft }
            ?: ideas.firstOrNull()

        return ContentPlannerDashboardSummary(
            savedCount = ideas.size,
            plannedCount = ideas.count { it.status == ContentIdeaStatus.Planned },
            doneCount = ideas.count { it.status == ContentIdeaStatus.Done },
            favoriteCount = ideas.count { it.isFavorite },
            nextIdeaTitle = nextIdea?.title ?: "Generate Content Ideas",
            nextIdeaPlatform = nextIdea?.platform?.label ?: "Content Planner",
            statusLabel = ideas.contentStatusLabel(),
            nextIdeaHelper = nextIdea?.nextIdeaHelper() ?: "Create ideas from your business profile.",
            ctaLabel = if (ideas.isEmpty()) "Generate Content Ideas" else "Open Content Planner",
            hasIdeas = ideas.isNotEmpty()
        )
    }

    private fun List<ContentIdea>.contentStatusLabel(): String {
        return when {
            isEmpty() -> "Needs ideas"
            any { it.status == ContentIdeaStatus.Planned } -> "Plan ready"
            all { it.status == ContentIdeaStatus.Done } -> "Completed"
            else -> "Drafts saved"
        }
    }

    private fun ContentIdea.nextIdeaHelper(): String {
        return when (status) {
            ContentIdeaStatus.Planned -> "Ready to schedule or post."
            ContentIdeaStatus.Done -> "Already marked done."
            ContentIdeaStatus.Draft -> "Review and mark planned when ready."
        }
    }
}
