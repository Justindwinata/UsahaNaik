package com.justindwinata.usahanaik.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ContentIdeaDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIdea(idea: ContentIdeaEntity): Long

    @Update
    suspend fun updateIdea(idea: ContentIdeaEntity)

    @Query("SELECT * FROM content_ideas WHERE id = :id LIMIT 1")
    suspend fun getIdea(id: Long): ContentIdeaEntity?

    @Query("SELECT * FROM content_ideas ORDER BY updatedAt DESC")
    suspend fun listIdeas(): List<ContentIdeaEntity>

    @Query("SELECT * FROM content_ideas ORDER BY updatedAt DESC")
    fun observeIdeas(): Flow<List<ContentIdeaEntity>>

    @Query("SELECT * FROM content_ideas WHERE status = :status ORDER BY updatedAt DESC")
    suspend fun listIdeasByStatus(status: String): List<ContentIdeaEntity>

    @Query("SELECT * FROM content_ideas WHERE isFavorite = 1 ORDER BY updatedAt DESC")
    suspend fun listFavoriteIdeas(): List<ContentIdeaEntity>

    @Query("UPDATE content_ideas SET status = :status, updatedAt = :updatedAt WHERE id = :id")
    suspend fun updateStatus(id: Long, status: String, updatedAt: Long)

    @Query("UPDATE content_ideas SET isFavorite = :isFavorite, updatedAt = :updatedAt WHERE id = :id")
    suspend fun updateFavorite(id: Long, isFavorite: Boolean, updatedAt: Long)

    @Query("DELETE FROM content_ideas WHERE id = :id")
    suspend fun deleteIdea(id: Long)

    @Query("DELETE FROM content_ideas")
    suspend fun clearIdeas()
}
