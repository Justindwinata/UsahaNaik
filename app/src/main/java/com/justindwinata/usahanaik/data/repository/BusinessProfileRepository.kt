package com.justindwinata.usahanaik.data.repository

import com.justindwinata.usahanaik.domain.model.BusinessProfile
import com.justindwinata.usahanaik.domain.model.BusinessSetupDraft
import kotlinx.coroutines.flow.Flow

interface BusinessProfileRepository {
    suspend fun saveBusinessProfile(draft: BusinessSetupDraft): BusinessProfile

    suspend fun getActiveBusinessProfile(): BusinessProfile?

    fun observeActiveBusinessProfile(): Flow<BusinessProfile?>

    suspend fun deleteBusinessProfile()

    suspend fun hasBusinessProfile(): Boolean
}
