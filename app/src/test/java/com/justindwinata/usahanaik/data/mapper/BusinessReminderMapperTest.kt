package com.justindwinata.usahanaik.data.mapper

import com.justindwinata.usahanaik.data.local.BusinessReminderEntity
import com.justindwinata.usahanaik.data.mapper.BusinessReminderMapper.toDomain
import com.justindwinata.usahanaik.data.mapper.BusinessReminderMapper.toEntity
import com.justindwinata.usahanaik.domain.model.BusinessReminder
import com.justindwinata.usahanaik.domain.model.ReminderFrequency
import com.justindwinata.usahanaik.domain.model.ReminderStatus
import com.justindwinata.usahanaik.domain.model.ReminderType
import org.junit.Assert.assertEquals
import org.junit.Test

class BusinessReminderMapperTest {
    @Test
    fun mapsDomainToEntity() {
        val entity = sampleReminder().toEntity(now = 200L)

        assertEquals("DailyFinancialTracking", entity.type)
        assertEquals("Daily", entity.frequency)
        assertEquals("Active", entity.status)
        assertEquals(200L, entity.createdAt)
        assertEquals(200L, entity.updatedAt)
    }

    @Test
    fun mapsEntityToDomain() {
        val domain = BusinessReminderEntity(
            id = 7L,
            title = "Review report",
            description = "Open report dashboard.",
            type = "BusinessReportReview",
            frequency = "Weekly",
            scheduledDay = "Friday",
            scheduledDate = "",
            timeLabel = "10:00",
            status = "Paused",
            relatedEntityId = null,
            createdAt = 10L,
            updatedAt = 20L
        ).toDomain()

        assertEquals(ReminderType.BusinessReportReview, domain.type)
        assertEquals(ReminderFrequency.Weekly, domain.frequency)
        assertEquals(ReminderStatus.Paused, domain.status)
    }

    @Test
    fun invalidEnumValuesUseSafeFallbacks() {
        val domain = BusinessReminderEntity(
            id = 1L,
            title = "Unknown",
            description = "Unknown",
            type = "BadType",
            frequency = "BadFrequency",
            scheduledDay = "",
            scheduledDate = "",
            timeLabel = "20:00",
            status = "BadStatus",
            relatedEntityId = null,
            createdAt = 0L,
            updatedAt = 0L
        ).toDomain()

        assertEquals(ReminderType.DailyFinancialTracking, domain.type)
        assertEquals(ReminderFrequency.Daily, domain.frequency)
        assertEquals(ReminderStatus.Active, domain.status)
    }

    private fun sampleReminder(): BusinessReminder {
        return BusinessReminder(
            title = "Record sales",
            description = "Record today's income and expenses.",
            type = ReminderType.DailyFinancialTracking,
            frequency = ReminderFrequency.Daily,
            timeLabel = "20:00"
        )
    }
}
