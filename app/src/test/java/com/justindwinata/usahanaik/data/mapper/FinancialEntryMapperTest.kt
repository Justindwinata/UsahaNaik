package com.justindwinata.usahanaik.data.mapper

import com.justindwinata.usahanaik.data.mapper.FinancialEntryMapper.toDomain
import com.justindwinata.usahanaik.data.mapper.FinancialEntryMapper.toEntity
import com.justindwinata.usahanaik.domain.model.FinancialEntry
import com.justindwinata.usahanaik.domain.model.FinancialEntryType
import org.junit.Assert.assertEquals
import org.junit.Test

class FinancialEntryMapperTest {
    @Test
    fun mapsDomainToEntityAndBack() {
        val entry = FinancialEntry(
            id = 7,
            type = FinancialEntryType.Income,
            title = "Sales",
            amount = 150_000,
            category = "Product sales",
            date = "2026-07-19",
            note = "Morning sales",
            createdAt = 100,
            updatedAt = 200
        )

        val mapped = entry.toEntity().toDomain()

        assertEquals(entry, mapped)
    }

    @Test
    fun unknownTypeFallsBackToExpense() {
        val entity = FinancialEntry(
            type = FinancialEntryType.Income,
            title = "Sales",
            amount = 150_000,
            category = "Product sales",
            date = "2026-07-19"
        ).toEntity().copy(type = "BROKEN")

        assertEquals(FinancialEntryType.Expense, entity.toDomain().type)
    }
}
