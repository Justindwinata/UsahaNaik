package com.justindwinata.usahanaik.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ContentCalendarDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(item: ContentCalendarEntity): Long

    @Query("SELECT * FROM content_calendar_items WHERE id = :id LIMIT 1")
    suspend fun getItem(id: Long): ContentCalendarEntity?

    @Query("SELECT * FROM content_calendar_items ORDER BY scheduledDate ASC, timeLabel ASC, updatedAt DESC")
    suspend fun listItems(): List<ContentCalendarEntity>

    @Query("SELECT * FROM content_calendar_items ORDER BY scheduledDate ASC, timeLabel ASC, updatedAt DESC")
    fun observeItems(): Flow<List<ContentCalendarEntity>>

    @Query("SELECT * FROM content_calendar_items WHERE scheduledDate >= :date ORDER BY scheduledDate ASC, timeLabel ASC")
    suspend fun listUpcoming(date: String): List<ContentCalendarEntity>

    @Query("SELECT * FROM content_calendar_items WHERE scheduledDate BETWEEN :startDate AND :endDate ORDER BY scheduledDate ASC, timeLabel ASC")
    suspend fun listBetween(startDate: String, endDate: String): List<ContentCalendarEntity>

    @Query("UPDATE content_calendar_items SET status = :status, updatedAt = :updatedAt WHERE id = :id")
    suspend fun updateStatus(id: Long, status: String, updatedAt: Long)

    @Query("DELETE FROM content_calendar_items WHERE id = :id")
    suspend fun deleteItem(id: Long)

    @Query("DELETE FROM content_calendar_items")
    suspend fun clearItems()
}
