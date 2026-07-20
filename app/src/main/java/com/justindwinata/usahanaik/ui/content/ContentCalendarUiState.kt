package com.justindwinata.usahanaik.ui.content

import com.justindwinata.usahanaik.domain.model.ContentCalendarSchedule
import com.justindwinata.usahanaik.domain.model.ContentCalendarStatus
import com.justindwinata.usahanaik.domain.model.ContentIdea
import com.justindwinata.usahanaik.domain.model.ContentPlatform

data class ContentScheduleFormState(
    val contentIdeaId: Long = 0L,
    val title: String = "",
    val platform: ContentPlatform = ContentPlatform.InstagramFeed,
    val scheduledDate: String = "",
    val timeLabel: String = "",
    val postingNote: String = ""
) {
    val isActive: Boolean = contentIdeaId > 0L
    val dateError: String? = if (isActive && scheduledDate.isBlank()) "Schedule date is required." else null
    val canSave: Boolean = isActive && scheduledDate.isNotBlank()
}

data class ContentCalendarUiState(
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val schedules: List<ContentCalendarSchedule> = emptyList(),
    val form: ContentScheduleFormState = ContentScheduleFormState(),
    val successMessage: String? = null,
    val errorMessage: String? = null
) {
    val upcomingSchedules: List<ContentCalendarSchedule>
        get() = schedules.sortedWith(compareBy<ContentCalendarSchedule> { it.scheduledDate }.thenBy { it.timeLabel })
}

fun ContentIdea.toScheduleForm(defaultDate: String): ContentScheduleFormState {
    return ContentScheduleFormState(
        contentIdeaId = id,
        title = title,
        platform = platform,
        scheduledDate = defaultDate,
        postingNote = recommendedPostingNote
    )
}

fun ContentScheduleFormState.toSchedule(): ContentCalendarSchedule {
    return ContentCalendarSchedule(
        contentIdeaId = contentIdeaId,
        title = title,
        platform = platform,
        scheduledDate = scheduledDate,
        timeLabel = timeLabel,
        postingNote = postingNote,
        status = ContentCalendarStatus.Planned
    )
}
