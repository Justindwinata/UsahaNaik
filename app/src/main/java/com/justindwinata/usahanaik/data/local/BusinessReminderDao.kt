package com.justindwinata.usahanaik.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface BusinessReminderDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReminder(reminder: BusinessReminderEntity): Long

    @Query("SELECT * FROM business_reminders WHERE id = :id LIMIT 1")
    suspend fun getReminder(id: Long): BusinessReminderEntity?

    @Query("SELECT * FROM business_reminders ORDER BY status ASC, scheduledDate ASC, timeLabel ASC, updatedAt DESC")
    suspend fun listReminders(): List<BusinessReminderEntity>

    @Query("SELECT * FROM business_reminders WHERE status = 'Active' ORDER BY scheduledDate ASC, timeLabel ASC, updatedAt DESC")
    suspend fun listActiveReminders(): List<BusinessReminderEntity>

    @Query("SELECT * FROM business_reminders ORDER BY status ASC, scheduledDate ASC, timeLabel ASC, updatedAt DESC")
    fun observeReminders(): Flow<List<BusinessReminderEntity>>

    @Query("UPDATE business_reminders SET status = :status, updatedAt = :updatedAt WHERE id = :id")
    suspend fun updateStatus(id: Long, status: String, updatedAt: Long)

    @Query("DELETE FROM business_reminders WHERE id = :id")
    suspend fun deleteReminder(id: Long)

    @Query("DELETE FROM business_reminders")
    suspend fun clearReminders()
}
