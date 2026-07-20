package com.justindwinata.usahanaik.data.mapper

import com.justindwinata.usahanaik.data.local.BusinessReminderEntity
import com.justindwinata.usahanaik.domain.model.BusinessReminder
import com.justindwinata.usahanaik.domain.model.ReminderFrequency
import com.justindwinata.usahanaik.domain.model.ReminderStatus
import com.justindwinata.usahanaik.domain.model.ReminderType

object BusinessReminderMapper {
    fun BusinessReminder.toEntity(now: Long): BusinessReminderEntity = BusinessReminderEntity(
        id = id,
        title = title,
        description = description,
        type = type.name,
        frequency = frequency.name,
        scheduledDay = scheduledDay,
        scheduledDate = scheduledDate,
        timeLabel = timeLabel,
        status = status.name,
        relatedEntityId = relatedEntityId,
        createdAt = createdAt.takeIf { it > 0L } ?: now,
        updatedAt = now
    )

    fun BusinessReminderEntity.toDomain(): BusinessReminder = BusinessReminder(
        id = id,
        title = title,
        description = description,
        type = runCatching { ReminderType.valueOf(type) }
            .getOrDefault(ReminderType.DailyFinancialTracking),
        frequency = runCatching { ReminderFrequency.valueOf(frequency) }
            .getOrDefault(ReminderFrequency.Daily),
        scheduledDay = scheduledDay,
        scheduledDate = scheduledDate,
        timeLabel = timeLabel,
        status = runCatching { ReminderStatus.valueOf(status) }
            .getOrDefault(ReminderStatus.Active),
        relatedEntityId = relatedEntityId,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}
