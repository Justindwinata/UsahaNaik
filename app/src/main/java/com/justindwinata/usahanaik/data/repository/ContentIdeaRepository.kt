package com.justindwinata.usahanaik.data.repository

import com.justindwinata.usahanaik.domain.model.ContentIdea
import com.justindwinata.usahanaik.domain.model.ContentIdeaFilter
import com.justindwinata.usahanaik.domain.model.ContentIdeaStatus
import kotlinx.coroutines.flow.Flow

interface ContentIdeaRepository {
    suspend fun saveIdea(idea: ContentIdea): ContentIdea
    suspend fun listIdeas(filter: ContentIdeaFilter = ContentIdeaFilter.All): List<ContentIdea>
    fun observeIdeas(): Flow<List<ContentIdea>>
    suspend fun updateStatus(id: Long, status: ContentIdeaStatus): ContentIdea?
    suspend fun updateFavorite(id: Long, isFavorite: Boolean): ContentIdea?
    suspend fun deleteIdea(id: Long)
    suspend fun clearIdeas()
}
