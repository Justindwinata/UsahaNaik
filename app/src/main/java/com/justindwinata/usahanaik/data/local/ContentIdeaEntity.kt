package com.justindwinata.usahanaik.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "content_ideas")
data class ContentIdeaEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val businessProfileId: Long = BusinessProfileEntity.ACTIVE_PROFILE_ID,
    val title: String,
    val platform: String,
    val contentType: String,
    val goal: String,
    val angle: String,
    val captionDraft: String,
    val cta: String,
    val hook: String,
    val visualSuggestion: String,
    val postingNote: String,
    val relatedChallenge: String?,
    val source: String,
    val safetyNote: String,
    val status: String,
    val isFavorite: Boolean,
    val createdAt: Long,
    val updatedAt: Long
)
