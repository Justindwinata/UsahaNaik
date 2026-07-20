package com.justindwinata.usahanaik.data.mapper

import com.justindwinata.usahanaik.data.local.ContentCalendarEntity
import com.justindwinata.usahanaik.domain.model.ContentCalendarSchedule
import com.justindwinata.usahanaik.domain.model.ContentCalendarStatus
import com.justindwinata.usahanaik.domain.model.ContentPlatform

object ContentCalendarMapper {
    fun ContentCalendarSchedule.toEntity(now: Long): ContentCalendarEntity {
        return ContentCalendarEntity(
            id = id,
            contentIdeaId = contentIdeaId,
            title = title,
            platform = platform.name,
            scheduledDate = scheduledDate,
            timeLabel = timeLabel,
            postingNote = postingNote,
            status = status.name,
            createdAt = createdAt.takeIf { it > 0L } ?: now,
            updatedAt = now
        )
    }

    fun ContentCalendarEntity.toDomain(): ContentCalendarSchedule {
        return ContentCalendarSchedule(
            id = id,
            contentIdeaId = contentIdeaId,
            title = title,
            platform = runCatching { ContentPlatform.valueOf(platform) }.getOrDefault(ContentPlatform.InstagramFeed),
            scheduledDate = scheduledDate,
            timeLabel = timeLabel,
            postingNote = postingNote,
            status = runCatching { ContentCalendarStatus.valueOf(status) }.getOrDefault(ContentCalendarStatus.Planned),
            createdAt = createdAt,
            updatedAt = updatedAt
        )
    }
}
