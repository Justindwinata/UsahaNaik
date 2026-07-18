package com.justindwinata.usahanaik.ui.content

import com.justindwinata.usahanaik.data.ai.FallbackContentIdeaProvider
import com.justindwinata.usahanaik.data.ai.LocalContentIdeaProvider
import com.justindwinata.usahanaik.data.repository.BusinessProfileRepository
import com.justindwinata.usahanaik.data.repository.ContentIdeaRepository
import com.justindwinata.usahanaik.domain.ai.ContentIdeaProvider
import com.justindwinata.usahanaik.domain.model.BusinessCategory
import com.justindwinata.usahanaik.domain.model.BusinessChallenge
import com.justindwinata.usahanaik.domain.model.BusinessProfile
import com.justindwinata.usahanaik.domain.model.BusinessSetupDraft
import com.justindwinata.usahanaik.domain.model.ContentGenerationSource
import com.justindwinata.usahanaik.domain.model.ContentGoal
import com.justindwinata.usahanaik.domain.model.ContentIdea
import com.justindwinata.usahanaik.domain.model.ContentIdeaFilter
import com.justindwinata.usahanaik.domain.model.ContentIdeaRequest
import com.justindwinata.usahanaik.domain.model.ContentIdeaResult
import com.justindwinata.usahanaik.domain.model.ContentIdeaStatus
import com.justindwinata.usahanaik.domain.model.ContentPlatform
import com.justindwinata.usahanaik.test.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ContentPlannerViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun showsEmptyStateWithoutProfile() = runTest {
        val viewModel = ContentPlannerViewModel(
            businessProfileRepository = FakeBusinessProfileRepository(null),
            contentIdeaRepository = FakeContentIdeaRepository(),
            contentIdeaProvider = LocalContentIdeaProvider()
        )
        advanceUntilIdle()

        assertFalse(viewModel.uiState.value.isLoading)
        assertFalse(viewModel.uiState.value.hasProfile)

        viewModel.generateIdeas()
        advanceUntilIdle()

        assertEquals("Complete business setup first.", viewModel.uiState.value.errorMessage)
    }

    @Test
    fun generateLocalIdeasWithSavedProfile() = runTest {
        val viewModel = ContentPlannerViewModel(
            businessProfileRepository = FakeBusinessProfileRepository(sampleProfile()),
            contentIdeaRepository = FakeContentIdeaRepository(),
            contentIdeaProvider = LocalContentIdeaProvider()
        )
        advanceUntilIdle()

        viewModel.updatePlatform(ContentPlatform.TikTok)
        viewModel.updateGoal(ContentGoal.ImproveContentConsistency)
        viewModel.generateIdeas()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue(state.hasProfile)
        assertTrue(state.generatedIdeas.size in 5..10)
        assertEquals(ContentGenerationSource.Local, state.generationSource)
        assertTrue(state.generatedIdeas.all { it.platform == ContentPlatform.TikTok })
    }

    @Test
    fun usesFallbackIdeasWhenPrimaryProviderFails() = runTest {
        val viewModel = ContentPlannerViewModel(
            businessProfileRepository = FakeBusinessProfileRepository(sampleProfile()),
            contentIdeaRepository = FakeContentIdeaRepository(),
            contentIdeaProvider = FallbackContentIdeaProvider(
                primaryProvider = FailingProvider(),
                fallbackProvider = LocalContentIdeaProvider()
            )
        )
        advanceUntilIdle()

        viewModel.generateIdeas()
        advanceUntilIdle()

        assertTrue(viewModel.uiState.value.usedFallback)
        assertEquals(ContentGenerationSource.Fallback, viewModel.uiState.value.generationSource)
    }

    @Test
    fun saveIdeaPersistsGeneratedIdea() = runTest {
        val repository = FakeContentIdeaRepository()
        val viewModel = ContentPlannerViewModel(
            businessProfileRepository = FakeBusinessProfileRepository(sampleProfile()),
            contentIdeaRepository = repository,
            contentIdeaProvider = LocalContentIdeaProvider()
        )
        advanceUntilIdle()
        viewModel.generateIdeas()
        advanceUntilIdle()
        val idea = viewModel.uiState.value.generatedIdeas.first()

        viewModel.saveIdea(idea)
        advanceUntilIdle()

        assertEquals(1, repository.listIdeas().size)
        assertEquals("Content idea saved locally.", viewModel.uiState.value.successMessage)
    }

    @Test
    fun markPlannedDoneFavoriteAndDeleteUpdateSavedIdeas() = runTest {
        val repository = FakeContentIdeaRepository()
        val saved = repository.saveIdea(sampleIdea())
        val viewModel = ContentPlannerViewModel(
            businessProfileRepository = FakeBusinessProfileRepository(sampleProfile()),
            contentIdeaRepository = repository,
            contentIdeaProvider = LocalContentIdeaProvider()
        )
        advanceUntilIdle()

        viewModel.markPlanned(saved.id)
        advanceUntilIdle()
        assertEquals(ContentIdeaStatus.Planned, viewModel.uiState.value.savedIdeas.first().status)

        viewModel.markDone(saved.id)
        advanceUntilIdle()
        assertEquals(ContentIdeaStatus.Done, viewModel.uiState.value.savedIdeas.first().status)

        viewModel.toggleFavorite(saved.id)
        advanceUntilIdle()
        assertTrue(viewModel.uiState.value.savedIdeas.first().isFavorite)

        viewModel.deleteIdea(saved.id)
        advanceUntilIdle()
        assertTrue(viewModel.uiState.value.savedIdeas.isEmpty())
    }

    @Test
    fun filtersSavedIdeasByStatus() = runTest {
        val repository = FakeContentIdeaRepository()
        repository.saveIdea(sampleIdea().copy(title = "Draft idea"))
        repository.saveIdea(sampleIdea().copy(title = "Planned idea", status = ContentIdeaStatus.Planned))
        val viewModel = ContentPlannerViewModel(
            businessProfileRepository = FakeBusinessProfileRepository(sampleProfile()),
            contentIdeaRepository = repository,
            contentIdeaProvider = LocalContentIdeaProvider()
        )
        advanceUntilIdle()

        viewModel.updateFilter(ContentIdeaFilter.Planned)
        advanceUntilIdle()

        assertEquals(1, viewModel.uiState.value.savedIdeas.size)
        assertEquals("Planned idea", viewModel.uiState.value.savedIdeas.first().title)
    }

    private fun sampleProfile(): BusinessProfile {
        return BusinessProfile(
            draft = BusinessSetupDraft(
                businessName = "Glow Nusantara",
                categoryId = "skincare_beauty",
                challenges = setOf(BusinessChallenge.InconsistentContent, BusinessChallenge.LowRepeatOrders),
                targetMonthlyRevenue = "Rp 15.000.000",
                targetMonthlyProfit = "Rp 5.000.000"
            ),
            createdAt = 1L,
            updatedAt = 1L
        )
    }

    private fun sampleIdea(): ContentIdea {
        return ContentIdea(
            title = "3 tips memilih produk untuk pemula",
            category = "Skincare & Beauty",
            platformSuggestion = "Instagram Reels",
            angle = "Educate customers with safe product education.",
            cta = "Save this post before buying your next product.",
            platform = ContentPlatform.InstagramReels,
            goal = ContentGoal.ProductEducation,
            hook = "Masih bingung mulai dari mana?",
            captionDraft = "Mulai dari kebutuhan kulit dan review komposisi produk secara bijak.",
            visualSuggestion = "Show product texture and simple routine steps."
        )
    }

    private class FakeBusinessProfileRepository(initialProfile: BusinessProfile?) : BusinessProfileRepository {
        private val profile = MutableStateFlow(initialProfile)

        override suspend fun saveBusinessProfile(draft: BusinessSetupDraft): BusinessProfile {
            val saved = BusinessProfile(draft = draft, createdAt = 1L, updatedAt = 1L)
            profile.value = saved
            return saved
        }

        override suspend fun getActiveBusinessProfile(): BusinessProfile? = profile.value

        override fun observeActiveBusinessProfile(): Flow<BusinessProfile?> = profile

        override suspend fun deleteBusinessProfile() {
            profile.value = null
        }

        override suspend fun hasBusinessProfile(): Boolean = profile.value != null
    }

    private class FakeContentIdeaRepository : ContentIdeaRepository {
        private val ideas = MutableStateFlow<List<ContentIdea>>(emptyList())
        private var nextId = 1L

        override suspend fun saveIdea(idea: ContentIdea): ContentIdea {
            val saved = idea.copy(
                id = if (idea.id == 0L) nextId++ else idea.id,
                updatedAt = 1L,
                createdAt = if (idea.createdAt == 0L) 1L else idea.createdAt
            )
            ideas.value = ideas.value.filterNot { it.id == saved.id } + saved
            return saved
        }

        override suspend fun listIdeas(filter: ContentIdeaFilter): List<ContentIdea> {
            return when (filter) {
                ContentIdeaFilter.All -> ideas.value
                ContentIdeaFilter.Draft -> ideas.value.filter { it.status == ContentIdeaStatus.Draft }
                ContentIdeaFilter.Planned -> ideas.value.filter { it.status == ContentIdeaStatus.Planned }
                ContentIdeaFilter.Done -> ideas.value.filter { it.status == ContentIdeaStatus.Done }
                ContentIdeaFilter.Favorite -> ideas.value.filter { it.isFavorite }
            }
        }

        override fun observeIdeas(): Flow<List<ContentIdea>> = ideas

        override suspend fun updateStatus(id: Long, status: ContentIdeaStatus): ContentIdea? {
            var updated: ContentIdea? = null
            ideas.value = ideas.value.map {
                if (it.id == id) {
                    it.copy(status = status).also { idea -> updated = idea }
                } else {
                    it
                }
            }
            return updated
        }

        override suspend fun updateFavorite(id: Long, isFavorite: Boolean): ContentIdea? {
            var updated: ContentIdea? = null
            ideas.value = ideas.value.map {
                if (it.id == id) {
                    it.copy(isFavorite = isFavorite).also { idea -> updated = idea }
                } else {
                    it
                }
            }
            return updated
        }

        override suspend fun deleteIdea(id: Long) {
            ideas.value = ideas.value.filterNot { it.id == id }
        }

        override suspend fun clearIdeas() {
            ideas.value = emptyList()
        }
    }

    private class FailingProvider : ContentIdeaProvider {
        override fun generateIdeas(category: BusinessCategory, businessName: String): List<ContentIdea> {
            error("not used")
        }

        override fun generateIdeas(request: ContentIdeaRequest): ContentIdeaResult {
            error("AI failed")
        }
    }
}
