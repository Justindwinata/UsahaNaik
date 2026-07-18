package com.justindwinata.usahanaik.ui.content

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.justindwinata.usahanaik.data.repository.BusinessProfileRepository
import com.justindwinata.usahanaik.data.repository.ContentIdeaRepository
import com.justindwinata.usahanaik.domain.ai.ContentIdeaProvider
import com.justindwinata.usahanaik.domain.model.ContentGoal
import com.justindwinata.usahanaik.domain.model.ContentIdea
import com.justindwinata.usahanaik.domain.model.ContentIdeaFilter
import com.justindwinata.usahanaik.domain.model.ContentIdeaRequest
import com.justindwinata.usahanaik.domain.model.ContentIdeaStatus
import com.justindwinata.usahanaik.domain.model.ContentIdeaType
import com.justindwinata.usahanaik.domain.model.ContentPlatform
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ContentPlannerViewModel(
    private val businessProfileRepository: BusinessProfileRepository,
    private val contentIdeaRepository: ContentIdeaRepository,
    private val contentIdeaProvider: ContentIdeaProvider
) : ViewModel() {
    private val _uiState = MutableStateFlow(ContentPlannerUiState(isLoading = true))
    val uiState: StateFlow<ContentPlannerUiState> = _uiState.asStateFlow()

    init {
        load()
    }

    fun load() {
        viewModelScope.launch {
            val profile = businessProfileRepository.getActiveBusinessProfile()
            _uiState.value = _uiState.value.copy(
                isLoading = false,
                hasProfile = profile != null,
                savedIdeas = contentIdeaRepository.listIdeas(_uiState.value.filter),
                errorMessage = null
            )
        }
    }

    fun updatePlatform(platform: ContentPlatform) = updateForm { copy(platform = platform) }
    fun updateGoal(goal: ContentGoal) = updateForm { copy(goal = goal) }
    fun updateType(type: ContentIdeaType?) = updateForm { copy(type = type) }
    fun updateIdeaCount(count: Int) = updateForm { copy(ideaCount = count.coerceIn(5, 10)) }

    fun updateFilter(filter: ContentIdeaFilter) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                filter = filter,
                savedIdeas = contentIdeaRepository.listIdeas(filter)
            )
        }
    }

    fun generateIdeas() {
        viewModelScope.launch {
            val profile = businessProfileRepository.getActiveBusinessProfile()
            if (profile == null) {
                _uiState.value = _uiState.value.copy(
                    hasProfile = false,
                    errorMessage = "Complete business setup first."
                )
                return@launch
            }
            _uiState.value = _uiState.value.copy(isGenerating = true, errorMessage = null, successMessage = null)
            runCatching {
                contentIdeaProvider.generateIdeas(
                    ContentIdeaRequest(
                        businessProfile = profile,
                        goal = _uiState.value.form.goal,
                        platform = _uiState.value.form.platform,
                        type = _uiState.value.form.type,
                        ideaCount = _uiState.value.form.ideaCount
                    )
                )
            }.onSuccess { result ->
                _uiState.value = _uiState.value.copy(
                    isGenerating = false,
                    hasProfile = true,
                    generatedIdeas = result.ideas,
                    promotionCampaigns = result.promotionCampaigns,
                    generationSource = result.source,
                    usedFallback = result.usedFallback,
                    successMessage = result.message
                )
            }.onFailure { error ->
                _uiState.value = _uiState.value.copy(
                    isGenerating = false,
                    errorMessage = error.message ?: "Failed to generate content ideas."
                )
            }
        }
    }

    fun saveIdea(idea: ContentIdea) {
        viewModelScope.launch {
            val saved = contentIdeaRepository.saveIdea(idea)
            _uiState.value = _uiState.value.copy(
                savedIdeas = contentIdeaRepository.listIdeas(_uiState.value.filter),
                generatedIdeas = _uiState.value.generatedIdeas.map { if (it.title == idea.title) saved else it },
                successMessage = "Content idea saved locally."
            )
        }
    }

    fun markPlanned(id: Long) = updateStatus(id, ContentIdeaStatus.Planned)
    fun markDone(id: Long) = updateStatus(id, ContentIdeaStatus.Done)
    fun markDraft(id: Long) = updateStatus(id, ContentIdeaStatus.Draft)

    fun toggleFavorite(id: Long) {
        viewModelScope.launch {
            val idea = contentIdeaRepository.listIdeas().firstOrNull { it.id == id } ?: return@launch
            contentIdeaRepository.updateFavorite(id, !idea.isFavorite)
            refreshSavedIdeas()
        }
    }

    fun deleteIdea(id: Long) {
        viewModelScope.launch {
            contentIdeaRepository.deleteIdea(id)
            refreshSavedIdeas()
        }
    }

    private fun updateStatus(id: Long, status: ContentIdeaStatus) {
        viewModelScope.launch {
            contentIdeaRepository.updateStatus(id, status)
            refreshSavedIdeas()
        }
    }

    private suspend fun refreshSavedIdeas() {
        _uiState.value = _uiState.value.copy(savedIdeas = contentIdeaRepository.listIdeas(_uiState.value.filter))
    }

    private fun updateForm(reducer: ContentGenerationFormState.() -> ContentGenerationFormState) {
        _uiState.value = _uiState.value.copy(form = _uiState.value.form.reducer())
    }
}

class ContentPlannerViewModelFactory(
    private val businessProfileRepository: BusinessProfileRepository,
    private val contentIdeaRepository: ContentIdeaRepository,
    private val contentIdeaProvider: ContentIdeaProvider
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ContentPlannerViewModel::class.java)) {
            return ContentPlannerViewModel(
                businessProfileRepository = businessProfileRepository,
                contentIdeaRepository = contentIdeaRepository,
                contentIdeaProvider = contentIdeaProvider
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}
