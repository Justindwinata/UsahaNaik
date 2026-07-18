package com.justindwinata.usahanaik.domain.model

data class ContentIdea(
    val title: String,
    val category: String,
    val platformSuggestion: String,
    val angle: String,
    val cta: String,
    val isAiAssistedPreview: Boolean = true
)
