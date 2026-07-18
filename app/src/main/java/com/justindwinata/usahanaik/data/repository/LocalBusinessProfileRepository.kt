package com.justindwinata.usahanaik.data.repository

import com.justindwinata.usahanaik.data.local.BusinessProfileDao
import com.justindwinata.usahanaik.data.mapper.BusinessProfileMapper.toDomain
import com.justindwinata.usahanaik.data.mapper.BusinessProfileMapper.toEntity
import com.justindwinata.usahanaik.domain.model.BusinessProfile
import com.justindwinata.usahanaik.domain.model.BusinessSetupDraft
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LocalBusinessProfileRepository(
    private val dao: BusinessProfileDao,
    private val nowProvider: () -> Long = { System.currentTimeMillis() }
) : BusinessProfileRepository {
    override suspend fun saveBusinessProfile(draft: BusinessSetupDraft): BusinessProfile {
        val existing = dao.getProfile()
        val now = nowProvider()
        val createdAt = existing?.createdAt ?: now
        val entity = draft.toEntity(
            createdAt = createdAt,
            updatedAt = now
        )
        dao.upsertProfile(entity)
        return entity.toDomain()
    }

    override suspend fun getActiveBusinessProfile(): BusinessProfile? {
        return dao.getProfile()?.toDomain()
    }

    override fun observeActiveBusinessProfile(): Flow<BusinessProfile?> {
        return dao.observeProfile().map { it?.toDomain() }
    }

    override suspend fun deleteBusinessProfile() {
        dao.deleteProfile()
    }

    override suspend fun hasBusinessProfile(): Boolean {
        return dao.hasProfile()
    }
}
