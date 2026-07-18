package com.justindwinata.usahanaik.data.mapper

import com.justindwinata.usahanaik.data.local.FinancialEntryEntity
import com.justindwinata.usahanaik.domain.model.FinancialEntry
import com.justindwinata.usahanaik.domain.model.FinancialEntryType

object FinancialEntryMapper {
    fun FinancialEntry.toEntity(): FinancialEntryEntity = FinancialEntryEntity(
        id = id,
        type = type.name,
        title = title,
        amount = amount,
        category = category,
        date = date,
        note = note,
        createdAt = createdAt,
        updatedAt = updatedAt
    )

    fun FinancialEntryEntity.toDomain(): FinancialEntry = FinancialEntry(
        id = id,
        type = enumValues<FinancialEntryType>().firstOrNull { it.name == type } ?: FinancialEntryType.Expense,
        title = title,
        amount = amount,
        category = category,
        date = date,
        note = note,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}
