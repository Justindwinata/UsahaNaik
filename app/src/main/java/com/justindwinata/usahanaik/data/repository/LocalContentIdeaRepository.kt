package com.justindwinata.usahanaik.data.repository

import com.justindwinata.usahanaik.data.local.ContentIdeaDao
import com.justindwinata.usahanaik.data.mapper.ContentIdeaMapper.toDomain
import com.justindwinata.usahanaik.data.mapper.ContentIdeaMapper.toEntity
import com.justindwinata.usahanaik.domain.model.ContentIdea
import com.justindwinata.usahanaik.domain.model.ContentIdeaFilter
import com.justindwinata.usahanaik.domain.model.ContentIdeaStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LocalContentIdeaRepository(
    private val dao: ContentIdeaDao,
    private val nowProvider: () -> Long = { System.currentTimeMillis() }
) : ContentIdeaRepository {
    override suspend fun saveIdea(idea: ContentIdea): ContentIdea {
        val now = nowProvider()
        val entity = idea.toEntity(now)
        val id = dao.insertIdea(entity)
        return entity.copy(id = id).toDomain()
    }

    override suspend fun listIdeas(filter: ContentIdeaFilter): List<ContentIdea> {
        return when (filter) {
            ContentIdeaFilter.All -> dao.listIdeas()
            ContentIdeaFilter.Draft -> dao.listIdeasByStatus(ContentIdeaStatus.Draft.name)
            ContentIdeaFilter.Planned -> dao.listIdeasByStatus(ContentIdeaStatus.Planned.name)
            ContentIdeaFilter.Done -> dao.listIdeasByStatus(ContentIdeaStatus.Done.name)
            ContentIdeaFilter.Favorite -> dao.listFavoriteIdeas()
        }.map { it.toDomain() }
    }

    override fun observeIdeas(): Flow<List<ContentIdea>> = dao.observeIdeas().map { ideas ->
        ideas.map { it.toDomain() }
    }

    override suspend fun updateStatus(id: Long, status: ContentIdeaStatus): ContentIdea? {
        dao.updateStatus(id, status.name, nowProvider())
        return dao.getIdea(id)?.toDomain()
    }

    override suspend fun updateFavorite(id: Long, isFavorite: Boolean): ContentIdea? {
        dao.updateFavorite(id, isFavorite, nowProvider())
        return dao.getIdea(id)?.toDomain()
    }

    override suspend fun deleteIdea(id: Long) {
        dao.deleteIdea(id)
    }

    override suspend fun clearIdeas() {
        dao.clearIdeas()
    }
}
