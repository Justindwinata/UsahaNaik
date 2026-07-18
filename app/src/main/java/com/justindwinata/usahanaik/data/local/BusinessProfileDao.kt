package com.justindwinata.usahanaik.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface BusinessProfileDao {
    @Upsert
    suspend fun upsertProfile(profile: BusinessProfileEntity)

    @Query("SELECT * FROM business_profiles WHERE id = :id LIMIT 1")
    suspend fun getProfile(id: Long = BusinessProfileEntity.ACTIVE_PROFILE_ID): BusinessProfileEntity?

    @Query("SELECT * FROM business_profiles WHERE id = :id LIMIT 1")
    fun observeProfile(id: Long = BusinessProfileEntity.ACTIVE_PROFILE_ID): Flow<BusinessProfileEntity?>

    @Query("SELECT EXISTS(SELECT 1 FROM business_profiles WHERE id = :id)")
    suspend fun hasProfile(id: Long = BusinessProfileEntity.ACTIVE_PROFILE_ID): Boolean

    @Query("DELETE FROM business_profiles WHERE id = :id")
    suspend fun deleteProfile(id: Long = BusinessProfileEntity.ACTIVE_PROFILE_ID)
}
