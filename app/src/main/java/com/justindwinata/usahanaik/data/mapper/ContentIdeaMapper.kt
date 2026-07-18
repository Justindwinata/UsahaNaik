package com.justindwinata.usahanaik.data.mapper

import com.justindwinata.usahanaik.data.local.ContentIdeaEntity
import com.justindwinata.usahanaik.domain.model.BusinessChallenge
import com.justindwinata.usahanaik.domain.model.ContentGenerationSource
import com.justindwinata.usahanaik.domain.model.ContentGoal
import com.justindwinata.usahanaik.domain.model.ContentIdea
import com.justindwinata.usahanaik.domain.model.ContentIdeaStatus
import com.justindwinata.usahanaik.domain.model.ContentIdeaType
import com.justindwinata.usahanaik.domain.model.ContentPlatform

object ContentIdeaMapper {
    fun ContentIdea.toEntity(now: Long): ContentIdeaEntity {
        return ContentIdeaEntity(
            id = id,
            title = title,
            platform = platform.name,
            contentType = type.name,
            goal = goal.name,
            angle = angle,
            captionDraft = captionDraft,
            cta = cta,
            hook = hook,
            visualSuggestion = visualSuggestion,
            postingNote = recommendedPostingNote,
            relatedChallenge = relatedBusinessChallenge?.name,
            source = source.name,
            safetyNote = safetyNote,
            status = status.name,
            isFavorite = isFavorite,
            createdAt = createdAt.takeIf { it > 0L } ?: now,
            updatedAt = now
        )
    }

    fun ContentIdeaEntity.toDomain(): ContentIdea {
        val type = enumValueOrDefault(contentType, ContentIdeaType.Educational)
        val platform = enumValueOrDefault(platform, ContentPlatform.InstagramReels)
        return ContentIdea(
            id = id,
            title = title,
            category = type.label,
            platformSuggestion = platform.label,
            angle = angle,
            cta = cta,
            type = type,
            platform = platform,
            goal = enumValueOrDefault(goal, ContentGoal.ProductEducation),
            captionDraft = captionDraft,
            hook = hook,
            visualSuggestion = visualSuggestion,
            recommendedPostingNote = postingNote,
            relatedBusinessChallenge = relatedChallenge?.let { enumValueOrDefault(it, BusinessChallenge.InconsistentContent) },
            source = enumValueOrDefault(source, ContentGenerationSource.Local),
            safetyNote = safetyNote,
            status = enumValueOrDefault(status, ContentIdeaStatus.Draft),
            isFavorite = isFavorite,
            createdAt = createdAt,
            updatedAt = updatedAt
        )
    }

    private inline fun <reified T : Enum<T>> enumValueOrDefault(value: String, default: T): T {
        return runCatching { enumValueOf<T>(value) }.getOrDefault(default)
    }
}
