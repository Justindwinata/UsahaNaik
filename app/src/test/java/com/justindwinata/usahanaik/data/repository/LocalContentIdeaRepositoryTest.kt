package com.justindwinata.usahanaik.data.repository

import com.justindwinata.usahanaik.data.local.ContentIdeaDao
import com.justindwinata.usahanaik.data.local.ContentIdeaEntity
import com.justindwinata.usahanaik.domain.model.ContentGoal
import com.justindwinata.usahanaik.domain.model.ContentIdea
import com.justindwinata.usahanaik.domain.model.ContentIdeaFilter
import com.justindwinata.usahanaik.domain.model.ContentIdeaStatus
import com.justindwinata.usahanaik.domain.model.ContentIdeaType
import com.justindwinata.usahanaik.domain.model.ContentPlatform
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class LocalContentIdeaRepositoryTest {
    @Test
    fun savesAndListsIdea() = runTest {
        val repository = LocalContentIdeaRepository(FakeContentIdeaDao(), nowProvider = { 100L })

        val saved = repository.saveIdea(sampleIdea())

        assertEquals(1L, saved.id)
        assertEquals(1, repository.listIdeas().size)
    }

    @Test
    fun updatesStatusAndFavorite() = runTest {
        val repository = LocalContentIdeaRepository(FakeContentIdeaDao(), nowProvider = { 100L })
        val saved = repository.saveIdea(sampleIdea())

        repository.updateStatus(saved.id, ContentIdeaStatus.Planned)
        repository.updateFavorite(saved.id, true)

        assertEquals(1, repository.listIdeas(ContentIdeaFilter.Planned).size)
        assertEquals(1, repository.listIdeas(ContentIdeaFilter.Favorite).size)
        assertTrue(repository.listIdeas().first().isFavorite)
    }

    @Test
    fun deletesIdea() = runTest {
        val dao = FakeContentIdeaDao()
        val repository = LocalContentIdeaRepository(dao, nowProvider = { 100L })
        val saved = repository.saveIdea(sampleIdea())

        repository.deleteIdea(saved.id)

        assertNull(dao.getIdea(saved.id))
    }

    private fun sampleIdea(): ContentIdea = ContentIdea(
        title = "Tips produk",
        category = ContentIdeaType.Educational.label,
        platformSuggestion = ContentPlatform.InstagramReels.label,
        angle = "Educate customers.",
        cta = "Save this post.",
        type = ContentIdeaType.Educational,
        platform = ContentPlatform.InstagramReels,
        goal = ContentGoal.ProductEducation,
        captionDraft = "Caption",
        hook = "Hook",
        visualSuggestion = "Visual",
        recommendedPostingNote = "Note"
    )

    private class FakeContentIdeaDao : ContentIdeaDao {
        private val ideas = MutableStateFlow<List<ContentIdeaEntity>>(emptyList())
        private var nextId = 1L

        override suspend fun insertIdea(idea: ContentIdeaEntity): Long {
            val id = if (idea.id == 0L) nextId++ else idea.id
            ideas.value = ideas.value.filterNot { it.id == id } + idea.copy(id = id)
            return id
        }

        override suspend fun updateIdea(idea: ContentIdeaEntity) {
            ideas.value = ideas.value.map { if (it.id == idea.id) idea else it }
        }

        override suspend fun getIdea(id: Long): ContentIdeaEntity? = ideas.value.firstOrNull { it.id == id }

        override suspend fun listIdeas(): List<ContentIdeaEntity> = ideas.value.sortedByDescending { it.updatedAt }

        override fun observeIdeas(): Flow<List<ContentIdeaEntity>> = ideas

        override suspend fun listIdeasByStatus(status: String): List<ContentIdeaEntity> {
            return listIdeas().filter { it.status == status }
        }

        override suspend fun listFavoriteIdeas(): List<ContentIdeaEntity> = listIdeas().filter { it.isFavorite }

        override suspend fun updateStatus(id: Long, status: String, updatedAt: Long) {
            ideas.value = ideas.value.map { if (it.id == id) it.copy(status = status, updatedAt = updatedAt) else it }
        }

        override suspend fun updateFavorite(id: Long, isFavorite: Boolean, updatedAt: Long) {
            ideas.value = ideas.value.map { if (it.id == id) it.copy(isFavorite = isFavorite, updatedAt = updatedAt) else it }
        }

        override suspend fun deleteIdea(id: Long) {
            ideas.value = ideas.value.filterNot { it.id == id }
        }

        override suspend fun clearIdeas() {
            ideas.value = emptyList()
        }
    }
}
